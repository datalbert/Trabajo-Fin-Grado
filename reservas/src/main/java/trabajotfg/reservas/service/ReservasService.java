package trabajotfg.reservas.service;

import org.springframework.stereotype.Service;

import trabajotfg.reservas.dto.ReservasDto;
import trabajotfg.reservas.entity.Reservas;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Map;


public interface ReservasService {

    String crearReserva(ReservasDto reserva);

    String cancelarReserva(int id);

    void eliminarReserva(int idReserva);

    void actualizarEstado(int idReserva, String estado);

    void actualizarEstadoPago(int idReserva, String estado);

    int obtenerReservaPorIdVehiclo(int idVehiculo);

    List<ReservasDto> obtenerReservas();

    List<ReservasDto> obtenerReservasCompletadas(String fecha,String email);

    List<ReservasDto> obtenerReservasActivas(String fecha,String email);

    List<ReservasDto> obtenerReservasPorEmail(String email);

    List<Integer> obtenerReservasPorFecha(String fecha_inicio,String fecha_fin);

    Reservas obtenerReservaPorIdVehiculo(int id);

    boolean comprobarFechas(Date fecha_inicio, Time hora_inicio);

    Map<Integer,Integer> obtenerEstadisticasPorCliente(String email);

}
