package trabajotfg.inventario.services.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.ws.rs.client.Entity;
import trabajotfg.inventario.dto.EntityDTO;

@FeignClient(name = "gps")
public interface GpsClient {

    @RequestMapping
    public void crearEntidad(EntityDTO entity);

    @RequestMapping
    public void eliminarEntidad(int id);
    

}
