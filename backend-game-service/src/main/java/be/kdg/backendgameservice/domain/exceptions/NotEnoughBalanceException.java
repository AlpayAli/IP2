package be.kdg.backendgameservice.domain.exceptions;

public class NotEnoughBalanceException extends IllegalStateException {
    public NotEnoughBalanceException(String message) {
        super(message);
    }
}
