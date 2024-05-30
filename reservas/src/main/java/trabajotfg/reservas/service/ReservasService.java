package trabajotfg.reservas.service;

import org.springframework.stereotype.Service;

import trabajotfg.reservas.dto.ReservasDto;


public interface ReservasService {

    void crearReserva(ReservasDto reserva);

    String eliminarReserva(int id);

}
