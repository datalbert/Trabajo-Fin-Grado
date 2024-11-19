package trabajotfg.gps.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownContentTypeException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.xstream.mapper.Mapper.Null;

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
import jakarta.ws.rs.client.Entity;
import lombok.AllArgsConstructor;
import trabajotfg.gps.dto.ComunidadesDTO;
import trabajotfg.gps.dto.EntityDTO;
import trabajotfg.gps.dto.FinalizarDTO;
import trabajotfg.gps.dto.ResponseDTO;
import trabajotfg.gps.services.GpsService;
import trabajotfg.gps.services.clients.InventarioClient;
import trabajotfg.gps.services.clients.ReservasClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.hc.client5.http.classic.methods.HttpHead;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties.Http;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Collections;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PutMapping;
;


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
@AllArgsConstructor
@Tag(name = "Gps", description = "API para la gestión de la localización de los vehículos")
public class GpsController {

    private GpsService gpsService;

    private InventarioClient inventarioClient;

    private ReservasClient reservasClient;

    private final String url = "http://localhost:1026/v2/entities";

    @Operation(summary = "Crear una nueva entidad en FIWARE")
    @ApiResponse(
        responseCode = "200",
        description = "Entidad creada correctamente",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))
    )
    @ApiResponse(
        responseCode = "400",
        description = "Solicitud inválida (datos de la entidad no válidos)",
        content = @Content(mediaType = "application/json")
    )
    @ApiResponse(
        responseCode = "500",
        description = "Error interno en el servidor al intentar crear la entidad",
        content = @Content(mediaType = "application/json")
    )
    @PostMapping()
    @ApiResponse(responseCode = "200", description = "Entidad creada correctamente")
    public ResponseDTO crearEntidad(
        @Parameter(description = "Objeto EntityDTO que contiene los detalles de la nueva entidad a crear", required = true)
        @RequestBody EntityDTO entity) {
        //TODO: process POST request

        String url = "http://localhost:1026/v2/entities";

        JSONObject entityjson= gpsService.createJson( entity);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        
        HttpEntity<String> request = new HttpEntity<>(entityjson.toString(), headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        
        return new ResponseDTO("200", "OK",null);
    }

    @Operation(summary = "Obtener todas las entidades del Orion Context Broker")
    @ApiResponse(
        responseCode = "200",
        description = "Lista de entidades obtenida correctamente",
        content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EntityDTO.class)))
    )
    @ApiResponse(
        responseCode = "500",
        description = "Error interno en el servidor al intentar obtener las entidades",
        content = @Content(mediaType = "application/json")
    )
    @GetMapping()
    public List<EntityDTO> obtenerEntidades() {

        String url = "http://localhost:1026/v2/entities";

        HttpHeaders headers = new HttpHeaders();

        HttpEntity request = new HttpEntity(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        ObjectMapper mapper = new ObjectMapper();
        List<EntityDTO> entities = new ArrayList<>();

        try {
            JsonNode root = mapper.readTree(response.getBody());
            for (JsonNode node : root) {
                EntityDTO entity = new EntityDTO();
                entity.setMatricula(node.get("id").asText());
                entity.setLatitud(node.get("position").get("value").asText().split(",")[0]);
                entity.setLongitud(node.get("position").get("value").asText().split(",")[1]);
                entities.add(entity);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return entities;

    
    }
    @Operation(summary = "Buscar una entidad por su matrícula")
    @ApiResponse(
        responseCode = "200",
        description = "Entidad obtenida correctamente",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = EntityDTO.class))
    )
    @ApiResponse(
        responseCode = "404",
        description = "Entidad no encontrada para la matrícula proporcionada",
        content = @Content(mediaType = "application/json")
    )
    @ApiResponse(
        responseCode = "500",
        description = "Error interno en el servidor al intentar obtener la entidad",
        content = @Content(mediaType = "application/json")
    )
    @GetMapping("/{matricula}")
    public EntityDTO obtenerEntidad(
        @Parameter(description = "Matrícula del coche a buscar en el sistema", required = true, example = "4938LHR")
        @PathVariable("matricula") String matricula) {

        String url = "http://localhost:1026/v2/entities/" + matricula;

        HttpHeaders headers = new HttpHeaders();

        HttpEntity request = new HttpEntity(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        ObjectMapper mapper = new ObjectMapper();
        EntityDTO entity = new EntityDTO();

        try {
            JsonNode root = mapper.readTree(response.getBody());
            entity.setMatricula(root.get("id").asText());
            entity.setLatitud(root.get("position").get("value").asText().split(",")[0]);
            entity.setLongitud(root.get("position").get("value").asText().split(",")[1]);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
    
        }

        return entity;
    }
    @Operation(summary = "Eliminar una entidad del sistema usando su matrícula")
    @ApiResponse(
        responseCode = "200",
        description = "Devuelve un ResponseDTO con el código y el mensaje de la operación",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))
    )
    @DeleteMapping("/{matricula}")
    public ResponseDTO eliminarEntidad(
        @Parameter(description = "Matrícula del coche a buscar en el sistema", required = true, example = "4938LHR")
        @PathVariable("matricula") String matricula) {

        String url_delete = url + "/" +matricula;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", null);  // Explicitly set Content-Type to null

        HttpEntity<Void> request = new HttpEntity<>(headers);  // Ensure request is empty

        RestTemplate restTemplate = new RestTemplate();

        try {
            // Send the DELETE request
            ResponseEntity<String> response = restTemplate.exchange(url_delete, HttpMethod.DELETE, request, String.class);

            // Check if the response status is 204 No Content
            if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                return new ResponseDTO("204", "Entity deleted successfully", null);
            } else {
                return new ResponseDTO(response.getStatusCode().toString(), "Unexpected response", null);
            }
        } catch (HttpClientErrorException e) {
            // Handle the error case if the entity does not exist or there is a bad request
            return new ResponseDTO("400", "Bad Request: " + e.getResponseBodyAsString(), null);
        }
  
    }

    @Operation(summary = "Finalizar la reserva, actualizando la entidad")
    @PutMapping()
    public ResponseDTO finalizarReserva(@RequestBody FinalizarDTO entity) {
        /*la secuencia que debe tomar este método es la siguiente
            1º Inventario para actualizar el estado del coche a DISPONIBLE
            2º Reservas para actualizar la reserva a finalizada
            3º Gps para actualizar la entidad
        */
        
        //1º Inventario para actualizar el estado del coche a DISPONIBLE
        ResponseEntity<ResponseDTO> inventarioestado=inventarioClient.actualizarEstado(entity.getIdVehiculo(), "DISPONIBLE");
        //2º Reservas para actualizar la reserva a finalizada
        ResponseEntity<ResponseDTO> reservasestado=reservasClient.actualizarEstadoReserva(entity.getIdReserva());
        
        String url_update= url+"/"+entity.getMatricula()+"/attrs";

        EntityDTO entitygps= new EntityDTO();
        entitygps.setMatricula(entity.getMatricula());
        entitygps.setLatitud(entity.getLatitud());
        entitygps.setLongitud(entity.getLongitud());

        JSONObject entityjson= gpsService.createJsonUptade( entitygps);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        
        HttpEntity<String> request = new HttpEntity<>(entityjson.toString(), headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url_update, HttpMethod.PUT, request, String.class);
        
        return new ResponseDTO("204", "Actualizado",null);
    }


    @GetMapping("/comunidades")
    public ResponseDTO getComunidades() {
        
        String type = "JSON";
        String key = "935f304a117600b9407a41c8d89fc614090f3fe4bdcdc56f5e86ce6cbe0bf570";
        String sandbox = "0";

        String url = "http://apiv1.geoapi.es/comunidades?type=" + type + "&key=" + key + "&sandbox=" + sandbox;
        
       return new ResponseDTO("200", "no implementrado ", null);
    }

    @GetMapping("{latitud}/{longitud}/{radio}")
    public List<EntityDTO> filtrarporUbicacion(@PathVariable("latitud") String latitud, @PathVariable("longitud") String longitud, @PathVariable("radio") String radio) {


        String ulrubicacion= url+"?type=Car&georel=near;maxDistance:"+radio+"&geometry=point&coords="+latitud+","+longitud;

        HttpHeaders headers = new HttpHeaders();

        HttpEntity request = new HttpEntity(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(ulrubicacion, HttpMethod.GET, request, String.class);

        ObjectMapper mapper = new ObjectMapper();
        List<EntityDTO> entities = new ArrayList<>();

        try {
            JsonNode root = mapper.readTree(response.getBody());
            for (JsonNode node : root) {
                EntityDTO entity = new EntityDTO();
                entity.setMatricula(node.get("id").asText());
                entity.setLatitud(node.get("position").get("value").asText().split(",")[0]);
                entity.setLongitud(node.get("position").get("value").asText().split(",")[1]);
                entities.add(entity);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return entities;
    }


    @PostMapping("/notificacion")
    public void recibirNotificacion(@RequestBody Map<String, Object> payload) {
        System.out.println("Notificación recibida de FIWARE: " + payload);

        // Verifica si la notificación contiene la clave "data"
        if (payload.containsKey("data")) {
            List<Map<String, Object>> data = (List<Map<String, Object>>) payload.get("data");
            data.forEach(entity -> {
                // Obtén el ID y el tipo de entidad de forma dinámica
                String entityId = (String) entity.get("id");
                String entityType = (String) entity.get("type");
                System.out.println("Entidad recibida: ID = " + entityId + ", Tipo = " + entityType);

                //entityId será la matricula
                //LLamar a un método que se encarge de finalizar la reserva 
                gpsService.finalizarReserva(entityId);
                // Recorre los atributos de la entidad
                /* 
                entity.forEach((key, value) -> {
                    if (!"id".equals(key) && !"type".equals(key)) {
                        // Procesa cada atributo dinámicamente
                        Map<String, Object> attribute = (Map<String, Object>) value;
                        String attributeType = (String) attribute.get("type");
                        Object attributeValue = attribute.get("value");

                        System.out.println("Atributo: " + key + " | Tipo: " + attributeType + " | Valor: " + attributeValue);

                        // Aquí puedes realizar el procesamiento necesario según el atributo
                    }
                });
                */
            });
        }
    }

    @PostMapping("/subscripcion/{matricula}/{fecha}")
    //este método para crear la subscripción de un vehiculo recibe la matricula del vehiculo
    public String crearSubscripcion(@PathVariable("matricula") String matricula, @PathVariable("fecha") String fecha) {
        //TODO: process POST request
        
        String url_subscription="http://localhost:1026/v2/subscriptions";

        JSONObject subscription = new JSONObject();
        subscription.put("description", "Notificación de cambios en la posición del carro " + matricula);

        JSONObject subject = new JSONObject();
        JSONArray entities = new JSONArray();
        JSONObject entity = new JSONObject();
        entity.put("id", matricula);
        entity.put("type", "Car");
        entities.put(entity);
        subject.put("entities", entities);

        JSONObject condition = new JSONObject();
        JSONArray attrs = new JSONArray();
        attrs.put("position");
        condition.put("attrs", attrs);
        subject.put("condition", condition);
        subscription.put("subject", subject);

        JSONObject notification = new JSONObject();
        JSONObject http = new JSONObject();
        http.put("url", "http://192.168.1.85:8083/notificacion");
        notification.put("http", http);
        notification.put("attrs", attrs);
        subscription.put("notification", notification);

        subscription.put("expires",fecha+"T23:59:59.00Z");
        subscription.put("throttling", 5);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(subscription.toString(), headers);

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.exchange(url_subscription, HttpMethod.POST, request, String.class);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return "Unexpected error: " + e.getMessage();
        }

    }
    

}
