package be.kdg.backendgameservice.domain.exceptions;

import org.springframework.dao.DataIntegrityViolationException;

public class PlayerAlreadyExistsException extends RuntimeException {
    public PlayerAlreadyExistsException(String message, DataIntegrityViolationException e) {
        super(message, e);
    }
}
