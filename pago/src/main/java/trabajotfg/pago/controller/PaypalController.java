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
import java.util.Optional;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import trabajotfg.pago.dto.OrderDto;
import trabajotfg.pago.entity.Transacciones;




@RestController
@RequiredArgsConstructor 
@Slf4j
@OpenAPIDefinition(
    info = @Info(
        title = "RentaMov API",
        version = "1.0",
        description = "API general para la gestión de inventario, reservas, pagos y localización en el sistema de alquiler de vehículos.",
        contact = @Contact(
            name = "Alberto Ávila Fernández",
            email = "albertoaf0520@gmail.com",
            url = "https://www.rentamov.com"
        )
    )
)
@Tag(name = "Paypal", description = "API para la Gestión de Pagos con Paypal")
public class PaypalController {

    private final PaypalService paypalService;

    public static final String cancelUrl = "http://localhost:9090/payment/cancel";
    public static final String successUrl = "http://localhost:9090/payment/success";

    @Operation(summary = "Crear una solicitud de pago con PayPal")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "URL de redirección para completar el pago en PayPal",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Error en los datos de la orden",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno en el servidor al crear la solicitud de pago",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
        )
    })
    @PostMapping("/payment")
    public String createPayment(
        @Parameter(description = "Objeto OrderDto que contiene los detalles de la orden", required = true)
        @RequestBody OrderDto order) {

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
    @Operation(summary = "Redirección después de un pago exitoso en PayPal")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "302",
            description = "Redirección a la URL del frontend para confirmar el pago",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Faltan parámetros de identificación de pago",
            content = @Content(mediaType = "application/json")
        )
    })
    @GetMapping("/payment/success")
    public RedirectView successPayment(
        @Parameter(description = "ID del pago proporcionado por PayPal", required = true, example = "PAY-123456789")
        @RequestParam("paymentId") String paymentId,
        @Parameter(description = "ID del pagador proporcionado por PayPal", required = true, example = "PAYER-12345")
        @RequestParam("PayerID") String payerId) {
        RedirectView redirectView = new RedirectView();
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {

                //enviamos a la cola el mensaje de aceptado
                paypalService.escribirEnCola(paymentId, "Aceptado");

                Optional<Transacciones> transaccion_realizada=paypalService.obtenerTransaccionById(paymentId);
                
                // Redireccionar al frontend
                redirectView.setUrl("http://localhost:4200/pago?id_transaccion="+paymentId+"&precio="+transaccion_realizada.get().getMonto()+"&fecha="+transaccion_realizada.get().getDescripcion_metodo_pago());


            } else {
                //enviamos a la cola el mensaje de rechazado
                paypalService.escribirEnCola(paymentId, "Rechazado");
                // Redirigir al error o una página específica
                redirectView.setUrl("http://localhost:4200/error");
            }
        } catch (PayPalRESTException e) {
            log.error("Error en successPayment: " + e.getMessage());
            redirectView.setUrl("http://localhost:4200/error");
        }
        return redirectView;
    }
    @Operation(summary = "Redirección tras la cancelación de un pago en PayPal")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "302",
            description = "Redirección a la página de error del frontend tras la cancelación del pago",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Parámetro de id de reserva faltante o inválido",
            content = @Content(mediaType = "application/json")
        )
    })
    @GetMapping("/payment/cancel")
    public RedirectView cancelPayment(
        @Parameter(description = "ID de la reserva asociada al pago cancelado", required = true, example = "123")
        @RequestParam("id_reserva") int id_reserva) {
        RedirectView redirectView = new RedirectView();
        //enviar a la cola mensaje de error

        paypalService.pagoCancelado(id_reserva, "Cancelado");

        // Redirigir al error o una página específica
        redirectView.setUrl("http://localhost:4200/error");

        return redirectView;
        
        
    }

    @GetMapping("/payment/error")
    public String errorPayment() {
        return "paymenterror";
    }
    @Operation(summary = "Solicitar un reembolso para una reserva")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Reembolso realizado correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos (falta id de reserva o monto)",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno en el servidor al procesar el reembolso",
            content = @Content(mediaType = "application/json")
        )
    })
    @DeleteMapping("/payment/refund")
    public String cancelarReserva(
        @Parameter(name = "id_reserva", description = "ID de la reserva que se quiere reembolsar", required = true, example = "123")
        @RequestParam("id_reserva") int idreserva, 
        @Parameter(name = "monto", description = "Cantidad monetaria a reembolsar", required = true, example = "100.50")
        @RequestParam("monto") String monto) {
        //TODO: process POST request
        //este método es para hacer el reembolso en caso de cancelar la reserva
        try {
            paypalService.refundPayment(idreserva, monto);
            return "success";
        } catch (PayPalRESTException e) {
            log.error("Error en refundPayment: " + e.getMessage());
            return "error";
        }
    }
    
    

}
