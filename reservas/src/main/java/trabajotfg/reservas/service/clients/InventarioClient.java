package trabajotfg.reservas.service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import trabajotfg.reservas.dto.InventarioDto;
import trabajotfg.reservas.dto.ResponseDTO;

@FeignClient(name = "inventario")
public interface InventarioClient {

    //definimos los método que vamos a llamar
    @RequestMapping(method = RequestMethod.PUT, value = "/actualizarEstado")
    public ResponseEntity<ResponseDTO> actualizarEstado(@RequestParam("idvehiculo") int idCoche, @RequestParam("estado") String estado);

    //queremos obtener el vehiculo por su id
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<InventarioDto> obtenerVehiculo(@PathVariable("id")  int id);





}
