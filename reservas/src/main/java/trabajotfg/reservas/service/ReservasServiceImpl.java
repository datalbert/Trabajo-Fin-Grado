package trabajotfg.reservas.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import trabajotfg.reservas.constant.ReservasConstant;
import trabajotfg.reservas.dto.InventarioDto;
import trabajotfg.reservas.dto.OrderDto;
import trabajotfg.reservas.dto.ReservasDto;
import trabajotfg.reservas.dto.ResponseDTO;
import trabajotfg.reservas.dto.TransaccionMsgDto;
import trabajotfg.reservas.entity.Pagos;
import trabajotfg.reservas.entity.Reservas;
import trabajotfg.reservas.mapped.ReservasMapped;
import trabajotfg.reservas.repository.PagosRepository;
import trabajotfg.reservas.repository.ReservasRepository;
import trabajotfg.reservas.service.clients.GpsClient;
import trabajotfg.reservas.service.clients.InventarioClient;
import trabajotfg.reservas.service.clients.PagoClient;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@Service
@AllArgsConstructor
public class ReservasServiceImpl  implements ReservasService{

    private ReservasRepository reservasRepository;
    private PagosRepository pagoRepository;
    
    //Clientes eureka
    private InventarioClient inventarioClient;
    private PagoClient pagoClient;
    private GpsClient gpsClient;

    //Bridge
    @Autowired
    private StreamBridge streamBridge;

    
    private static final Logger log = LoggerFactory.getLogger(ReservasServiceImpl.class);


    @Override
    public String crearReserva(ReservasDto reserva) {
        // TODO Auto-generated method stub
        //primero llamar al inventario para modificar el estado
        ResponseEntity<ResponseDTO> inventarioestado=inventarioClient.actualizarEstado(reserva.getIdvehiculo(), "RESERVADO");

        //guardar la reserva
        Reservas reserva_insertada=reservasRepository.save(ReservasMapped.convertToEntity(reserva, new Reservas()));

        //creao la subscripcion del coche reservado
        //comprobar si la fecha fin esta no vacia
        String respuesta=gpsClient.crearSubscripcion(reserva.getMatricula(), reserva.getFechaFin());


        Pagos pago= new Pagos();

        pago.setReserva(reserva_insertada);
        pago.setCantidad(reserva.getPrecio());
        pago.setFecha(new Date());
        pago.setEstado("PENDIENTE");
        pago.setMetodoPago("PAYPAL");

        pagoRepository.save(pago);

        //llamar al pago para realizar el pago
        String url= pagoClient.createPayment(new OrderDto(
            reserva.getEmailcliente(), 
            reserva_insertada.getIdreserva(), 
            reserva.getPrecio(), "EUR", "paypal", "sale",
             "Reserva del vehiculo"+ reserva.getMarca() + "el dia" + reserva.getFechaInicio()));

        return url;
        
    }


    @Override
    public String cancelarReserva(int id) {
        // verificar si la cancelacion antes de las 24 horas de el inicio de la reserva
        Optional<Reservas> reserva=reservasRepository.findById(id);
        boolean condicion_24horas=false;
        double cantidad_reembolso=0;
        if(reserva.isEmpty()){
            throw new RuntimeException("Reserva no encontrada con ID: " + id);
        }
        
        condicion_24horas=comprobarFechas(reserva.get().getFecha_inicio(), reserva.get().getHora_inicio());
        ResponseEntity<ResponseDTO> inventarioestado=inventarioClient.actualizarEstado(reserva.get().getIdVehiculo(), "DISPONIBLE");
        
        //obtener la cantidad monetaria asociada a la reserva
        Optional<Pagos> pago=pagoRepository.findByReserva(reserva.get());
        cantidad_reembolso=pago.get().getCantidad();
        //eliminamos la resaerva
        reservasRepository.deleteById(id);
        
        if (condicion_24horas) {
            // si es antes de 24 horas se devuelve el 100% del total
            // se llama a Pago con el 100%
            
            pagoClient.cancelarReserva(id, Double.toString(cantidad_reembolso));
            return ReservasConstant.RESERVA_CANCELADA_REMBOLSO_100;
        }
        else
        {
            // si es despues de 24 horas se cobra el 50% del total
            // se llama a pago con el 50%
            pagoClient.cancelarReserva(id, Double.toString(cantidad_reembolso*0.5));
            return ReservasConstant.RESERVA_CANCELADA_REMBOLSO_50;
        }

    }

    @Override
    public List<ReservasDto> obtenerReservas() {
        // TODO Auto-generated method stub
        List<ReservasDto> reservasDTO= new ArrayList<>();
        List<Reservas> reservas= reservasRepository.findAll();
        for (Reservas reserva : reservas) {
            reservasDTO.add(ReservasMapped.convertToDTO(reserva, new ReservasDto()));
        }

        return reservasDTO;

    }

    @Override
    public List<ReservasDto> obtenerReservasCompletadas(String fecha,String email) {
        List<ReservasDto> reservasDTO = new ArrayList<>();
        try {
            Date nuevaFecha = new SimpleDateFormat("yyyy-MM-dd").parse(fecha);
            List<Reservas> reservas = reservasRepository.findByFechaCompletadas(nuevaFecha,email);
            for (Reservas reserva : reservas) {
                reservasDTO.add(ReservasMapped.convertToDTO(reserva, new ReservasDto()));
            }
        } catch (ParseException e) {
            e.printStackTrace();
            // Handle the exception, maybe log it or throw a custom exception
        }
        return reservasDTO;
    }


    @Override
    public List<ReservasDto> obtenerReservasActivas(String fecha,String email) {
        List<ReservasDto> reservasDTO = new ArrayList<>();
        try {
            Date nuevaFecha = new SimpleDateFormat("yyyy-MM-dd").parse(fecha);
            List<Reservas> reservas = reservasRepository.findByFechaActivas(nuevaFecha,email);
            for (Reservas reserva : reservas) {
                ReservasDto reservaDTO = ReservasMapped.convertToDTO(reserva, new ReservasDto());
                ResponseEntity<InventarioDto> coche_reservado=inventarioClient.obtenerCochePorId(reserva.getIdVehiculo());
                Pagos pago=pagoRepository.findByReserva(reserva).get();
                reservaDTO.setPrecio(pago.getCantidad());
                reservaDTO.setMatricula(coche_reservado.getBody().getMatricula());
                reservaDTO.setMarca(coche_reservado.getBody().getMarca());
                reservasDTO.add(reservaDTO);
                
            }
        } catch (ParseException e) {
            e.printStackTrace();
            // Handle the exception, maybe log it or throw a custom exception
        }
        return reservasDTO;
    }


    @Override
    public List<ReservasDto> obtenerReservasPorEmail(String email) {
        // TODO Auto-generated method stub
        List<ReservasDto> reservasDTO= new ArrayList<>();
        List<Reservas> reservas= reservasRepository.findByEmailcliente(email);
        for (Reservas reserva : reservas) {
            reservasDTO.add(ReservasMapped.convertToDTO(reserva, new ReservasDto()));
        }
        return reservasDTO;
    }


    @Override
    public void actualizarEstado(int idReserva, String estado) {
        // TODO Auto-generated method stub
        Reservas reserva = reservasRepository.findById(idReserva).orElseThrow(()-> new RuntimeException("Reserva no encontrada"));
        reserva.setEstado(estado);
        reservasRepository.save(reserva);
    }


    @Override
    public boolean comprobarFechas(Date fecha_inicio, Time hora_inicio) {
        // TODO Auto-generated method stub
        
        // 1. Obtener fecha_inicio y hora_inicio de la reserva
            LocalDate fechaInicio = fecha_inicio.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            
            LocalTime horaInicio = hora_inicio.toLocalTime();

            // 2. Combinar fecha y hora en un LocalDateTime
            LocalDateTime fechaHoraEvento = LocalDateTime.of(fechaInicio, horaInicio);

            // 3. Obtener la fecha y hora actuales
            LocalDateTime ahora = LocalDateTime.now();

            // 4. Calcular la diferencia en horas entre la fecha de inicio de la reserva y la actual
            long horasDiferencia = ChronoUnit.HOURS.between(ahora, fechaHoraEvento);

            if (horasDiferencia >= 24) {
                //se devuelve el 100% del dinero
                return true;
            } else {
                //se devuelve el 50% del dinero
                return false;
            }

        
    }


    @Override
    public void eliminarReserva(int idReserva) {
        // TODO Auto-generated method stub
        //Tenemos que obtener el identificador el vehiculo para ponerlo en estado disponible
        Optional<Reservas> reserva=reservasRepository.findById(idReserva);
        var result= streamBridge.send("sendInventarioConfirmation-out-0", new TransaccionMsgDto(0, reserva.get().getIdVehiculo(), "Disponible"));
        //antes de eliminarla la reserva tenemos que llamar a pago para hacer el reembolso
        Optional<Pagos> pago=pagoRepository.findByReserva(reserva.get());
        
        String respuesta=pagoClient.cancelarReserva(idReserva, Double.toString(pago.get().getCantidad()));

        log.trace("Cancelar respiesta", respuesta);



        reservasRepository.deleteById(idReserva);

    }


    @Override
    public void actualizarEstadoPago(int idReserva, String estado) {
        // TODO Auto-generated method stub
        //primero obtener el pago asociado a la reserva
        Optional<Reservas> reserva=reservasRepository.findById(idReserva);
        Pagos pago=pagoRepository.findByReserva(reserva.get()).get();
        pago.setEstado(estado);
        pagoRepository.save(pago);
    }


    @Override
    public int obtenerReservaPorIdVehiclo(int idVehiculo) {
        // TODO Auto-generated method stub
        Reservas reserva=reservasRepository.findByIdVehiculo(idVehiculo);

        return reserva.getIdreserva();
    }


    @Override
    public Reservas obtenerReservaPorIdVehiculo(int id) {
        // TODO Auto-generated method stub
        return reservasRepository.findByIdVehiculo(id);
    }


    @Override
    public Map<Integer, Integer> obtenerEstadisticasPorCliente(String email) {
        // TODO Auto-generated method stub
            List<Object[]> results = reservasRepository.findVehiclesRentedByUserPerMonth(email);
            Map<Integer, Integer> rentalsPerMonth = new HashMap<>();

            for (Object[] result : results) {
                Integer month = ((Number) result[0]).intValue(); // Mes
                Integer vehicleCount = ((Number) result[1]).intValue(); // Número de vehículos alquilados
                rentalsPerMonth.put(month, vehicleCount);
            }

            return rentalsPerMonth;
        }
    
    

}
