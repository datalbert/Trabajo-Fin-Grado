package trabajotfg.mensajes.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SpringTemplateEngine thymeleafTemplateEngine;

    public void sendEmail(String to, String subject, Map<String, Object> templateModel) throws MessagingException {
        
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("carsrental10@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        
        // Create a Thymeleaf context
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);

        // Create the HTML body using Thymeleaf
        String htmlBody = thymeleafTemplateEngine.process("reservas-template", thymeleafContext);
        helper.setText(htmlBody, true);
        
        javaMailSender.send(message);
    }


}

