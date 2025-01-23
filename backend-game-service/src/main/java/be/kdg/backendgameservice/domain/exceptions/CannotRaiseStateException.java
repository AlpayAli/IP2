package be.kdg.backendgameservice.domain.exceptions;

public class CannotRaiseStateException extends IllegalStateException {
    public CannotRaiseStateException(String message) {
        super(message);
    }
}
