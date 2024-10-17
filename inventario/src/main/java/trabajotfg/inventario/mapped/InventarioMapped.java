package trabajotfg.inventario.mapped;


import trabajotfg.inventario.dto.InventarioDto;
import trabajotfg.inventario.entity.Coches;

public class InventarioMapped {

    
    public static Coches maptoEntity(InventarioDto dto, Coches entity) {
        
        entity.setMarca(dto.getMarca());
        entity.setModelo(dto.getModelo());
        entity.setMatricula(dto.getMatricula());
        entity.setUrlfotos(dto.getUrlfotos());
        entity.setCombustible(dto.getCombustible());
        entity.setTransmision(dto.getTransmision());
        entity.setNumAsientos(dto.getNumAsientos());
        entity.setEmailpropietario(dto.getEmailpropietario());
        return entity;
    }

    public static InventarioDto maptoDto(Coches entity, InventarioDto dto) {

        dto.setId(entity.getIdcoche());
        dto.setMarca(entity.getMarca());
        dto.setModelo(entity.getModelo());
        dto.setMatricula(entity.getMatricula());
        dto.setUrlfotos(entity.getUrlfotos());
        dto.setCombustible(entity.getCombustible());
        dto.setTransmision(entity.getTransmision());
        dto.setNumAsientos(entity.getNumAsientos());
        dto.setEmailpropietario(entity.getEmailpropietario());
        return dto;
    }

}
