package trabajotfg.reservas.service;

import org.springframework.stereotype.Service;

import trabajotfg.reservas.dto.ReservasDto;

import java.util.List;


public interface ReservasService {

    void crearReserva(ReservasDto reserva);

    String eliminarReserva(int id);

    void actualizarEstado(int idReserva, String estado);

    List<ReservasDto> obtenerReservas();

    List<ReservasDto> obtenerReservasCompletadas(String fecha,String email);

    List<ReservasDto> obtenerReservasActivas(String fecha,String email);

    List<ReservasDto> obtenerReservasPorEmail(String email);

}
