package com.meli.desafioqa.exceptions;

import org.springframework.http.HttpStatus;

public class NotValidFilterException extends BaseException{

    public NotValidFilterException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
