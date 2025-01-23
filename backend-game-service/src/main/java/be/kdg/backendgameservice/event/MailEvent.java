package be.kdg.backendgameservice.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MailEvent extends ApplicationEvent {
    private final String email;
    private final String subject;
    private final String text;

    public MailEvent(Object source, String email, String subject, String text) {
        super(source);
        this.email = email;
        this.subject = subject;
        this.text = text;
    }
}
