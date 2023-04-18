package br.com.vivo.challengeform.exceptions.resources;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ResourceBadRequestException extends RuntimeException{
    public ResourceBadRequestException(String message){
        super(message);
    }
}
