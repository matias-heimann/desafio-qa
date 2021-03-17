package com.meli.desafioqa.model;

import com.meli.desafioqa.model.dto.BookingDTO;
import com.meli.desafioqa.model.dto.PaymentMethodDTO;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter @AllArgsConstructor @EqualsAndHashCode @ToString
public class HotelReservationRequest {

    @NotNull(message = "Ingrese nombre de usuario") @NotBlank(message = "Ingrese nombre de usuario") @Email(message = "Nombre de usuario debe ser un mail")
    private String userName;
    @NotNull(message = "Ingrese reserva valida") @Valid
    private BookingDTO booking;
    @NotNull(message = "Ingrese metodo de pago valido") @Valid
    private PaymentMethodDTO paymentMethod;

    public HotelReservationRequest(){

    }

}
