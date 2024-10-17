package trabajotfg.mensajes.services;

import java.util.Map;

import jakarta.mail.MessagingException;

public interface EmailSenderService {

    void sendEmail(String to, String subject, Map<String, Object> templateModel) throws MessagingException;

}
