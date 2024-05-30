package trabajotfg.reservas.service;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import trabajotfg.reservas.constant.ReservasConstant;
import trabajotfg.reservas.dto.ReservasDto;
import trabajotfg.reservas.dto.ResponseDTO;
import trabajotfg.reservas.entity.Reservas;
import trabajotfg.reservas.mapped.ReservasMapped;
import trabajotfg.reservas.repository.PagosRepository;
import trabajotfg.reservas.repository.ReservasRepository;
import trabajotfg.reservas.service.clients.InventarioClient;

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

    

}
