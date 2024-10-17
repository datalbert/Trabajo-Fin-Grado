package trabajotfg.gps.dto;

import lombok.Data;


@Data
public class FinalizarDTO {
    
    private int idReserva;
    private int idVehiculo;
    private String matricula;
    private String latitud;
    private String longitud;
}
/*
 * Esta clase recibe todos los parametros necesarios para finalizar la reserva 
 * y actulizar el correcpondiente estado en los distintos servicios implicados
 * Reservas y Inventario
 */