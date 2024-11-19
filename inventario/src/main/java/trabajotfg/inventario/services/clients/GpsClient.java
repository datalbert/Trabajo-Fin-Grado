package trabajotfg.inventario.services.clients;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.List;

import jakarta.ws.rs.client.Entity;
import trabajotfg.inventario.dto.EntityDTO;

@FeignClient(name = "gps")
public interface GpsClient {

    @RequestMapping
    public void crearEntidad(EntityDTO entity);

    @DeleteMapping(value = "/{matricula}")
    public void eliminarEntidad(@PathVariable("matricula") String matricula);

    @GetMapping(value = "/{matricula}")
    EntityDTO obtenerEntidad(@PathVariable("matricula")String matricula);

    @GetMapping(value="{latitud}/{longitud}/{radio}")
    List<EntityDTO> filtrarporUbicacion(@PathVariable("latitud")String latitud, @PathVariable("longitud")String longitud, @PathVariable("radio")String radio);
    

}
