package trabajotfg.reservas.service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import trabajotfg.reservas.dto.OrderDto;

@FeignClient(name = "gps")
public interface GpsClient {

    
    //este método para crear la subscripción de un vehiculo recibe la matricula del vehiculo
    @RequestMapping(method = RequestMethod.POST, value = "/subscripcion/{matricula}")
    public String crearSubscripcion(@PathVariable("matricula") String matricula);

    

}
