package be.kdg.backendgameservice.domain.exceptions;

public class BrokeBoyException extends IllegalArgumentException {
    public BrokeBoyException (String message) {
        super(message);
    }
}
