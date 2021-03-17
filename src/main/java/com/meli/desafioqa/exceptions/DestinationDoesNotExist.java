package com.meli.desafioqa.exceptions;

import org.springframework.http.HttpStatus;

public class DestinationDoesNotExist extends BaseException{

    public DestinationDoesNotExist(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
