package trabajotfg.reservas.service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import trabajotfg.reservas.dto.OrderDto;

@FeignClient(name = "pago")
public interface PagoClient {

    @RequestMapping(method = RequestMethod.POST, value = "/payment")
    public String createPayment(@RequestBody OrderDto order);

    @RequestMapping(method= RequestMethod.DELETE , value= "/payment/refund")
    public String cancelarReserva(@RequestParam("id_reserva") int idreserva, @RequestParam("monto") String monto);

    

}
