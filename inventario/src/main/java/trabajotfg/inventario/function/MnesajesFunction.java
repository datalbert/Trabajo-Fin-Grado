package trabajotfg.inventario.function;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import trabajotfg.inventario.dto.TransaccionMsgDto;
import trabajotfg.inventario.services.InventarioService;

@Configuration
public class MnesajesFunction {

    private InventarioService inventarioService;

    public MnesajesFunction(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }


    @Bean
    public Consumer<TransaccionMsgDto> sendInventarioConfirmation() {

        return transaccion -> {
            try {
                //evaluar si el estado es Disponible actualizar el inventario
                if (transaccion.estado().equals("Disponible")){
                    // actualizamos el inventario
                    inventarioService.modificarEstado(transaccion.id_coche(), "DISPONIBLE");
                    System.out.println("Actualizando inventario");
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        
    }



}
