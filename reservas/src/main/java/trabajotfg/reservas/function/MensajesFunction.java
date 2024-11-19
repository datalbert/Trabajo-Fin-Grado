package trabajotfg.reservas.function;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import trabajotfg.reservas.dto.TransaccionMsgDto;
import trabajotfg.reservas.entity.Reservas;
import trabajotfg.reservas.service.ReservasService;

@Configuration
public class MensajesFunction {

    private ReservasService reservasService;

    public MensajesFunction(ReservasService reservasService) {
        this.reservasService = reservasService;
    }

    
    @Bean
    public Consumer<TransaccionMsgDto> sendPagoConfirmation() {

        return transaccion -> {
            try {
                
                //evaluar el estado 
                //Si estado es completado se actualiza el id de la reserva a completada
                //Si estado es fallido se elimina la reserva
                //Si el estado es Cancelado se elimina la reserva
                if (transaccion.estado().equals("Aceptado")){
                    // actualizamos la reserva  Completada
                    reservasService.actualizarEstado(transaccion.id_reserva(), "Completado");
                    //actualizamos el pago a completado
                    reservasService.actualizarEstadoPago(transaccion.id_reserva(), "Completado");
                    
                }
                if(transaccion.estado().equals("Rechazado") || transaccion.estado().equals("Cancelado")){
                    reservasService.eliminarReserva(transaccion.id_reserva());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        
    }

        


}
