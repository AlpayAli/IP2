package be.kdg.backendgameservice.domain.exceptions;

public class WrongInviteException extends IllegalArgumentException {
    public WrongInviteException(String message) {
        super(message);
    }
}
