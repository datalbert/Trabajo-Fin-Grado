package trabajotfg.inventario.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import trabajotfg.inventario.constant.InventarioConstant;
import trabajotfg.inventario.dto.InventarioDto;
import trabajotfg.inventario.dto.ResponseDTO;
import trabajotfg.inventario.entity.Coches;
import trabajotfg.inventario.services.InventarioService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import org.springframework.http.MediaType;




@RestController
@RequestMapping("inventario")
@Tag(name = "Inventario", description = "API para la Gestión de Inventario")
@AllArgsConstructor
public class InventarioController {

    private InventarioService inventarioService;


    @Operation(summary = "Insertar un nuevo coche")
    @ApiResponse(responseCode = "201", description = "Coche insertado correctamente")
    @PostMapping("/nuevoCoche")
    public ResponseEntity<ResponseDTO> insertarNuevocoche(@RequestPart("nuevocoche")InventarioDto nuevocoche, @RequestParam("fotos") MultipartFile imageFile) throws IOException{
        //TODO: process POST request

        //cuando se inserta un nuevo coche se debe modificar tambien el estado del coche
        inventarioService.insertarCoche(nuevocoche, imageFile);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(InventarioConstant.HTTP_STATUS_CREATED, InventarioConstant.HTTP_STATUS_CREATED_MESSAGE));
    }

    
    @Operation(summary = "Obtener la imagen de un coche del inventario")
    @ApiResponse(responseCode = "200", description = "Devuelve la imagen en formato Byte[] del coche seleccionado")
    @GetMapping("/obtenerImagen")
    public ResponseEntity<?> obtenerImagen(@RequestParam("id") int id){

        Coches coche = inventarioService.obtenerCoche(id);

        byte[] imageData= coche.getFotos();

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf(IMAGE_JPEG_VALUE))
                .body(imageData);

    }

    @GetMapping("/obtenerImagen2")
    public ResponseEntity<?> obtenerImagen2(@RequestParam("id") int id) {
        Coches coche = inventarioService.obtenerCoche(id);
        byte[] imageData = coche.getFotos();

        // Convertir los bytes de la imagen a una cadena Base64
        String imageDataBase64 = Base64.getEncoder().encodeToString(imageData);

        // Devolver la cadena Base64 como parte de la respuesta
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/jpeg"))
                .body(imageDataBase64);
    }

    @Operation(summary = "Obtener todos los coches del inventario")
    @ApiResponse(responseCode = "200", description = "Lista de coches obtenida correctamente")
    @GetMapping("/obtenerTodos")
    public ResponseEntity<List<InventarioDto>> obtenerTodos(){

        List<InventarioDto> devolverlista=inventarioService.obtenerCoches();

        return ResponseEntity.status(HttpStatus.OK).body(devolverlista);
    }

    @Operation(summary = "Obtener el listado de los coches con estado disponibles")
    @ApiResponse(responseCode = "200", description = "Lista de coches disponibles obtenida correctamente")
    @GetMapping("/obtenerDisponibles")
    public ResponseEntity<List<InventarioDto>> obtenerDisponibles(){

        List<InventarioDto> devolverlista=inventarioService.obtenerDisponibles();

        return ResponseEntity.status(HttpStatus.OK).body(devolverlista);
    }

    @Operation(summary = "Actualización del estado de un coche")
    @ApiResponse(responseCode = "200", description = "Estado del coche actualizado correctamente")
    @PutMapping("/actualizarEstado")
    public ResponseEntity<ResponseDTO> actualizarEstado(@RequestParam int idvehiculo,@RequestParam String estado){

        inventarioService.modificarEstado(idvehiculo, estado);


        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(InventarioConstant.HTTP_STATUS_OK, InventarioConstant.HTTP_STATUS_CHANGE));
  
    }

    @Operation(summary = "Eliminar un coche del inventario")
    @ApiResponse(responseCode = "200", description = "Coche eliminado correctamente")
    @DeleteMapping("/eliminarCoche/{id}") 
    public ResponseEntity<ResponseDTO> eliminarCoche(@PathVariable("id") int id){

        inventarioService.eliminarCoche(id);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(InventarioConstant.HTTP_STATUS_OK, InventarioConstant.HTTP_RECURSE_DELETED));
    }

    @Operation(summary = "Buscar coches por diferentes filtros")
    @ApiResponse(responseCode = "200", description = "Lista de coches obtenida correctamente por los filtros seleccionados")
    @GetMapping("/buscarporCategoria")
    public ResponseEntity<List<InventarioDto>> searchByFilters(
        @RequestParam(defaultValue="") String marca, 
        @RequestParam(defaultValue="") String modelo, 
        @RequestParam(defaultValue="") String combustible, 
        @RequestParam(defaultValue="") String transmision, 
        @RequestParam(defaultValue="") String numAsientos) {
        List<InventarioDto> inventariocompleto = inventarioService.searchcarsByFilters(marca, modelo, combustible, transmision, numAsientos);
        return ResponseEntity.status(HttpStatus.OK).body(inventariocompleto);
        
    }
    

    


}


