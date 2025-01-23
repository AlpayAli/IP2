package be.kdg.backendgameservice.domain.exceptions;

public class NotFriendsException extends IllegalArgumentException {
    public NotFriendsException(String message) {
        super(message);
    }
}
