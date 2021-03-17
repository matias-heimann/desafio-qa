package com.meli.desafioqa.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidReservationException extends BaseException{
    public InvalidReservationException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
