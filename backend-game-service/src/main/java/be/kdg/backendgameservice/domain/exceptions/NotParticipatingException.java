package be.kdg.backendgameservice.domain.exceptions;

public class NotParticipatingException extends IllegalArgumentException {
    public NotParticipatingException(String message) {
        super(message);
    }
}
