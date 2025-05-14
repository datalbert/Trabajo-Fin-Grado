package trabajotfg.pago.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.DetailedRefund;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Refund;
import com.paypal.api.payments.RefundRequest;
import com.paypal.api.payments.Sale;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import lombok.RequiredArgsConstructor;
import trabajotfg.pago.dto.OrderDto;
import trabajotfg.pago.dto.TransaccionMsgDto;
import trabajotfg.pago.entity.Transacciones;
import trabajotfg.pago.mapped.TransaccionesMapped;
import trabajotfg.pago.repository.TransaccionesRepository;


@Service
@RequiredArgsConstructor
public class PaypalServiceImpl implements PaypalService {
    
    private final APIContext apiContext;

    private final TransaccionesRepository transaccionesRepository;

    @Autowired
	private StreamBridge streamBridge;

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);
        
        //actualizamos la base de datos con el estado de la transaccion y el id del pagador
        Optional<Transacciones> transaccion= transaccionesRepository.findById(paymentId);
        transaccion.get().setEstado("COMPLETADO");
        transaccion.get().setId_pagador(payerId);
        transaccionesRepository.save(transaccion.get());

        return payment.execute(apiContext, paymentExecution);

    }

    public Payment createPayment2(
            OrderDto order,
            String cancelUrl, 
            String successUrl) throws PayPalRESTException {

        //almacenamos en la base de datos la transaccion
        
        Amount amount = new Amount();
        amount.setCurrency(order.getCurrency());
        amount.setTotal(String.format(Locale.forLanguageTag(order.getCurrency()), "%.2f", order.getPrice()));

        Transaction transaction = new Transaction();
        transaction.setDescription(order.getDescription());
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(order.getMethod());

        Payment payment = new Payment();
        payment.setIntent(order.getIntent());
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        
        
        RedirectUrls redirectUrls = new RedirectUrls();
        //añadir parametro a la url de cancelar con el id de la reserva
        redirectUrls.setCancelUrl(cancelUrl+"?id_reserva="+order.getId_reserva());
        redirectUrls.setReturnUrl(successUrl);

        payment.setRedirectUrls(redirectUrls);
        Payment createdPayment = payment.create(apiContext);

        String id_payment=createdPayment.getId();
    
        
        
        //Persistimos los datos en la base de datos
        Transacciones transaccion = new Transacciones();
        transaccion=TransaccionesMapped.maptoEntity(order, transaccion);
        transaccion.setIdTransaccion(id_payment);
        transaccion.setEstado("PENDIENTE");
        transaccionesRepository.save(transaccion);

        return createdPayment;

    }

    @Override
    public DetailedRefund refundPayment(int id_reserva, String amount) throws PayPalRESTException {
        //fetch the transaction from the database using the id of the reservation
        Transacciones transaccion=transaccionesRepository.findByIdReserva(id_reserva);

        //fetch id_transaccion from database
        String id_transaccion=transaccion.getIdTransaccion();

        //Retrieve the payment to get the sale details

        Payment payment= Payment.get(apiContext, id_transaccion);

         // Check if the payment has transactions
        if (payment.getTransactions() != null && !payment.getTransactions().isEmpty()) {
            // Get the first transaction
            Transaction transaction = payment.getTransactions().get(0);
            
            // Check if related resources exist
            if (transaction.getRelatedResources() != null && !transaction.getRelatedResources().isEmpty()) {
                String saleId = transaction.getRelatedResources().get(0).getSale().getId();

                // Create Amount to refund
                Amount amountRefund = new Amount();
                amountRefund.setCurrency(transaccion.getMoneda()); // e.g., USD
                amountRefund.setTotal(amount); // Full or partial refund

                // Create RefundRequest object
                RefundRequest refundRequest = new RefundRequest();
                refundRequest.setAmount(amountRefund);

                // Create Sale object
                Sale sale = Sale.get(apiContext, saleId);

                if (sale == null) {
                    throw new PayPalRESTException("Sale not found");
                }
                else{
                    DetailedRefund refund = sale.refund(apiContext, refundRequest);
                    transaccion.setEstado("REEMBOLSADO");
                    transaccionesRepository.save(transaccion);
                    return refund;
                }        
                // Optional: Update transaccion status in your database
                
            }
        }

        return null;


    }

    @Override
    public void escribirEnCola(String paymentId, String estado) {
        /*
        Este método se encarga de escribir en la cola de mensajes
        primero obteniendo el id de la reserva asociado al id de la transaccion el estado corresponeinte 
         Estado --> aceptado
            Estado --> rechazado
            Estado --> cancelado
        */
        
        Optional<Transacciones> transaccion=transaccionesRepository.findById(paymentId);

        //enviar a la cola de mensajes

        System.out.println("Estado de la reserva: "+estado);

        var result =streamBridge.send("sendPagoConfirmation-out-0", new TransaccionMsgDto(transaccion.get().getIdReserva(), 0, estado));

        System.out.println("Mensaje enviado a la cola de mensajes: "+result);
    }

    @Override
    public void pagoCancelado(int id_reserva, String estado) {
        /*
         * Este método se ejecutara una vez cancelado el pago haciendo las siguientes acciones:
         *  1º -> Eliminar de la base de datos la transaccion asociada a la reserva
         *  2º -> Enviar a la cola de mensajes el mensaje de cancelado
         */

         Transacciones transaccion_cancelada=transaccionesRepository.findByIdReserva(id_reserva);

         transaccionesRepository.delete(transaccion_cancelada);

         var result =streamBridge.send("sendPagoConfirmation-out-0", new TransaccionMsgDto(id_reserva, 0, estado));

         System.out.println("Mensaje enviado a la cola de mensajes: "+result);


    }

    @Override
    public Optional<Transacciones> obtenerTransaccionById(String paymentId) {
        // TODO Auto-generated method stub
        return transaccionesRepository.findByIdTransaccion(paymentId);
    }

}
