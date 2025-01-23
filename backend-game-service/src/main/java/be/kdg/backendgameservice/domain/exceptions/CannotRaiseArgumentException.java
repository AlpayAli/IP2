package be.kdg.backendgameservice.domain.exceptions;

public class CannotRaiseArgumentException extends IllegalArgumentException {
    public CannotRaiseArgumentException(String message) {
        super(message);
    }
}
