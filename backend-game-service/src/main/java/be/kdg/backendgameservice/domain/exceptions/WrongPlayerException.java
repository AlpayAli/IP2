package be.kdg.backendgameservice.domain.exceptions;

public class WrongPlayerException extends IllegalStateException {
    public WrongPlayerException(String message) {
        super(message);
    }
}
