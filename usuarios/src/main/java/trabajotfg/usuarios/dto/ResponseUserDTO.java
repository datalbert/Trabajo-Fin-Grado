package trabajotfg.usuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class ResponseUserDTO {

    private String statusCode;

    private String statusMsg;

    private UsuariosDTO usuario;

}
