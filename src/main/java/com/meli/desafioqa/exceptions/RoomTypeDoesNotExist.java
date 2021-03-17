package com.meli.desafioqa.exceptions;

import org.springframework.http.HttpStatus;

public class RoomTypeDoesNotExist extends BaseException{
    public RoomTypeDoesNotExist(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
