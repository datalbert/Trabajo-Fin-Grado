package trabajotfg.gps.services;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import trabajotfg.gps.dto.EntityDTO;

@Service
@AllArgsConstructor
public class GpsServiceImpl  implements GpsService {


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

}
