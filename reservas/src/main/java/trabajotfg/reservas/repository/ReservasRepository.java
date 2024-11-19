package trabajotfg.reservas.repository;

import java.util.List;
import java.util.Date;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import trabajotfg.reservas.entity.Reservas;

public interface ReservasRepository  extends JpaRepository<Reservas, Integer>{

    @Query("SELECT r FROM Reservas r WHERE r.fecha_fin < :fecha AND r.emailcliente=:email")
    List<Reservas> findByFechaCompletadas(Date fecha,String email);

    @Query("SELECT r FROM Reservas r WHERE r.fecha_fin >= :fecha AND r.emailcliente=:email AND r.estado='Completado'")
    List<Reservas> findByFechaActivas(Date fecha,String email);

    List<Reservas> findByEmailcliente(String email);

    Reservas findByIdVehiculo(int idVehiculo);

    @Query(value = "SELECT MONTH(fecha_inicio) AS mes, COUNT(id_vehiculo) AS num_vehiculos " +
    "FROM `Reservas` " +
    "WHERE emailcliente = :email " +
    "GROUP BY MONTH(fecha_inicio) " +
    "ORDER BY MONTH(fecha_inicio)", nativeQuery = true)
List<Object[]> findVehiclesRentedByUserPerMonth(@Param("email") String email);

    

}
