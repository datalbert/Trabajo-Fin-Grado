package trabajotfg.pago.controller;


import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import trabajotfg.pago.service.PaypalService;

import java.lang.ProcessBuilder.Redirect;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import trabajotfg.pago.dto.OrderDto;




@RestController
@RequiredArgsConstructor 
@Slf4j
public class PaypalController {

    private final PaypalService paypalService;

    public static final String cancelUrl = "http://localhost:9090/payment/cancel";
    public static final String successUrl = "http://localhost:9090/payment/success";


    @PostMapping()
    public String createPayment(@RequestPart("order") OrderDto order) {

        try {
            
            //Payment payment = paypalService.createPayment(100.0, "USD", "paypal", "sale", "payment description", cancelUrl, successUrl);
            //Payment payment = paypalService.createPayment(order.getPrice(), order.getCurrency(), order.getMethod(), order.getIntent(), order.getDescription(), cancelUrl, successUrl);
            Payment payment = paypalService.createPayment2(order, cancelUrl, successUrl);
            
            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    //return new RedirectView(link.getHref());
                    return "redirect:"+link.getHref();
                }
            }   
        } catch (PayPalRESTException e) {
            log.error("Ha ocurrido un error en createPayment"+ e.getMessage());
            return null;
        }
        //if the payment is not successful, redirect to the error page
        //return new RedirectView("/payment/error");
        return "redirect:/payment/error";
    }

    @GetMapping("/payment/success")
    public RedirectView successPayment(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        RedirectView redirectView = new RedirectView();
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                // Redireccionar al frontend
                
                redirectView.setUrl("http://localhost:4200/");
            } else {
                // Redirigir al error o una página específica
                redirectView.setUrl("http://localhost:4200/error");
            }
        } catch (PayPalRESTException e) {
            log.error("Error en successPayment: " + e.getMessage());
            redirectView.setUrl("http://localhost:4200/error");
        }
        return redirectView;
    }

    @GetMapping("/payment/cancel")
    public String cancelPayment() {
        return "paymentCancel";
    }

    @GetMapping("/payment/error")
    public String errorPayment() {
        return "paymenterror";
    }

    @PostMapping("/refund")
    public String postMethodName(@RequestBody String entity) {
        //TODO: process POST request
        //este método es para hacer el reembolso en caso de cancelar la reserva
        return entity;
    }
    
    

}
