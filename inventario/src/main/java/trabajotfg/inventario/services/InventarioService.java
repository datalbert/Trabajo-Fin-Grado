package trabajotfg.inventario.services;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import trabajotfg.inventario.dto.InventarioDto;
import trabajotfg.inventario.entity.Coches;



public interface InventarioService {

    void insertarCoche(InventarioDto cochenuevo) throws IOException;

    void eliminarCoche(int id);

    Coches obtenerCoche(int id);

    void actualizarCoche(InventarioDto coche);

    List<InventarioDto> obtenerCoches();


    List<InventarioDto> obtenerDisponibles();

    void modificarEstado(int id, String estado);

    List<InventarioDto> searchcarsByFilters(String marca, String modelo, String combustible, String transmision, String numAsientos);

    
    

}
