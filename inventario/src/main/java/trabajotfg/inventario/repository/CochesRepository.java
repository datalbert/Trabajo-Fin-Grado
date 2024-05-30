package trabajotfg.inventario.repository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import trabajotfg.inventario.entity.Coches;

public interface CochesRepository extends JpaRepository<Coches, Integer>, JpaSpecificationExecutor<Coches> {
    // Remove the conflicting method declaration

    List<Coches> findAll();

    List <Coches> findAll(Specification<Coches> specification);

    
}
