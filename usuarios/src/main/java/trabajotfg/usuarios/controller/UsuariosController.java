package trabajotfg.usuarios.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import trabajotfg.usuarios.constant.UsuariosConstant;
import trabajotfg.usuarios.dto.ResponseDTO;
import trabajotfg.usuarios.dto.ResponseUserDTO;
import trabajotfg.usuarios.dto.UsuariosDTO;
import trabajotfg.usuarios.service.UsuarioService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping
@AllArgsConstructor
public class UsuariosController {

    private  UsuarioService usuarioService;


    @PostMapping
    public ResponseEntity<ResponseDTO> crearUsuario(@RequestBody UsuariosDTO usuarionuevo) {
        // TODO Auto-generated method stub
        usuarioService.crearUsuario(usuarionuevo);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(UsuariosConstant.HTTP_STATUS_CREATED, UsuariosConstant.HTTP_STATUS_CREATED_MESSAGE));
    }

    @GetMapping("/{id}")
    // no implementado hay que mirar como hacerlo con auth2.0
    public ResponseEntity<ResponseUserDTO> buscarUsuario(@RequestParam String email, String password) {
        // TODO Auto-generated method stub
        UsuariosDTO usuario_respuesta=usuarioService.buscarUsuario(email, password);


        if(usuario_respuesta == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseUserDTO(UsuariosConstant.HTTP_STATUS_NOT_FOUND,UsuariosConstant.HTTP_USER_NOT_EXIST,null));
        }
        else{
            if(usuario_respuesta.getNombre() == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseUserDTO(UsuariosConstant.HTTP_STATUS_NOT_FOUND,UsuariosConstant.HTTP_USER_INCORRECT,null));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseUserDTO(UsuariosConstant.HTTP_STATUS_OK,UsuariosConstant.HTTP_STATUS_OK,usuario_respuesta));
        }

    }

    @Operation(summary = "Obtener todos los usuarios", description = "Obtención de todos los usuarios registrados en la aplicación.")
    @ApiResponse(responseCode = "200", description = "Usuarios obtenidos correctamente")
    @Parameter(description = "Objeto JSON de la reserva a registrar")
    @GetMapping
    public ResponseEntity<List<UsuariosDTO>> obtenerUsuarios() {

        List<UsuariosDTO> usuarios=usuarioService.obtenerUsuarios();


        return ResponseEntity.status(HttpStatus.OK).body(usuarios);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> eliminarUsuario(@PathVariable int id) {

        usuarioService.eliminarUsuario(id);
        // TODO Auto-generated method stub
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(UsuariosConstant.HTTP_STATUS_OK, UsuariosConstant.HTTP_USER_DELETE));
    }





}
