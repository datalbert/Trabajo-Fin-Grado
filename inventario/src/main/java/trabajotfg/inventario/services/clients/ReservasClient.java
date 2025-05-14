package trabajotfg.inventario.services.clients;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import jakarta.ws.rs.client.Entity;
import trabajotfg.inventario.dto.EntityDTO;

@FeignClient(name = "reservas")
public interface ReservasClient {

    
    @RequestMapping(method=RequestMethod.GET, value = "/reservas/fecha")
    public List<Integer> obtenerReservasPorFecha(@RequestParam("fecha_inicio") String fecha_inicio ,@RequestParam("fecha_fin") String fecha_fin);


}
