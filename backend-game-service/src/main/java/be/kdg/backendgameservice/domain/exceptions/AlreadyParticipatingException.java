package be.kdg.backendgameservice.domain.exceptions;

public class AlreadyParticipatingException extends RuntimeException {
    public AlreadyParticipatingException(String message) {
        super(message);
    }
}
