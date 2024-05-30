
package trabajotfg.reservas.mapped;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import trabajotfg.reservas.dto.ReservasDto;
import trabajotfg.reservas.entity.Reservas;


public class ReservasMapped {

    public static Reservas convertToEntity(ReservasDto dto, Reservas entity) {
        
        entity.setId_usuario(dto.getIdcliente());
        entity.setId_vehiculo(dto.getIdvehiculo());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date fechaInicio = (Date) dateFormat.parse(dto.getFechaInicio());
            entity.setFecha_inicio(fechaInicio);
        } catch (ParseException e) {
            // Handle the exception or throw it
        }
        try {
            Date fechaFin = (Date) dateFormat.parse(dto.getFechaFin());
            entity.setFecha_fin(fechaFin);
        } catch (ParseException e) {
            // Handle the exception or throw it
        }
        entity.setEstado(dto.getEstado());
        return entity;
    }

    public static ReservasDto convertToDTO(Reservas entity, ReservasDto dto) {
        
        dto.setIdcliente(entity.getId_usuario());
        dto.setIdvehiculo(entity.getId_vehiculo());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dto.setFechaInicio(dateFormat.format(entity.getFecha_inicio()));
        dto.setFechaFin(dateFormat.format(entity.getFecha_fin()));
        dto.setEstado(entity.getEstado());
        return dto;
        
    }
}
