package trabajotfg.inventario.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import trabajotfg.inventario.entity.Coches;
import trabajotfg.inventario.entity.Estado;

public interface EstadoRepository  extends JpaRepository<Estado, Integer>{

    @Query("SELECT e.coche FROM Estado e WHERE e.estado = :estado")
    List<Coches> findAllByEstado(String estado);

    Estado findByCoche(Coches coche);


    

}
