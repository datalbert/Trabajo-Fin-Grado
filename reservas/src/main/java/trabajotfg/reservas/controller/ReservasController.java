package trabajotfg.reservas.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import trabajotfg.reservas.constant.ReservasConstant;
import trabajotfg.reservas.dto.ReservasDto;
import trabajotfg.reservas.dto.ResponseDTO;
import trabajotfg.reservas.service.ReservasService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("reservas")
@Tag(name = "Inventario", description = "API para la Gestión de Inventario")
@AllArgsConstructor
public class ReservasController {

    private ReservasService reservasService;

    @PostMapping
    public ResponseEntity<ResponseDTO> crearReserva(@RequestBody ReservasDto reserva){
        reservasService.crearReserva(reserva);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(ReservasConstant.HTTP_STATUS_CREATED, ReservasConstant.HTTP_STATUS_CREATED_MESSAGE));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> eliminarReserva(@PathVariable int id){
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(ReservasConstant.HTTP_STATUS_OK, reservasService.eliminarReserva(id)));
    }

   

    

    
    
    





}
