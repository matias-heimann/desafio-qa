package com.meli.desafioqa.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundHotelsException extends BaseException{

    public NotFoundHotelsException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
