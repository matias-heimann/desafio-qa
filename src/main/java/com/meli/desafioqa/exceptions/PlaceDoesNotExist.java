package com.meli.desafioqa.exceptions;

import org.springframework.http.HttpStatus;

public class PlaceDoesNotExist extends BaseException{

    public PlaceDoesNotExist(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
