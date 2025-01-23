package be.kdg.backendgameservice.event;

import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class MailEventListener {
    private final JavaMailSender mailSender;
    private final Logger _logger = Logger.getLogger(MailEventListener.class.getName());

    public MailEventListener(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @EventListener
    @Async
    public void onMailEvent(MailEvent mailEvent) {
        _logger.info("\n\n\n\n\nSending email to \n\n\n\n " + mailEvent.getEmail());
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("kdgpoker@gmail.com\n");
        mailMessage.setTo(mailEvent.getEmail());
        mailMessage.setSubject(mailEvent.getSubject());
        mailMessage.setText(mailEvent.getText());
        try {
            mailSender.send(mailMessage);
            _logger.info("Email sent to " + mailEvent.getEmail());
        } catch (Exception e) {
            _logger.severe("An error occurred while trying to send an email to " + mailEvent.getEmail());
        }
    }
}
