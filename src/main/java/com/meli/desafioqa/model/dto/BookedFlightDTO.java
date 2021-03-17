package com.meli.desafioqa.model.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @EqualsAndHashCode @AllArgsConstructor
public class BookedFlightDTO {

    private String userName;
    private Double amount;
    private Double interest;
    private Double total;
    private FlightReservationDTO flightReservationDTO;
    private StatusDTO status;
}
