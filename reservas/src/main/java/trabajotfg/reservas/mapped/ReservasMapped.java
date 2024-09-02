
package trabajotfg.reservas.mapped;

import java.util.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import trabajotfg.reservas.dto.ReservasDto;
import trabajotfg.reservas.entity.Reservas;


public class ReservasMapped {

    public static Reservas convertToEntity(ReservasDto dto, Reservas entity) {
        
        entity.setId_usuario(dto.getIdcliente());
        entity.setId_vehiculo(dto.getIdvehiculo());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        
        try {
            Date fechaInicio = dateFormat.parse(dto.getFechaInicio());
            entity.setFecha_inicio(fechaInicio);
        } catch (ParseException e) {
            // Handle the exception or throw it
            e.printStackTrace();
        }
        
        try {
            Date fechaFin = dateFormat.parse(dto.getFechaFin());
            entity.setFecha_fin(fechaFin);
        } catch (ParseException e) {
            // Handle the exception or throw it
            e.printStackTrace();
        }
        
        try {
            Date horaInicio = timeFormat.parse(dto.getHoraInicio());
            entity.setHora_inicio(new java.sql.Time(horaInicio.getTime()));
        } catch (ParseException e) {
            // Handle the exception or throw it
            e.printStackTrace();
        }
        
        try {
            Date horaFin = timeFormat.parse(dto.getHoraFin());
            entity.setHora_final(new java.sql.Time(horaFin.getTime()));
        } catch (ParseException e) {
            // Handle the exception or throw it
            e.printStackTrace();
        }

        entity.setEstado(dto.getEstado());
        return entity;
    }

    public static ReservasDto convertToDTO(Reservas entity, ReservasDto dto) {
        
        dto.setIdreserva(entity.getIdreserva());
        dto.setIdcliente(entity.getId_usuario());
        dto.setIdvehiculo(entity.getId_vehiculo());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        dto.setFechaInicio(dateFormat.format(entity.getFecha_inicio()));
        dto.setFechaFin(dateFormat.format(entity.getFecha_fin()));
        dto.setHoraInicio(timeFormat.format(entity.getHora_inicio()));
        dto.setHoraFin(timeFormat.format(entity.getHora_final()));
        dto.setEstado(entity.getEstado());
        return dto;
        
    }
}
