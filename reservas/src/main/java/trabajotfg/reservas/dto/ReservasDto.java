package trabajotfg.reservas.dto;

import lombok.Data;

@Data
public class ReservasDto {

    private int idreserva;

    private int idvehiculo;

    private int idcliente;

    private String fechaInicio;

    private String fechaFin;

    private String estado;



}
