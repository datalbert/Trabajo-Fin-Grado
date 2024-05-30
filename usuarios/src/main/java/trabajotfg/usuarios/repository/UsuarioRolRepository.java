package trabajotfg.usuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import trabajotfg.usuarios.entity.UsuariosRol;

public interface UsuarioRolRepository extends JpaRepository<UsuariosRol,Integer>{

     @Query("SELECT ur FROM UsuariosRol ur JOIN FETCH ur.rol WHERE ur.usuario.id = :idUsuario")
    UsuariosRol findByUsuarioIdWithRol(@Param("idUsuario") int idUsuario);



}
