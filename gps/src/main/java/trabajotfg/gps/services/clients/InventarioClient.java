package trabajotfg.gps.services.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import trabajotfg.gps.dto.InventarioDto;
import trabajotfg.gps.dto.ResponseDTO;

@FeignClient(name = "inventario")
public interface InventarioClient {

    //definimos los método que vamos a llamar
    @RequestMapping(method = RequestMethod.PUT, value = "coches/{idvehiculo}/estado")
    public ResponseEntity<ResponseDTO> actualizarEstado(@PathVariable int idvehiculo, @RequestBody String estado);

    @RequestMapping(method = RequestMethod.GET, value = "/coches/matricula/{matricula}")
    public ResponseEntity<InventarioDto> obtenerCochePorMatricula(@PathVariable("matricula") String matricula);


}
