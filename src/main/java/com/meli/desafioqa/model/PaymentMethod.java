package com.meli.desafioqa.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter @AllArgsConstructor @EqualsAndHashCode
public class PaymentMethod {

    @NotBlank(message = "Ingrese tipo de pago valido")
    private String type;
    @NotBlank(message = "Ingrese numero de tarjeta valido")
    private String number;
    @Min(value = 1, message = "Minimo debe haber una cuota")
    @Max(value = 12, message = "Maximo son 12 cuotas")
    @NotNull(message = "Ingrese numero de cuotas")
    private Integer dues;

    public PaymentMethod(){}

}
