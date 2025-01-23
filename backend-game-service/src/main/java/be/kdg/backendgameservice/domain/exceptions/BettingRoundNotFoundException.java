package be.kdg.backendgameservice.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BettingRoundNotFoundException extends RuntimeException {
    public BettingRoundNotFoundException(String message) {super(message);}
}
