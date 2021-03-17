package com.meli.desafioqa.exceptions;

import org.springframework.http.HttpStatus;

public class NotValidDuesNumber extends BaseException{
    public NotValidDuesNumber(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
