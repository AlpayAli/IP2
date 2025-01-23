package be.kdg.backendgameservice.domain.exceptions;

public class NotAbleToCheckException extends IllegalStateException {
    public NotAbleToCheckException(String message) {
        super(message);
    }
}
