package com.meli.desafioqa.exceptions;

import org.springframework.http.HttpStatus;

public class NotValidHotelFilterException extends BaseException{

    public NotValidHotelFilterException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
