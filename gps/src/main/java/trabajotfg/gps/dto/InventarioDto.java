package trabajotfg.gps.dto;

import lombok.Data;

@Data
public class InventarioDto {

    private int id;

    private String marca;
    
    private String modelo;
    
    private String matricula;
    
    private String urlfotos;
    
    private String combustible;
    
    private String transmision;
    
    private int numAsientos;
    
    private String emailpropietario;

    private String latitud;

    private String longitud;


}
