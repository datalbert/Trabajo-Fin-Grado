package trabajotfg.gps.services.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import trabajotfg.gps.dto.ResponseDTO;

@FeignClient(name = "reservas")
public interface ReservasClient {

     @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<ResponseDTO> actualizarEstadoReserva(@PathVariable int id);

}
