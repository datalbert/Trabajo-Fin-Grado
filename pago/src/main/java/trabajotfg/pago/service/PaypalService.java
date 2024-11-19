package trabajotfg.pago.service;

import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import org.springframework.stereotype.Service;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.DetailedRefund;
import com.paypal.api.payments.Order;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Refund;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import lombok.RequiredArgsConstructor;
import trabajotfg.pago.dto.OrderDto;


public interface PaypalService {

    

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException;

    public void  escribirEnCola(String paymentId, String estado);

    public void pagoCancelado(int id_reserva, String estado);

    public Payment createPayment2(OrderDto order,String cancelUrl, String successUrl) throws PayPalRESTException;

    public DetailedRefund refundPayment(int id_reserva, String amount) throws PayPalRESTException;

}
