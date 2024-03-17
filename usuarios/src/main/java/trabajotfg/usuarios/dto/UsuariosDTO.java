package trabajotfg.usuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
public class UsuariosDTO {

    private String nombre;

    private String apellidos;

    private int telefonoContacto;

    private String dni;

    private String email;


}
