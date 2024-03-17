package trabajotfg.usuarios.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import trabajotfg.usuarios.constant.UsuariosConstant;
import trabajotfg.usuarios.dto.ResponseDTO;
import trabajotfg.usuarios.dto.UsuariosDTO;
import trabajotfg.usuarios.entity.Usuarios;
import trabajotfg.usuarios.service.UsuarioServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



@RestController
@RequestMapping("usuario")
@AllArgsConstructor
public class UsuariosController {

    private final UsuarioServiceImpl usuarioService;

    
   


    @PostMapping("/crear-usuarios")
    public ResponseEntity<ResponseDTO> crearUsuario(@RequestBody UsuariosDTO usuarionuevo) {
        // TODO Auto-generated method stub
        usuarioService.crearUsuario(usuarionuevo);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(UsuariosConstant.HTTP_STATUS_CREATED, UsuariosConstant.HTTP_STATUS_CREATED_MESSAGE));
    }






}
