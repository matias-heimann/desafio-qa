package com.meli.desafioqa.model;

import com.meli.desafioqa.model.dto.FlightReservationDTO;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter @AllArgsConstructor @EqualsAndHashCode
public class FlightReservationRequest {

    @NotBlank(message = "El username es obligatorio") @Email(message = "El username debe ser un email")
    private String userName;
    @NotNull(message = "Ingrese reserva valida") @Valid
    private FlightReservationDTO flightReservation;

    public FlightReservationRequest(){
    }

}
