package trabajotfg.reservas.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.Path;
import lombok.AllArgsConstructor;
import trabajotfg.reservas.constant.ReservasConstant;
import trabajotfg.reservas.dto.ReservasDto;
import trabajotfg.reservas.dto.ReservasMsgDto;
import trabajotfg.reservas.dto.ResponseDTO;
import trabajotfg.reservas.entity.Reservas;
import trabajotfg.reservas.service.ReservasService;

import java.util.List;
import java.util.Map;

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
@OpenAPIDefinition(
    info = @Info(
        title = "RentaMov API",
        version = "1.0",
        description = "API general para la gestión de inventario, reservas, pagos y localización en el sistema de alquiler de vehículos.",
        contact = @Contact(
            name = "Alberto Ávila Fernández",
            email = "albertoaf0520@gmail.com",
            url = "https://www.rentamov.com"
        )
    )
)
@Tag(name = "Reservas", description = "API para la Gestión de las reservas de los vehiculos en la Apliación")
@AllArgsConstructor
public class ReservasController {

    private ReservasService reservasService;



    @Autowired
	private StreamBridge streamBridge;


            @Operation(
            summary = "Registrar una reserva",
            description = "Registra una nueva reserva en la aplicación, actualizando el estado del vehículo en Inventario y proporcionando una URL para realizar el pago de la reserva."
        )
        @Parameter(
            name = "reservaDto",
            description = "Objeto JSON con los datos de la reserva que se desea registrar",
            required = true
        )
        @ApiResponse(
            responseCode = "201",
            description = "Reserva registrada correctamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ResponseDTO.class)
            )
        )
    @PostMapping
    public ResponseEntity<ResponseDTO> crearReserva(@RequestBody ReservasDto reserva){
        
        String url_pago=reservasService.crearReserva(reserva);

        //enviar a la cola de mensajes
        sendEmail(reserva);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(ReservasConstant.HTTP_STATUS_CREATED, url_pago));
    
    }

    @Operation(
    summary = "Eliminar una reserva",
    description = "Elimina una reserva en la aplicación, lo cual actualiza el estado del vehículo en Inventario y, si aplica, realiza el reembolso de la reserva mediante el servicio de Pago."
)
@Parameter(
    name = "id",
    description = "Identificador de la reserva que se desea eliminar",
    example = "1",
    required = true
)
@ApiResponse(
    responseCode = "200",
    description = "Reserva eliminada correctamente",
    content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = ResponseDTO.class)
    )
)
    @DeleteMapping("/reservas/{id}")
    public ResponseEntity<ResponseDTO> cancelarReserva(@PathVariable int id){
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(ReservasConstant.HTTP_STATUS_OK, reservasService.cancelarReserva(id)));
    }

    @Operation(
    summary = "Obtener todas las reservas",
    description = "Obtención de todas las reservas registradas en la aplicación."
    )
    @ApiResponse(
    responseCode = "200",
    description = "Reservas obtenidas correctamente",
    content = @Content(
        mediaType = "application/json",
        array = @ArraySchema(schema = @Schema(implementation = ReservasDto.class))
        )
    )
    @GetMapping("/reservas/todas")
    public ResponseEntity<List<ReservasDto>> obtenerReservas(){
        List<ReservasDto> reservas=reservasService.obtenerReservas();
        return ResponseEntity.status(HttpStatus.OK).body(reservas);
    }

    @Operation(
    summary = "Obtener todas las reservas por email del cliente",
    description = "Obtiene todas las reservas registradas en la aplicación para un cliente específico, identificado por su email."
    )
    @Parameter(
        name = "email",
        description = "Email del cliente cuyas reservas se desean obtener",
        example = "usuario@email.com",
        required = true
    )
    @ApiResponse(
        responseCode = "200",
        description = "Reservas obtenidas correctamente",
        content = @Content(
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = ReservasDto.class))
        )
    )
    @GetMapping("/reservas")
    public ResponseEntity<List<ReservasDto>> obtenerReservasPorEmail(@RequestParam("email") String emailcliente){
        List<ReservasDto> reservas=reservasService.obtenerReservasPorEmail(emailcliente);
        return ResponseEntity.status(HttpStatus.OK).body(reservas);
    }

    @Operation(
        summary = "Obtener reservas por fecha y email del cliente",
        description = "Obtiene una lista de reservas completadas en una fecha específica por un cliente, identificado por su email."
    )
    @Parameter(
        name = "fecha",
        description = "Fecha de la reserva en formato YYYY-MM-DD",
        example = "2021-06-01",
        required = true
    )
    @Parameter(
        name = "email",
        description = "Email del cliente cuyas reservas se desean obtener",
        example = "usuario@email.com",
        required = true
    )
    @ApiResponse(
        responseCode = "200",
        description = "Reservas obtenidas correctamente",
        content = @Content(
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = ReservasDto.class))
        )
    )
    @GetMapping("/reservas/completadas")
    public ResponseEntity<List<ReservasDto>> obtenerReservasCompletadasFecha(@RequestParam("fecha") String fecha, @RequestParam("email") String email){
        List<ReservasDto> reservas=reservasService.obtenerReservasCompletadas(fecha,email);
        return ResponseEntity.status(HttpStatus.OK).body(reservas);
    }

    @Operation(
    summary = "Obtener reservas activas en una fecha específica",
    description = "Obtiene una lista de reservas activas para el cliente especificado en una fecha específica."
    )
    @Parameter(
        name = "fecha",
        description = "Fecha de la reserva en formato YYYY-MM-DD",
        example = "2021-06-01",
        required = true
    )
    @Parameter(
        name = "email",
        description = "Email del cliente cuyas reservas activas se desean obtener",
        example = "usuario@email.com",
        required = true
    )
    @ApiResponse(
        responseCode = "200",
        description = "Reservas obtenidas correctamente",
        content = @Content(
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = ReservasDto.class))
        )
    )
    @GetMapping("/reservas/activas")
    public ResponseEntity<List<ReservasDto>> obtenerReservasActivasFecha(@RequestParam("fecha") String fecha, @RequestParam("email") String email){
        List<ReservasDto> reservas=reservasService.obtenerReservasActivas(fecha,email);
        return ResponseEntity.status(HttpStatus.OK).body(reservas);
    }

    @Operation(
    summary = "Actualizar el estado de una reserva",
    description = "Actualiza el estado de una reserva a 'Finalizado' cuando concluye."
    )
    @Parameter(
        name = "id",
        description = "Identificador de la reserva que se desea actualizar",
        example = "1",
        required = true
    )
    @ApiResponse(
        responseCode = "200",
        description = "Estado de la reserva actualizado correctamente",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ResponseDTO.class)
        )
    )
    @PutMapping("/reservas/{id}")
    public ResponseEntity<ResponseDTO> actualizarEstadoReserva(@PathVariable("id") int id){
        
        reservasService.actualizarEstado(id,"FINALIZADO");
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(ReservasConstant.HTTP_STATUS_OK, ReservasConstant.HTTP_STATUS_OK_MESSAGE));
    }

    @Operation(
    summary = "Obtener una reserva activa por ID de vehículo",
    description = "Devuelve el ID de una reserva activa asociada a un vehículo específico identificado por su ID."
    )
    @Parameter(
        name = "idvehiculo",
        description = "Identificador del vehículo cuyas reservas activas se desean consultar",
        example = "123",
        required = true
    )
    @ApiResponse(
        responseCode = "200",
        description = "Reserva activa obtenida correctamente",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(type = "integer", description = "ID de la reserva activa")
        )
    )
    @GetMapping("/reserva/{idvehiculo}")
    public int obtenerReservaPorIdVehiclo(@PathVariable("idvehiculo") int idvehiculo){

        Reservas reserva=reservasService.obtenerReservaPorIdVehiculo(idvehiculo);
        
        return reserva.getIdreserva();
    }

    @GetMapping("/reserva/estadisticas/alquilados")
    public ResponseEntity<Map<Integer, Integer>> obtenerestadisticasPorMeses(@RequestParam("email") String email) {
        Map<Integer,Integer> reservasPorMeses=reservasService.obtenerEstadisticasPorCliente(email);
        return ResponseEntity.status(HttpStatus.OK).body(reservasPorMeses);
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
