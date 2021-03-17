package com.meli.desafioqa.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter @AllArgsConstructor @EqualsAndHashCode
public class PaymentMethod {

    @NotBlank(message = "Ingrese tipo de pago valido")
    private String type;
    @NotBlank(message = "Ingrese numero de tarjeta valido")
    private String number;
    @NotNull(message = "Ingrese numero de cuotas")
    private Integer dues;

    public PaymentMethod(){}

}
