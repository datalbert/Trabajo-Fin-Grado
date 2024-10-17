package trabajotfg.pago.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import lombok.RequiredArgsConstructor;
import trabajotfg.pago.dto.OrderDto;
import trabajotfg.pago.entity.Transacciones;
import trabajotfg.pago.mapped.TransaccionesMapped;
import trabajotfg.pago.repository.TransaccionesRepository;


@Service
@RequiredArgsConstructor
public class PaypalServiceImpl implements PaypalService {
    
    private final APIContext apiContext;

    private final TransaccionesRepository transaccionesRepository;



    public Payment createPayment(
            Double total, 
            String currency, 
            String method, 
            String intent, 
            String description, 
            String cancelUrl, 
            String successUrl) throws PayPalRESTException {

        //almacenamos en la base de datos la transaccion
        
        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format(Locale.forLanguageTag(currency), "%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(method);

        Payment payment = new Payment();
        payment.setIntent(intent);
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);

        payment.setRedirectUrls(redirectUrls);

        Payment createdPayment = payment.create(apiContext);

        String id_payment=createdPayment.getId();
        //Persistimos los datos en la base de datos

        

        return createdPayment;

    }

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
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);

        payment.setRedirectUrls(redirectUrls);

        Payment createdPayment = payment.create(apiContext);

        String id_payment=createdPayment.getId();
        
        //Persistimos los datos en la base de datos
        Transacciones transaccion = new Transacciones();
        transaccion=TransaccionesMapped.maptoEntity(order, transaccion);
        transaccion.setId_transaccion(id_payment);
        transaccion.setEstado("PENDIENTE");
        transaccionesRepository.save(transaccion);

        return createdPayment;

    }

}
