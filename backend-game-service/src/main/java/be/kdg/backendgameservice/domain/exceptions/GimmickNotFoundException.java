package be.kdg.backendgameservice.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class GimmickNotFoundException extends RuntimeException {
    public GimmickNotFoundException(String message) {
        super(message);
    }
}
