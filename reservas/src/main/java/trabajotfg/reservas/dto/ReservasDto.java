package trabajotfg.reservas.dto;

import lombok.Data;

@Data
public class ReservasDto {

    private int idreserva;

    private int idvehiculo;

    private String matricula;

    private String marca;

    private String emailcliente;

    private String nombrecliente;

    private String fechaInicio;

    private String fechaFin;

    private String horaInicio;

    private String horaFin;

    private String estado;

    private String direccion;

    private Double precio;



}
