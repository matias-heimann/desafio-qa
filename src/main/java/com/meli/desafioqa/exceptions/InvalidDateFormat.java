package com.meli.desafioqa.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidDateFormat extends BaseException{
    public InvalidDateFormat(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
