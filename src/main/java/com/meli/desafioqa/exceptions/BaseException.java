package com.meli.desafioqa.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter @AllArgsConstructor
public abstract class BaseException extends Exception {

    private HttpStatus status;
    private String message;

}
