package com.meli.desafioqa.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidPriceFormat extends BaseException{

    public InvalidPriceFormat(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
