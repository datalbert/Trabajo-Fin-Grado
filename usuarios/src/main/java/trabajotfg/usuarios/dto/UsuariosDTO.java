package trabajotfg.usuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
public class UsuariosDTO {

    private String nombre;

    private String apellidos;

    private String password;

    private String email;

    private String direccion;

    private int codigopostal;

    private int numerotelefono;

    private String dni;

    private String rol;





}
