package trabajotfg.inventario.mapped;


import trabajotfg.inventario.dto.InventarioDto;
import trabajotfg.inventario.entity.Coches;

public class InventarioMapped {

    
    public static Coches maptoEntity(InventarioDto dto, Coches entity) {
        
        entity.setMarca(dto.getMarca());
        entity.setModelo(dto.getModelo());
        entity.setMatricula(dto.getMatricula());
        //entity.setFotos(dto.getFotos());
        entity.setCombustible(dto.getCombustible());
        entity.setTransmision(dto.getTransmision());
        entity.setNumAsientos(dto.getNumAsientos());
        entity.setIdPropietario(dto.getIdPropietario());
        return entity;
    }

    public static InventarioDto maptoDto(Coches entity, InventarioDto dto) {

        dto.setId(entity.getIdcoche());
        dto.setMarca(entity.getMarca());
        dto.setModelo(entity.getModelo());
        dto.setMatricula(entity.getMatricula());
        //dto.setFotos(Base64.getEncoder().encodeToString(entity.getFotos()));
        dto.setCombustible(entity.getCombustible());
        dto.setTransmision(entity.getTransmision());
        dto.setNumAsientos(entity.getNumAsientos());
        dto.setIdPropietario(entity.getIdPropietario());
        return dto;
    }

}
