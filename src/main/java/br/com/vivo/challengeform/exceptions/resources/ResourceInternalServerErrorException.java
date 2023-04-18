package br.com.vivo.challengeform.exceptions.resources;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class ResourceInternalServerErrorException extends RuntimeException{
    public ResourceInternalServerErrorException(String message){
        super(message);
    }
}
