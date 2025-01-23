package be.kdg.backendgameservice.domain.exceptions;

public class CannotCallException extends IllegalStateException {
    public CannotCallException(String message) {
        super(message);
    }
}
