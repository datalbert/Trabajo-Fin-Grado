package trabajotfg.usuarios.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import trabajotfg.usuarios.constant.UsuariosConstant;
import trabajotfg.usuarios.dto.ResponseDTO;
import trabajotfg.usuarios.dto.ResponseUserDTO;
import trabajotfg.usuarios.dto.UsuariosDTO;
import trabajotfg.usuarios.service.UsuarioService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("usuario")
@AllArgsConstructor
public class UsuariosController {

    private  UsuarioService usuarioService;


    @PostMapping("/crear-usuarios")
    public ResponseEntity<ResponseDTO> crearUsuario(@RequestBody UsuariosDTO usuarionuevo) {
        // TODO Auto-generated method stub
        usuarioService.crearUsuario(usuarionuevo);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(UsuariosConstant.HTTP_STATUS_CREATED, UsuariosConstant.HTTP_STATUS_CREATED_MESSAGE));
    }

    @GetMapping("/buscar-usuarios")
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





}
