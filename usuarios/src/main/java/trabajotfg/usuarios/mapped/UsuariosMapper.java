package trabajotfg.usuarios.mapped;

import trabajotfg.usuarios.dto.UsuariosDTO;
import trabajotfg.usuarios.entity.Usuarios;

public class UsuariosMapper {

    public static UsuariosDTO mapToUsuariosDTO(Usuarios usuario, UsuariosDTO usuarioDTO) {
        usuarioDTO.setNombre(usuario.getNombre());
        usuarioDTO.setApellidos(usuario.getApellidos());
        usuarioDTO.setTelefonoContacto(usuario.getTelefono());
        usuarioDTO.setDni(usuario.getDni());
        usuarioDTO.setEmail(usuario.getEmail());
        return usuarioDTO;
    }

    public static Usuarios mapToUsuarios(UsuariosDTO usuarioDTO, Usuarios usuario) {
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setApellidos(usuarioDTO.getApellidos());
        usuario.setTelefono(usuarioDTO.getTelefonoContacto());
        usuario.setDni(usuarioDTO.getDni());
        usuario.setEmail(usuarioDTO.getEmail());
        return usuario;
    }

}
