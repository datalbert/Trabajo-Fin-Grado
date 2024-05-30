package trabajotfg.usuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import trabajotfg.usuarios.entity.Rol;

public interface Rolrepository extends JpaRepository<Rol,Integer>{

    Rol findByTipo(String tipo);

}
