package trabajotfg.reservas.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import trabajotfg.reservas.constant.ReservasConstant;
import trabajotfg.reservas.dto.ReservasDto;
import trabajotfg.reservas.dto.ResponseDTO;
import trabajotfg.reservas.service.ReservasService;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping
@Tag(name = "Reservas", description = "API para la Gestión de las reservas de los vehiculos en la Apliación")
@AllArgsConstructor
public class ReservasController {

    private ReservasService reservasService;

    @Operation(summary = "Registrar una reserva", description = "Registro de una reserva en la aplicación. Interviene Imventario para modificar el estado del vehiculo y Pago para realizar el pago de la reserva.")
    @Parameter(description = "Objeto JSON de la reserva a registrar")
    @ApiResponse(responseCode = "201", description = "Reserva registrada correctamente")

    @PostMapping
    public ResponseEntity<ResponseDTO> crearReserva(@RequestBody ReservasDto reserva){
        
        reservasService.crearReserva(reserva);
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
    @GetMapping
    public ResponseEntity<List<ReservasDto>> obtenerReservas(){
        List<ReservasDto> reservas=reservasService.obtenerReservas();
        return ResponseEntity.status(HttpStatus.OK).body(reservas);
    }

    @Operation(summary = "Obtener una reserva por fecha", description = "Este método devuelve las reservas completadas  la fecha actual.")
    @Parameter(description = "Fecha de la reserva", example = "2021-06-01")
    @ApiResponse(responseCode = "200", description = "Reservas obtenidas correctamente")
    @GetMapping("/completadas/{fecha}")
    public ResponseEntity<List<ReservasDto>> obtenerReservasCompletadasFecha(@PathVariable String fecha){
        List<ReservasDto> reservas=reservasService.obtenerReservasCompletadas(fecha);
        return ResponseEntity.status(HttpStatus.OK).body(reservas);
    }

    @Operation(summary = "Obtener una reserva por fecha", description = "Este método devuelve las reservas activas a la fecha actual.")
    @Parameter(description = "Fecha de la reserva", example = "2021-06-01")
    @ApiResponse(responseCode = "200", description = "Reservas obtenidas correctamente")
    @GetMapping("/activas/{fecha}")
    public ResponseEntity<List<ReservasDto>> obtenerReservasActivasFecha(@PathVariable String fecha){
        List<ReservasDto> reservas=reservasService.obtenerReservasActivas(fecha);
        return ResponseEntity.status(HttpStatus.OK).body(reservas);
    }




    

   

    

    
    
    





}
