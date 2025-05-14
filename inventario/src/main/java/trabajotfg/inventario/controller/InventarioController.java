package trabajotfg.inventario.controller;

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
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import jakarta.ws.rs.client.Entity;
import lombok.AllArgsConstructor;
import trabajotfg.inventario.constant.InventarioConstant;
import trabajotfg.inventario.dto.EntityDTO;
import trabajotfg.inventario.dto.InventarioDto;
import trabajotfg.inventario.dto.ResponseDTO;
import trabajotfg.inventario.entity.Coches;
import trabajotfg.inventario.mapped.InventarioMapped;
import trabajotfg.inventario.services.InventarioService;
import trabajotfg.inventario.services.clients.GpsClient;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.ArrayList;

import org.hibernate.annotations.Array;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;





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
@Tag(name = "Inventario", description = "API para la Gestión de Inventario")
@AllArgsConstructor
public class InventarioController {

    private InventarioService inventarioService;

    private GpsClient gpsClient;


    @Operation(summary = "Insertar un nuevo coche")
    @ApiResponse(responseCode = "201", description = "Coche insertado correctamente")
    @PostMapping("/coches")
    public ResponseEntity<ResponseDTO> insertarNuevocoche(@RequestPart("nuevocoche")InventarioDto nuevocoche) throws IOException{
        //TODO: process POST request

        //cuando se inserta un nuevo coche se debe modificar tambien el estado del coche
        inventarioService.insertarCoche(nuevocoche);

        //con eureka llamar a la api de gps para crear la entidad del coche en la tabla de gps
        EntityDTO entity = new EntityDTO();
        entity.setMatricula(nuevocoche.getMatricula());
        entity.setLatitud(nuevocoche.getLatitud());
        entity.setLongitud(nuevocoche.getLongitud());
        gpsClient.crearEntidad(entity);

        
        
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(InventarioConstant.HTTP_STATUS_CREATED, InventarioConstant.HTTP_STATUS_CREATED_MESSAGE));
    }

    @Operation(summary = "Obtener un coche por su identificador de la base de datos")
    @ApiResponses ( value = {
        @ApiResponse(
            responseCode = "200",
            description = "Coche obtenido correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = InventarioDto.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Coche no encontrado")
    })
    @GetMapping("/coches/{id}")
    public ResponseEntity<InventarioDto> obtenerCochePorId (@PathVariable("id")  int id) {


        InventarioDto coche = InventarioMapped.maptoDto(inventarioService.obtenerCoche(id), new InventarioDto());

        return ResponseEntity.status(HttpStatus.OK).body(coche);
    }

    @Operation(summary = "Obtener un coche por su matricula")
    @ApiResponses ( value = {
        @ApiResponse(
            responseCode = "200",
            description = "Coche obtenido correctamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = InventarioDto.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Coche no encontrado")
    })
    @GetMapping("/coches/matricula/{matricula}")
    public ResponseEntity<InventarioDto> obtenerCochePorMatricula(@PathVariable("matricula") String matricula) {
        
        InventarioDto coche = InventarioMapped.maptoDto(inventarioService.obtenerCochesPorMatricula(matricula), new InventarioDto());

        return ResponseEntity.status(HttpStatus.OK).body(coche);

    }
    

    @Operation(summary = "Obtener todos los coches del inventario")
    @ApiResponses ( value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de todos los coches obtenidos correctamente del inventario",
            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = InventarioDto.class)))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Coche no encontrado")
    })
    @GetMapping("/coches/todos")
    public ResponseEntity<List<InventarioDto>> obtenerTodos(){

        List<InventarioDto> devolverlista=inventarioService.obtenerCoches();

        return ResponseEntity.status(HttpStatus.OK).body(devolverlista);
    }

    @Operation(summary = "Obtener todos los coches del inventario pertenecientes a un usuario")
    @ApiResponses ( value = {
        @ApiResponse(
            responseCode = "200",
            description = "lista de coches pertenecientes a un usuario",
            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = InventarioDto.class)))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Coche no encontrado")
    })
    @GetMapping("/coches/propietario")
    public ResponseEntity<List<InventarioDto>> obtenerCochesPorPropietario(@RequestParam("email") String emailpropietario){

        List<InventarioDto> devolverlista=inventarioService.obtenerCochesPorPropietario(emailpropietario);

        return ResponseEntity.status(HttpStatus.OK).body(devolverlista);
    }

    @Operation(summary = "Obtener el listado de los coches con estado disponibles")
    @ApiResponses ( value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de coches disponibles  del inventario",
            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = InventarioDto.class)))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Coche no encontrado")
    })
    @GetMapping("/coches/disponibles")
    public ResponseEntity<List<InventarioDto>> obtenerDisponibles(){

        List<InventarioDto> devolverlista=inventarioService.obtenerDisponibles();

        for (InventarioDto coche : devolverlista) {
            //llamar al gps para obtener la localización de cada coche disponible
            EntityDTO entity = gpsClient.obtenerEntidad(coche.getMatricula());
            coche.setLatitud(entity.getLatitud());
            coche.setLongitud(entity.getLongitud());
        }
        

        return ResponseEntity.status(HttpStatus.OK).body(devolverlista);
    }

    @Operation(summary = "Obtener el listado de los coches con estado disponibles")
    @ApiResponses ( value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de coches disponibles  del inventario",
            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = InventarioDto.class)))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Coche no encontrado")
    })
    @GetMapping("/coches/disponibles/fecha")
    public ResponseEntity<List<InventarioDto>> obtenerDisponiblesPorFechas(@RequestParam("fechainicio") String fechainicio, @RequestParam("fechafin") String fechafin){

        List<InventarioDto> devolverlista=inventarioService.obtenerDisponiblesFechas(fechainicio, fechafin);

        for (InventarioDto coche : devolverlista) {
            //llamar al gps para obtener la localización de cada coche disponible
            EntityDTO entity = gpsClient.obtenerEntidad(coche.getMatricula());
            coche.setLatitud(entity.getLatitud());
            coche.setLongitud(entity.getLongitud());
        }
        

        return ResponseEntity.status(HttpStatus.OK).body(devolverlista);
    }



    @Operation(summary = "Actualización del estado de un coche")
    @ApiResponse(
        responseCode = "200",
        description = "Estado del coche actualizado correctamente",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
    )
    @PutMapping("/coches/{idvehiculo}/estado")
    public ResponseEntity<ResponseDTO> actualizarEstado(
            @Parameter(description = "Identificador único del coche en la base de datos", required = true, example = "123")
            @PathVariable int idvehiculo,

            @Parameter(description = "Nuevo estado que se desea asignar al coche (por ejemplo, 'Disponible', 'En mantenimiento')", required = true, example = "Disponible")
            @RequestBody String estado){

        inventarioService.modificarEstado(idvehiculo, estado);


        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(InventarioConstant.HTTP_STATUS_OK, InventarioConstant.HTTP_STATUS_CHANGE));
  
    }
    @Operation(summary = "Actualizar información de un coche en el inventario")
    @ApiResponse(
        responseCode = "200",
        description = "Información del coche actualizada correctamente",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))
    )
    @ApiResponse(
        responseCode = "400",
        description = "Datos de entrada inválidos",
        content = @Content(mediaType = "application/json")
    )
    @PutMapping("coches")
    public  ResponseEntity<ResponseDTO> actualizarCoche(
        @Parameter(description = "Objeto InventarioDto con los datos actualizados del coche", required = true)
        @RequestBody InventarioDto nuevocoche) throws IOException{
        //TODO: process PUT request

        inventarioService.actualizarCoche(nuevocoche);
        
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(InventarioConstant.HTTP_STATUS_OK, InventarioConstant.HTTP_VEHICULE_CHANGE));
    }

    //@CrossOrigin
    @Operation(summary = "Eliminar un coche del inventario")
    @ApiResponse(
        responseCode = "200",
        description = "Coche eliminado correctamente",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))
    )
    @ApiResponse(
        responseCode = "404",
        description = "Coche no encontrado",
        content = @Content(mediaType = "application/json")
    )
    @DeleteMapping("/coches/{id}") 
    public ResponseEntity<ResponseDTO> eliminarCoche(
        @Parameter(description = "Identificador único del coche a eliminar", required = true, example = "123")

        @PathVariable("id") int id){

        inventarioService.eliminarCoche(id);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(InventarioConstant.HTTP_STATUS_OK, InventarioConstant.HTTP_RECURSE_DELETED));
    }

    @Operation(summary = "Buscar coches en una ubicación específica")
    @ApiResponse(
        responseCode = "200",
        description = "Lista de coches obtenida correctamente en la ubicación especificada",
        content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = InventarioDto.class)))
    )
    @ApiResponse(
        responseCode = "404",
        description = "No se encontraron coches en la ubicación especificada",
        content = @Content(mediaType = "application/json")
    )
    @GetMapping("/coches/ubicacion/{latitud}/{longitud}/{radio}")
    public ResponseEntity<List<InventarioDto>> buscarPorCategoria(
        @Parameter(description = "Latitud de la ubicación de búsqueda", required = true, example = "40.7128")
        @PathVariable("latitud") String latitud, 
        @Parameter(description = "Longitud de la ubicación de búsqueda", required = true, example = "-74.0060")
        @PathVariable("longitud") String longitud, 
        @Parameter(description = "Radio de búsqueda en kilómetros", required = true, example = "10")
        @PathVariable("radio") String radio) {
        
        //llamar a gps para obtener la lista de coches que se encuentran en un radio 
        List<EntityDTO> lista = gpsClient.filtrarporUbicacion(latitud, longitud, radio);

        //obtener la lista de coches que se encuentran en la lista de gps

        List<InventarioDto> inventariocompleto = new ArrayList<>();
        
        for (EntityDTO entity : lista) {
            Coches coche = inventarioService.obtenerCochesPorMatricula(entity.getMatricula());
            InventarioDto cocheDto = InventarioMapped.maptoDto(coche, new InventarioDto());
            cocheDto.setLatitud(entity.getLatitud());
            cocheDto.setLongitud(entity.getLongitud());
            inventariocompleto.add(cocheDto);
        }

        return ResponseEntity.status(HttpStatus.OK).body(inventariocompleto);


    }
    

    @Operation(summary = "Buscar coches por diferentes filtros de categorías")
    @ApiResponse(
        responseCode = "200",
        description = "Lista de coches obtenida correctamente por los filtros seleccionados",
        content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = InventarioDto.class)))
    )
    @GetMapping("/coches")
    public ResponseEntity<List<InventarioDto>> searchByFilters(
        @Parameter(description = "Marca del coche para filtrar", example = "Toyota")
        @RequestParam(defaultValue="") String marca,
        @Parameter(description = "Modelo del coche para filtrar", example = "Corolla") 
        @RequestParam(defaultValue="") String modelo, 
        @Parameter(description = "Tipo de combustible del coche para filtrar", example = "Gasolina")
        @RequestParam(defaultValue="") String combustible, 
        @Parameter(description = "Tipo de transmisión del coche para filtrar", example = "Automático")
        @RequestParam(defaultValue="") String transmision, 
        @Parameter(description = "Número de asientos del coche para filtrar", example = "5")
        @RequestParam(defaultValue="") String numAsientos) {
            Integer asientosInt = null;
            if (!numAsientos.isBlank()) {
                try {
                    asientosInt = Integer.parseInt(numAsientos);
                } catch (NumberFormatException e) {
                    // Aquí puedes manejar el error o ignorarlo para omitir el filtro
                    return ResponseEntity.badRequest().build(); // si decides cortar
                }
            }
        
            List<InventarioDto> inventarioCompleto = inventarioService.searchcarsByFilters(
                marca, modelo, combustible, transmision, asientosInt
            );
        return ResponseEntity.status(HttpStatus.OK).body(inventarioCompleto);
        //List<InventarioDto> inventariocompleto = inventarioService.searchcarsByFilters(marca, modelo, combustible, transmision, numAsientos);
        //return ResponseEntity.status(HttpStatus.OK).body(inventariocompleto);
        
    }
    

    


}


