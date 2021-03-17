package com.meli.desafioqa.model.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter @Setter @AllArgsConstructor @EqualsAndHashCode
public class StatusDTO {

    private HttpStatus status;
    private String message;

}
