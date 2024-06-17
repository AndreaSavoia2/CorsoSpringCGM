package it.cgmconsulting.mscomment.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GenericException extends RuntimeException{

    private final String msg;
    private final HttpStatus status;
    public GenericException(String msg, HttpStatus status) {
        super(msg);
        this.msg = msg;
        this.status = status;
    }
}
