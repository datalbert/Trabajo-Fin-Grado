package trabajotfg.reservas.repository;

import java.util.List;
import java.util.Date;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import trabajotfg.reservas.entity.Reservas;

public interface ReservasRepository  extends JpaRepository<Reservas, Integer>{

    @Query("SELECT r FROM Reservas r WHERE r.fecha_fin < :fecha AND r.emailcliente=:email")
    List<Reservas> findByFechaCompletadas(Date fecha,String email);

    @Query("SELECT r FROM Reservas r WHERE r.fecha_fin >= :fecha AND r.emailcliente=:email")
    List<Reservas> findByFechaActivas(Date fecha,String email);

    List<Reservas> findByEmailcliente(String email);

    

}
