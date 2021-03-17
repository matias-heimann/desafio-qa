package com.meli.desafioqa.model;

import com.meli.desafioqa.model.dto.BookingDTO;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter @AllArgsConstructor @EqualsAndHashCode @ToString
public class HotelReservationRequest {

    @NotBlank(message = "El username es obligatorio") @Email(message = "El username debe ser un email")
    private String userName;
    @NotNull(message = "Ingrese reserva valida") @Valid
    private BookingDTO booking;

    public HotelReservationRequest(){
    }

}
