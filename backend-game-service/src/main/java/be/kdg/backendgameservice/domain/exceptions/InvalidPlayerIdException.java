package be.kdg.backendgameservice.domain.exceptions;

public class InvalidPlayerIdException extends RuntimeException {
    public InvalidPlayerIdException(String message) {
        super(message);
    }
}
