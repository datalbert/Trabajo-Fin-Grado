package trabajotfg.reservas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import trabajotfg.reservas.entity.Pagos;
import trabajotfg.reservas.entity.Reservas;

import java.util.Optional;

public interface PagosRepository  extends JpaRepository<Pagos, Integer>{

    Optional<Pagos> findByReserva(Reservas idReserva);

}
