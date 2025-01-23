package be.kdg.backendgameservice.domain.exceptions;

public class NotEnoughXpException extends RuntimeException {
    public NotEnoughXpException(String message) {
        super(message);
    }
}
