package trabajotfg.inventario.dto;

import lombok.Data;

@Data
public class InventarioDto {

    private int id;

    private String marca;
    
    private String modelo;
    
    private String matricula;
    
    private String fotos;
    
    private String combustible;
    
    private String transmision;
    
    private int numAsientos;
    
    private int idPropietario;


}
