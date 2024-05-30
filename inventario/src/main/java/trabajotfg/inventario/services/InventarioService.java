package trabajotfg.inventario.services;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import trabajotfg.inventario.dto.InventarioDto;
import trabajotfg.inventario.entity.Coches;



public interface InventarioService {

    void insertarCoche(InventarioDto cochenuevo,MultipartFile imagen) throws IOException;

    void eliminarCoche(int id);

    Coches obtenerCoche(int id);

    List<InventarioDto> obtenerCoches();

    void insertarFoto(MultipartFile foto) throws IOException;

    List<InventarioDto> obtenerDisponibles();

    void modificarEstado(int id, String estado);

    List<InventarioDto> searchcarsByFilters(String marca, String modelo, String combustible, String transmision, String numAsientos);

    
    

}
