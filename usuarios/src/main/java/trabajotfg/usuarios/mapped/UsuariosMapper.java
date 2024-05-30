package trabajotfg.usuarios.mapped;

import trabajotfg.usuarios.dto.UsuariosDTO;
import trabajotfg.usuarios.entity.Usuarios;
import trabajotfg.usuarios.entity.UsuariosRol;

public class UsuariosMapper {

    public static UsuariosDTO mapToUsuariosDTO(Usuarios usuario, UsuariosDTO usuarioDTO) {
        usuarioDTO.setNombre(usuario.getNombre());
        usuarioDTO.setApellidos(usuario.getApellidos());
        usuarioDTO.setPassword(usuario.getPassword());
        usuarioDTO.setEmail(usuario.getEmail());
        usuarioDTO.setDireccion(usuario.getDireccion());
        usuarioDTO.setCodigopostal(usuario.getCodigopostal());
        usuarioDTO.setNumerotelefono(usuario.getNumerotelefono());
        usuarioDTO.setDni(usuario.getDni());
        
        return usuarioDTO;
    }

    public static Usuarios mapToUsuarios(UsuariosDTO usuarioDTO, Usuarios usuario) {
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setApellidos(usuarioDTO.getApellidos());
        usuario.setPassword(usuarioDTO.getPassword());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setDireccion(usuarioDTO.getDireccion());
        usuario.setCodigopostal(usuarioDTO.getCodigopostal());
        usuario.setNumerotelefono(usuarioDTO.getNumerotelefono());
        usuario.setDni(usuarioDTO.getDni());
        return usuario;
    }

}
