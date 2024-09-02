package trabajotfg.gps.dto;
   
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data @AllArgsConstructor
public class ResponseDTO {

    private String statusCode;

    private String statusMsg;

    private List<ComunidadesDTO> comunidades;
}
 



    