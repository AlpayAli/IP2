package be.kdg.backendgameservice.domain.exceptions;

public class ChatBotException extends RuntimeException {
    public ChatBotException(String message, Exception e) {
        super(message, e);
    }
}
