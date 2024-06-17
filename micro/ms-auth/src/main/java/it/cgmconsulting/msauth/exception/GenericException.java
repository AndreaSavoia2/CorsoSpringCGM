package it.cgmconsulting.msauth.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
public class GenericException extends  RuntimeException{

    private String msg;
    private HttpStatus httpStatus;
    public GenericException(String msg , HttpStatus httpStatus) {
        super(msg);
        this.msg = msg;
        this.httpStatus = httpStatus;
    }


}
