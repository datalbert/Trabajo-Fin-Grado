package trabajotfg.reservas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import trabajotfg.reservas.constant.ReservasConstant;
import trabajotfg.reservas.dto.InventarioDto;
import trabajotfg.reservas.dto.ReservasDto;
import trabajotfg.reservas.dto.ResponseDTO;
import trabajotfg.reservas.entity.Reservas;
import trabajotfg.reservas.mapped.ReservasMapped;
import trabajotfg.reservas.repository.PagosRepository;
import trabajotfg.reservas.repository.ReservasRepository;
import trabajotfg.reservas.service.clients.InventarioClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Service
@AllArgsConstructor
public class ReservasServiceImpl  implements ReservasService{

    private ReservasRepository reservasRepository;
    private PagosRepository estadoRepository;
    private InventarioClient inventarioClient;


    @Override
    public void crearReserva(ReservasDto reserva) {
        // TODO Auto-generated method stub
        //primero llamar al inventario para modificar el estado
        ResponseEntity<ResponseDTO> inventarioestado=inventarioClient.actualizarEstado(reserva.getIdvehiculo(), "RESERVADO");
        
        reservasRepository.save(ReservasMapped.convertToEntity(reserva, new Reservas()));
    }


    @Override
    public String eliminarReserva(int id) {
        // verificar si la cancelacion antes de las 24 horas de el inicio de la reserva
        Optional<Reservas> reserva=reservasRepository.findById(id);
        if (System.currentTimeMillis()-reserva.get().getFecha_inicio().getTime()<=86400000) {
            // si es asi se devuelve el dinero
            
            // se cambia el estado del vehiculo a disponible
            // se elimina la reserva
            ResponseEntity<ResponseDTO> inventarioestado=inventarioClient.actualizarEstado(reserva.get().getId_vehiculo(), "DISPONIBLE");
            reservasRepository.deleteById(id);
            return ReservasConstant.RESERVA_CANCELADA_REMBOLSO_50;
        }
        else{
            // si es despues de 24 horas se cobra el 50% del total

            // se cambia el estado del vehiculo a disponible
            // se elimina la reserva
            ResponseEntity<ResponseDTO> inventarioestado=inventarioClient.actualizarEstado(reserva.get().getId_vehiculo(), "DISPONIBLE");
            reservasRepository.deleteById(id);
            return ReservasConstant.RESERVA_CANCELADA_REMBOLSO_100;
        }
        
    }

    @Override
    public List<ReservasDto> obtenerReservas() {
        // TODO Auto-generated method stub
        List<ReservasDto> reservasDTO= new ArrayList<>();
        List<Reservas> reservas= reservasRepository.findAll();
        for (Reservas reserva : reservas) {
            reservasDTO.add(ReservasMapped.convertToDTO(reserva, new ReservasDto()));
        }

        return reservasDTO;

    }

    @Override
    public List<ReservasDto> obtenerReservasCompletadas(String fecha,String email) {
        List<ReservasDto> reservasDTO = new ArrayList<>();
        try {
            Date nuevaFecha = new SimpleDateFormat("yyyy-MM-dd").parse(fecha);
            List<Reservas> reservas = reservasRepository.findByFechaCompletadas(nuevaFecha,email);
            for (Reservas reserva : reservas) {
                reservasDTO.add(ReservasMapped.convertToDTO(reserva, new ReservasDto()));
            }
        } catch (ParseException e) {
            e.printStackTrace();
            // Handle the exception, maybe log it or throw a custom exception
        }
        return reservasDTO;
    }


    @Override
    public List<ReservasDto> obtenerReservasActivas(String fecha,String email) {
        List<ReservasDto> reservasDTO = new ArrayList<>();
        try {
            Date nuevaFecha = new SimpleDateFormat("yyyy-MM-dd").parse(fecha);
            List<Reservas> reservas = reservasRepository.findByFechaActivas(nuevaFecha,email);
            for (Reservas reserva : reservas) {
                ReservasDto reservaDTO = ReservasMapped.convertToDTO(reserva, new ReservasDto());
                ResponseEntity<InventarioDto> coche_reservado=inventarioClient.obtenerVehiculo(reserva.getId_vehiculo());
                reservaDTO.setMatricula(coche_reservado.getBody().getMatricula());
                reservaDTO.setMarca(coche_reservado.getBody().getMarca());
                reservasDTO.add(reservaDTO);
                
            }
        } catch (ParseException e) {
            e.printStackTrace();
            // Handle the exception, maybe log it or throw a custom exception
        }
        return reservasDTO;
    }


    @Override
    public List<ReservasDto> obtenerReservasPorEmail(String email) {
        // TODO Auto-generated method stub
        List<ReservasDto> reservasDTO= new ArrayList<>();
        List<Reservas> reservas= reservasRepository.findByEmailcliente(email);
        for (Reservas reserva : reservas) {
            reservasDTO.add(ReservasMapped.convertToDTO(reserva, new ReservasDto()));
        }
        return reservasDTO;
    }


    @Override
    public void actualizarEstado(int idReserva, String estado) {
        // TODO Auto-generated method stub
        Reservas reserva = reservasRepository.findById(idReserva).orElseThrow(()-> new RuntimeException("Reserva no encontrada"));
        reserva.setEstado(estado);
        reservasRepository.save(reserva);
    }

    

}
