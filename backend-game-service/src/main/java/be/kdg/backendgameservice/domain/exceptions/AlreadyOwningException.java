package be.kdg.backendgameservice.domain.exceptions;

public class AlreadyOwningException extends IllegalStateException {
    public AlreadyOwningException(String message) {
        super(message);
    }
}
