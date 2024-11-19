package trabajotfg.gps.services;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import trabajotfg.gps.dto.EntityDTO;


public interface GpsService {

JSONObject createJson(EntityDTO entity);

JSONObject createJsonUptade(EntityDTO entity);

void finalizarReserva(String matricula);


}
