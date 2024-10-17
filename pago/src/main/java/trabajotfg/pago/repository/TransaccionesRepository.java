package trabajotfg.pago.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import trabajotfg.pago.entity.Transacciones;

public interface TransaccionesRepository  extends JpaRepository<Transacciones, String>{

}
