package trabajotfg.usuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import trabajotfg.usuarios.entity.Usuarios;

@Repository
public interface UsuarioRepository  extends JpaRepository<Usuarios, Integer>{

    Usuarios findByEmail(String email);

}
