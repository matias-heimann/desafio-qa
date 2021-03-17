package com.meli.desafioqa.model.dto;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@Getter @Setter @AllArgsConstructor @EqualsAndHashCode @ToString
public class FlightReservationDTO {

    @NotNull(message = "La fecha de ida es obligatoria")
    private String dateFrom;
    @NotNull(message = "La fecha de vuelta es obligatoria")
    private String dateTo;
    @NotBlank(message = "El origen es obligatorio")
    private String origin;
    @NotBlank(message = "El destino es obligatorio")
    private String destination;
    @NotBlank(message = "El numero de vuelo es obligatoria")
    private String flightNumber;
    @NotNull(message = "El numero de asientos es obligatorio")
    @Min(value = 1, message = "Debe haber al menos un asiento seleccionado")
    private Integer seats;
    @NotBlank(message = "El tipo de asiento es obligatorio")
    private String seatType;
    @NotEmpty(message = "La lista de personas no debe ser vacia") @Valid
    private List<PersonDTO> people;

}
