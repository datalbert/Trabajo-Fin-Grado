package trabajotfg.pago.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import trabajotfg.pago.entity.Transacciones;

public interface TransaccionesRepository  extends JpaRepository<Transacciones, String>{

    Transacciones findByIdReserva(Integer id);

    Optional<Transacciones> findByIdTransaccion(String idTransaccion);

}
