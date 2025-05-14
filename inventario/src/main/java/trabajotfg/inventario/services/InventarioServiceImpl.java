package trabajotfg.inventario.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;


import lombok.AllArgsConstructor;
import trabajotfg.inventario.dto.InventarioDto;
import trabajotfg.inventario.entity.Coches;
import trabajotfg.inventario.entity.Estado;
import trabajotfg.inventario.mapped.InventarioMapped;
import trabajotfg.inventario.repository.CochesRepository;
import trabajotfg.inventario.repository.EstadoRepository;
import trabajotfg.inventario.services.clients.GpsClient;
import trabajotfg.inventario.services.clients.ReservasClient;
import trabajotfg.inventario.specification.CochesSpecification;

@Service
@AllArgsConstructor
public class InventarioServiceImpl implements InventarioService{

    private CochesRepository cochesRepository;

    private EstadoRepository estadoRepository;

    //cliente de GPS
    private GpsClient gpsClient;

    private ReservasClient reservasClient;

    @Override
    public void eliminarCoche(int id) {

        //llamar también al GPS para que elimine el coche de su base de datos
        gpsClient.eliminarEntidad(cochesRepository.findById(id).get().getMatricula());

        cochesRepository.deleteById(id);

    }

    @Override
    public Coches obtenerCoche(int id) {
        // TODO Auto-generated method stub
        //Optional<Coches> cochedevolver= cochesRepository.findById(id);

        return cochesRepository.findById(id).orElseThrow(()-> new RuntimeException("Coche no encontrado"));
    }

    @Override
    public Coches obtenerCochesPorMatricula(String matricula) {
        // TODO Auto-generated method stub
        return cochesRepository.findByMatricula(matricula);
       
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
    public void insertarCoche(InventarioDto cochenuevo) throws IOException {
        // TODO Auto-generated method stub
        
        Coches coche = InventarioMapped.maptoEntity(cochenuevo, new Coches());
        
        
        Estado estado = new Estado();
        
        estado.setCoche(cochesRepository.save(coche));

        estado.setEstado("Disponible");

        estadoRepository.save(estado);

        //tenemos que crear tambiñen la subscripción en el GPS
        gpsClient.crearSubscripcion(coche.getMatricula());


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
    public void actualizarCoche(InventarioDto coche) {
        // TODO Auto-generated method stub
        Coches vehiculo =cochesRepository.findById(coche.getId()).orElseThrow(()-> new RuntimeException("Coche no encontrado"));

        vehiculo.setMarca(coche.getMarca());
        vehiculo.setModelo(coche.getModelo());
        vehiculo.setMatricula(coche.getMatricula());
        vehiculo.setCombustible(coche.getCombustible());
        vehiculo.setTransmision(coche.getTransmision());
        vehiculo.setNumAsientos(coche.getNumAsientos());

        cochesRepository.save(vehiculo);
        

    }

    @Override
    public List<InventarioDto> searchcarsByFilters(String marca, String modelo, String combustible, String transmision,
            Integer numAsientos) {
        // TODO Auto-generated method stub
        List<Coches> coches = cochesRepository.findAll(CochesSpecification.conditionalSearch(marca, modelo, combustible, transmision,
         numAsientos,"Disponible"));
        List<InventarioDto> inventariocompleto = new ArrayList<>();
        for (Coches coche : coches){
            inventariocompleto.add(InventarioMapped.maptoDto(coche, new InventarioDto()));
        }
        return inventariocompleto;
    }

    @Override
    public List<InventarioDto> obtenerCochesPorPropietario(String email) {
        
        List<InventarioDto> inventariocompleto = new ArrayList<>();


        for (Coches coches : cochesRepository.findByEmailpropietario(email)) {
            //obtenemos el estado de cada uno de los coches
            Estado estado = estadoRepository.findByCoche(coches);
            InventarioDto coche_instertar = InventarioMapped.maptoDto(coches, new InventarioDto());
            coche_instertar.setEstado(estado.getEstado());
            //inventariocompleto.add(InventarioMapped.maptoDto(coches, new InventarioDto()));
            inventariocompleto.add(coche_instertar);
        }

        return inventariocompleto;

        
    }

    @Override
    public List<InventarioDto> obtenerDisponiblesFechas(String fechainicio, String fechafin) {
        // TODO Auto-generated method stub
        //Llamar a reserva para obtener los coches reservados en esas fechas
        List<Integer> ids = reservasClient.obtenerReservasPorFecha(fechainicio, fechafin);

        List<InventarioDto> inventariocompleto = new ArrayList<>();

        for (Coches coches : cochesRepository.findAllByIdcocheNotIn(ids)) {
            inventariocompleto.add(InventarioMapped.maptoDto(coches, new InventarioDto()));
        }

        return inventariocompleto;


    }

   

    
    

   
}
