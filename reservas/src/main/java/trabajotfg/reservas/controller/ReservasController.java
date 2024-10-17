package trabajotfg.reservas.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import trabajotfg.reservas.constant.ReservasConstant;
import trabajotfg.reservas.dto.ReservasDto;
import trabajotfg.reservas.dto.ReservasMsgDto;
import trabajotfg.reservas.dto.ResponseDTO;
import trabajotfg.reservas.service.ReservasService;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;


@RestController
@RequestMapping
@Tag(name = "Reservas", description = "API para la Gestión de las reservas de los vehiculos en la Apliación")
@AllArgsConstructor
public class ReservasController {

    private ReservasService reservasService;



    @Autowired
	private StreamBridge streamBridge;

    @Operation(summary = "Registrar una reserva", description = "Registro de una reserva en la aplicación. Interviene Imventario para modificar el estado del vehiculo y Pago para realizar el pago de la reserva.")
    @Parameter(description = "Objeto JSON de la reserva a registrar")
    @ApiResponse(responseCode = "201", description = "Reserva registrada correctamente")

    @PostMapping
    public ResponseEntity<ResponseDTO> crearReserva(@RequestBody ReservasDto reserva){
        
        reservasService.crearReserva(reserva);

        //enviar a la cola de mensajes
        sendEmail(reserva);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(ReservasConstant.HTTP_STATUS_CREATED, ReservasConstant.HTTP_STATUS_CREATED_MESSAGE));
    
    }

    @Operation(summary = "Eliminar una reserva", description = "Eliminación de una reserva en la aplicación. Interviene Imventario para modificar el estado del vehiculo y Pago para realizar el reembolso de la reserva.")
    @Parameter(description = "Identificador de la reserva a eliminar", example = "1")
    @ApiResponse(responseCode = "200", description = "Reserva eliminada correctamente")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> eliminarReserva(@PathVariable int id){
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(ReservasConstant.HTTP_STATUS_OK, reservasService.eliminarReserva(id)));
    }

    @Operation(summary = "Obtener todas las reservas", description = "Obtención de todas las reservas registradas en la aplicación.")
    @ApiResponse(responseCode = "200", description = "Reservas obtenidas correctamente")
    @GetMapping("/todas")
    public ResponseEntity<List<ReservasDto>> obtenerReservas(){
        List<ReservasDto> reservas=reservasService.obtenerReservas();
        return ResponseEntity.status(HttpStatus.OK).body(reservas);
    }

    @Operation(summary = "Obtener todas las reservas por emailcliente", description = "Obtención de todas las reservas registradas en la aplicación por emailcliente.")
    @Parameter(description = "Email del cliente", example = "usuario@email.com")
    @ApiResponse(responseCode = "200", description = "Reservas obtenidas correctamente")
    @GetMapping("/")
    public ResponseEntity<List<ReservasDto>> obtenerReservasPorEmail(@RequestParam("email") String emailcliente){
        List<ReservasDto> reservas=reservasService.obtenerReservasPorEmail(emailcliente);
        return ResponseEntity.status(HttpStatus.OK).body(reservas);
    }

    @Operation(summary = "Obtener una reserva por fecha", description = "Este método devuelve las reservas completadas  la fecha actual.")
    @Parameter(description = "Fecha de la reserva", example = "2021-06-01")
    @ApiResponse(responseCode = "200", description = "Reservas obtenidas correctamente")
    @GetMapping("/completadas")
    public ResponseEntity<List<ReservasDto>> obtenerReservasCompletadasFecha(@RequestParam("fecha") String fecha, @RequestParam("email") String email){
        List<ReservasDto> reservas=reservasService.obtenerReservasCompletadas(fecha,email);
        return ResponseEntity.status(HttpStatus.OK).body(reservas);
    }

    @Operation(summary = "Obtener una reserva por fecha", description = "Este método devuelve las reservas activas a la fecha actual.")
    @Parameter(description = "Fecha de la reserva", example = "2021-06-01")
    @ApiResponse(responseCode = "200", description = "Reservas obtenidas correctamente")
    @GetMapping("/activas")
    public ResponseEntity<List<ReservasDto>> obtenerReservasActivasFecha(@RequestParam("fecha") String fecha, @RequestParam("email") String email){
        List<ReservasDto> reservas=reservasService.obtenerReservasActivas(fecha,email);
        return ResponseEntity.status(HttpStatus.OK).body(reservas);
    }

    @Operation(summary = "Actualizar el estado de una reserva", description = "Este método actualiza el estado de una reserva a Finalizado cuando concluye la misma.")
    @Parameter(description = "Identificador de la reserva", example = "1")
    @ApiResponse(responseCode = "200", description = "Estado de la reserva actualizado correctamente")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> actualizarEstadoReserva(@PathVariable("id") int id){
        
        reservasService.actualizarEstado(id,"FINALIZADO");
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(ReservasConstant.HTTP_STATUS_OK, ReservasConstant.HTTP_STATUS_OK_MESSAGE));
    }



    public void sendEmail(ReservasDto datos_resersa){

        String fecha_fin;
        //vamos a enviar a la cola la información de la reserva
        if (datos_resersa.getFechaFin().equals("null")) {
            fecha_fin=datos_resersa.getFechaInicio();
        }
        else{
            fecha_fin=datos_resersa.getFechaFin();
        }
        ReservasMsgDto reservamgs= new ReservasMsgDto(datos_resersa.getEmailcliente(),
            datos_resersa.getNombrecliente(), 
            datos_resersa.getMarca(), datos_resersa.getMatricula(), datos_resersa.getFechaInicio(),
            fecha_fin, datos_resersa.getHoraInicio(), datos_resersa.getHoraFin(), datos_resersa.getDireccion());
        
        //enviar a la cola de mensajes
        
        var result =streamBridge.send("sendEmail-out-0", reservamgs);

        System.out.println("Mensaje enviado a la cola de mensajes: "+result);
    }




    

   

    

    
    
    





}
