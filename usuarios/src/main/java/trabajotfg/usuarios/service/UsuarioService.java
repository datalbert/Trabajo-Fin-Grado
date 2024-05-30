package trabajotfg.usuarios.service;

import trabajotfg.usuarios.dto.UsuariosDTO;
import trabajotfg.usuarios.entity.Usuarios;
import trabajotfg.usuarios.entity.UsuariosRol;
import trabajotfg.usuarios.repository.UsuarioRepository;

public interface UsuarioService  {

    void crearUsuario(UsuariosDTO usuarionuevo);

    UsuariosDTO buscarUsuario(String email, String password);

    

    

}
