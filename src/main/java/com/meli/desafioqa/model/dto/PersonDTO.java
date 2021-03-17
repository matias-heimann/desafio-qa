package com.meli.desafioqa.model.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter @AllArgsConstructor @EqualsAndHashCode
public class PersonDTO {

    @NotNull(message = "Ingrese dni valido") @NotBlank(message = "Ingrese dni valido")
    private String dni;
    @NotNull(message = "Ingrese nombre valido") @NotBlank(message = "Ingrese nombre valido")
    private String name;
    @NotNull(message = "Ingrese apellido valido") @NotBlank(message = "Ingrese apellido valido")
    private String lastName;
    @NotNull(message = "Ingrese fecha de nacimiento valida") @NotBlank(message = "Ingrese fecha de nacimiento valida")
    private String birthDate;
    @NotNull(message = "Por favor ingrese un e-mail válido") @NotBlank(message = "Por favor ingrese un e-mail válido") @Email(message = "Por favor ingrese un e-mail válido")
    private String mail;

    public PersonDTO(){}
}
