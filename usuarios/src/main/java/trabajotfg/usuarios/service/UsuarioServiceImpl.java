package trabajotfg.usuarios.service;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import trabajotfg.usuarios.dto.UsuariosDTO;
import trabajotfg.usuarios.entity.Usuarios;
import trabajotfg.usuarios.mapped.UsuariosMapper;
import trabajotfg.usuarios.repository.UsuarioRepository;

@Service
@AllArgsConstructor
public class UsuarioServiceImpl implements UsuarioService{

    private final UsuarioRepository usuarioRepository;


    @Override
    public void crearUsuario(UsuariosDTO usuarionuevo) {
        // TODO Auto-generated method stub
        //llamar al repositorio para persisitir el usuario en la base de datos


        usuarioRepository.save(UsuariosMapper.mapToUsuarios(usuarionuevo, new Usuarios()));

    }

    

}
