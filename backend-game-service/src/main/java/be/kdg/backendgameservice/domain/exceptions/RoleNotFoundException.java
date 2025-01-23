package be.kdg.backendgameservice.domain.exceptions;

public class RoleNotFoundException extends IllegalStateException {
    public RoleNotFoundException(String message) {
        super(message);
    }
}
