package trabajotfg.usuarios.service;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import trabajotfg.usuarios.dto.UsuariosDTO;
import trabajotfg.usuarios.entity.Rol;
import trabajotfg.usuarios.entity.Usuarios;
import trabajotfg.usuarios.entity.UsuariosRol;
import trabajotfg.usuarios.mapped.UsuariosMapper;
import trabajotfg.usuarios.repository.Rolrepository;
import trabajotfg.usuarios.repository.UsuarioRepository;
import trabajotfg.usuarios.repository.UsuarioRolRepository;

@Service
@AllArgsConstructor
public class UsuarioServiceImpl implements UsuarioService{

    private final UsuarioRepository usuarioRepository;

    private final Rolrepository rolrepository;

    private final UsuarioRolRepository usuarioRolRepository;


    @Override
    public void crearUsuario(UsuariosDTO usuarionuevo) {
        // TODO Auto-generated method stub
        //Creamos la instancia de UsuarioRol
        UsuariosRol usuarioRol = new UsuariosRol();

        
        Rol rol = rolrepository.findByTipo(usuarionuevo.getRol());
        
        usuarioRol.setUsuario(usuarioRepository.save(UsuariosMapper.mapToUsuarios(usuarionuevo, new Usuarios())));
        usuarioRol.setRol(rol);

        usuarioRolRepository.save(usuarioRol);

    }


    @Override
    public UsuariosDTO buscarUsuario(String email, String password) {
        // TODO Auto-generated method stub
        Usuarios usuarios = usuarioRepository.findByEmail(email);

        UsuariosRol usuarioRol = usuarioRolRepository.findByUsuarioIdWithRol(usuarios.getIdUsuarios());



        if(usuarios == null) {
            //devolmebo null si el usuario no existe
            return null;
        }
        else{
            if(usuarios.getPassword().equals(password)){
                UsuariosDTO usuarioencontrado=UsuariosMapper.mapToUsuariosDTO(usuarios, new UsuariosDTO());
                usuarioencontrado.setRol(usuarioRol.getRol().getTipo());
                return usuarioencontrado;
            }else{
                //devolvemo un objeto vacio si el usuario existe pero la contraseña es incorrecta
                return new UsuariosDTO();
                
        }
    }

    }


    

    

}
