package trabajotfg.inventario.repository;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import trabajotfg.inventario.entity.Coches;

public interface CochesRepository extends JpaRepository<Coches, Integer>, JpaSpecificationExecutor<Coches> {
    // Remove the conflicting method declaration

   List<Coches> findAll();

   List <Coches> findAll(Specification<Coches> specification);

   Coches findByMatricula(String matricula);

   List<Coches> findByEmailpropietario(String emailpropietario);

    @Query("SELECT c FROM Coches c WHERE c.idcoche NOT IN :ids")
    List<Coches> findAllByIdcocheNotIn(@Param("ids") List<Integer> ids);

   
}
    

