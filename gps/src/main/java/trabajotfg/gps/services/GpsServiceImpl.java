package trabajotfg.gps.services;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import io.swagger.v3.oas.models.security.SecurityScheme.In;
import lombok.AllArgsConstructor;
import trabajotfg.gps.dto.EntityDTO;
import trabajotfg.gps.dto.InventarioDto;
import trabajotfg.gps.dto.ResponseDTO;
import trabajotfg.gps.services.clients.InventarioClient;
import trabajotfg.gps.services.clients.ReservasClient;

@Service
@AllArgsConstructor
public class GpsServiceImpl  implements GpsService {


    //clientes de los servicios
    private InventarioClient inventarioClient;
    private ReservasClient reservasClient;

    public JSONObject createJson(EntityDTO entity) {

        JSONObject obj = new JSONObject();
        obj.put("type", "Car");
        obj.put("id", entity.getMatricula());

        JSONObject position = new JSONObject();
        position.put("type", "geo:point");
        position.put("value", entity.getLatitud() + "," + entity.getLongitud());

        obj.put("position", position);
        return obj;
    }

    @Override
    public JSONObject createJsonUptade(EntityDTO entity) {

        JSONObject obj = new JSONObject();
        // TODO Auto-generated method stub
        JSONObject position = new JSONObject();
        position.put("type", "geo:point");
        position.put("value", entity.getLatitud() + "," + entity.getLongitud());

        obj.put("position", position);

        return obj;
        
    }

    @Override
    public void finalizarReserva(String matricula) {
        // TODO Auto-generated method stub
        //recibimos la matricula y tenemos que finalizar la reserva para ello

        /*
         * 1. Buscar el identificador del vehiculo a traves de la matricula
         * 2. Una vez con el identificador del vehiculo buscar la reserva asociada
         * 3. Actualizar el estado de la reserva a finalizada
         * 4. Actualizar el estado del vehiculo a disponible
         */

         ResponseEntity<InventarioDto> entidad_coche=inventarioClient.obtenerCochePorMatricula(matricula);

          //1º Inventario para actualizar el estado del coche a DISPONIBLE
        ResponseEntity<ResponseDTO> inventarioestado=inventarioClient.actualizarEstado(
            entidad_coche.getBody().getId(),
            "DISPONIBLE");
        
        // obtenemos el identifivado de la reserva y cuyo estado es ACTIVA
        int identificador_reserva=reservasClient.obtenerReservaPorIdVehiclo(entidad_coche.getBody().getId());
        
        //2º Reservas para actualizar la reserva a finalizada
        ResponseEntity<ResponseDTO> reservasestado=reservasClient.actualizarEstadoReserva(identificador_reserva,"FINALIZADA");
    }

}
