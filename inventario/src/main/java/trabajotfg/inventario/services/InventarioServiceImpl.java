package trabajotfg.inventario.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import trabajotfg.inventario.dto.InventarioDto;
import trabajotfg.inventario.entity.Coches;
import trabajotfg.inventario.entity.Estado;
import trabajotfg.inventario.mapped.InventarioMapped;
import trabajotfg.inventario.repository.CochesRepository;
import trabajotfg.inventario.repository.EstadoRepository;
import trabajotfg.inventario.specification.CochesSpecification;

@Service
@AllArgsConstructor
public class InventarioServiceImpl implements InventarioService{

    private CochesRepository cochesRepository;

    private EstadoRepository estadoRepository;

    @Override
    public void eliminarCoche(int id) {
        // TODO Auto-generated method stub
        cochesRepository.deleteById(id);
        
    }

    @Override
    public Coches obtenerCoche(int id) {
        // TODO Auto-generated method stub
        //Optional<Coches> cochedevolver= cochesRepository.findById(id);

        return cochesRepository.findById(id).orElseThrow(()-> new RuntimeException("Coche no encontrado"));

    }

    @Override
    public List<InventarioDto> obtenerCoches() {
        // TODO Auto-generated method stub

        List<InventarioDto> inventariocompleto = new ArrayList<>();

        for (Coches coches : cochesRepository.findAll()) {
            inventariocompleto.add(InventarioMapped.maptoDto(coches, new InventarioDto()));
        }

        return inventariocompleto;
    }

    

    @Override
    public void insertarCoche(InventarioDto cochenuevo,MultipartFile imagen) throws IOException {
        // TODO Auto-generated method stub
        
        Coches coche = InventarioMapped.maptoEntity(cochenuevo, new Coches());
        
        coche.setFotos(imagen.getBytes());
        
        Estado estado = new Estado();
        
        estado.setCoche(cochesRepository.save(coche));

        estado.setEstado("Disponible");

        estadoRepository.save(estado);


    }

    @Override
    public void insertarFoto(MultipartFile foto) throws IOException {

        Coches coche = new Coches();
        coche.setFotos(foto.getBytes());
        cochesRepository.save(coche);
        // TODO Auto-generated method stub
    }


    @Override
    public List<InventarioDto> obtenerDisponibles() {
        // Este método devuelve los coches Disponibles
        List<InventarioDto> inventariocompleto = new ArrayList<>();

        for (Coches coches: estadoRepository.findAllByEstado("Disponible")) {
            inventariocompleto.add(InventarioMapped.maptoDto(coches, new InventarioDto()));
        }

        return inventariocompleto;
    }

    @Override
    public void modificarEstado(int id, String estado) {
        // TODO Auto-generated method stub

        Coches coche = cochesRepository.findById(id).orElseThrow(()-> new RuntimeException("Coche no encontrado"));

        Estado estadoactual = estadoRepository.findByCoche(coche);

        estadoactual.setEstado(estado);

        estadoRepository.save(estadoactual);
        
    }

    @Override
    public List<InventarioDto> searchcarsByFilters(String marca, String modelo, String combustible, String transmision,
            String numAsientos) {
        // TODO Auto-generated method stub
        List<Coches> coches = cochesRepository.findAll(CochesSpecification.conditionalSearch(marca, modelo, combustible, transmision, numAsientos));
        List<InventarioDto> inventariocompleto = new ArrayList<>();
        for (Coches coche : coches){
            inventariocompleto.add(InventarioMapped.maptoDto(coche, new InventarioDto()));
        }
        return inventariocompleto;
    }
    

   
}
