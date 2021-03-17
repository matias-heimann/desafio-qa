package com.meli.desafioqa.model.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter @AllArgsConstructor @EqualsAndHashCode
public class PaymentMethodDTO {

    @NotNull(message = "Ingrese tipo de pago valido") @NotBlank(message = "Ingrese tipo de pago valido")
    private String type;
    @NotNull(message = "Ingrese numero de tarjeta valido") @NotBlank(message = "Ingrese numero de tarjeta valido")
    private String number;
    @NotNull(message = "Ingrese numero de cuotas")
    private Integer dues;

    public PaymentMethodDTO(){}

}
