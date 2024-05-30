package trabajotfg.reservas.service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import trabajotfg.reservas.dto.ResponseDTO;

@FeignClient(name = "inventario")
public interface InventarioClient {

    //definimos los método que vamos a llamar
    @RequestMapping(method = RequestMethod.PUT, value = "/inventario/actualizarEstado")
    public ResponseEntity<ResponseDTO> actualizarEstado(@RequestParam("idvehiculo") int idCoche, @RequestParam("estado") String estado);




}
