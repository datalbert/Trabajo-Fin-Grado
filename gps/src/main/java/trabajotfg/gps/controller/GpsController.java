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

import io.swagger.v3.oas.annotations.Operation;
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

import org.apache.hc.client5.http.classic.methods.HttpHead;
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
@AllArgsConstructor
public class GpsController {

    private GpsService gpsService;

    private InventarioClient inventarioClient;

    private ReservasClient reservasClient;

    private final String url = "http://localhost:1026/v2/entities";

    @PostMapping()
    public ResponseDTO crearEntidad(@RequestBody EntityDTO entity) {
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

    @GetMapping("/{matricula}")
    public EntityDTO obtenerEntidad(@PathVariable("matricula") String matricula) {

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

    @DeleteMapping("/{matricula}")
    public ResponseDTO eliminarEntidad(@PathVariable("matricula") String matricula) {

        String url = "http://localhost:1026/v2/entities/" + matricula;

        HttpHeaders headers = new HttpHeaders();
        

        HttpEntity request = new HttpEntity(headers);


        RestTemplate restTemplate = new RestTemplate();

         try {
            // Send the DELETE request
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, request, String.class);

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

}
