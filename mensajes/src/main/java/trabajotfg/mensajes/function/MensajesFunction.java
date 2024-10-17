package trabajotfg.mensajes.function;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import trabajotfg.mensajes.dto.ReservasMsgDto;
import trabajotfg.mensajes.services.EmailSenderService;

@Configuration
public class MensajesFunction {

    private final EmailSenderService emailSenderService;

    
    public MensajesFunction(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    @Bean
    public Consumer<ReservasMsgDto> sendEmail() {
        return email -> {
            try {
                Map<String, Object> templateModel = new HashMap<>();
		        templateModel.put("reserva", email);
                emailSenderService.sendEmail(email.email()," Reserva de coche"+ email.Coche(), templateModel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }


}
