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

    @NotBlank(message = "Ingrese dni valido")
    private String dni;
    @NotBlank(message = "Ingrese nombre valido")
    private String name;
    @NotBlank(message = "Ingrese apellido valido")
    private String lastName;
    @NotBlank(message = "Ingrese fecha de nacimiento valida")
    private String birthDate;
    @NotBlank(message = "Por favor ingrese un email válido") @Email(message = "Por favor ingrese un email válido")
    private String mail;

    public PersonDTO(){}
}
