package com.meli.desafioqa.model.dto;

import lombok.*;

@Getter @Setter @EqualsAndHashCode @AllArgsConstructor @ToString
public class BookedFlightDTO {

    private String userName;
    private Double amount;
    private Double interest;
    private Double total;
    private FlightReservationDTO flightReservationDTO;
    private StatusDTO status;
}
