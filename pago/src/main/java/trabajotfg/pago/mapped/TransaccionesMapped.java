package trabajotfg.pago.mapped;

import trabajotfg.pago.dto.OrderDto;
import trabajotfg.pago.entity.Transacciones;

public class TransaccionesMapped {

    public static Transacciones maptoEntity(OrderDto dto, Transacciones entity) {
        entity.setId_reserva(dto.getId_reserva());
        entity.setEmail(dto.getEmail());
        entity.setMonto(dto.getPrice());
        entity.setMoneda(dto.getCurrency());
        entity.setDescripcion_metodo_pago(dto.getDescription());
        
        return entity;
    }

    public static OrderDto maptoDto(Transacciones entity, OrderDto dto) {
        dto.setEmail(entity.getEmail());
        dto.setId_reserva(entity.getId_reserva());
        dto.setPrice(entity.getMonto());
        dto.setCurrency(entity.getMoneda());
        dto.setDescription(entity.getDescripcion_metodo_pago());
        return dto;
    }

}
