package com.meli.desafioqa.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.meli.desafioqa.exceptions.InvalidPriceFormat;
import com.meli.desafioqa.utils.FlightFromJson;
import com.meli.desafioqa.utils.UtilPriceToInt;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter @Setter @AllArgsConstructor @EqualsAndHashCode
public class FlightDao {

    private String flightNumber;
    private String origin;
    private String destination;
    private String seatType;
    private Integer price;
    private LocalDate dateFrom;
    private LocalDate dateTo;

    public FlightDao(FlightFromJson flight) throws InvalidPriceFormat {
        this.flightNumber = flight.getFlightNumber();
        this.origin = flight.getOrigin();
        this.destination = flight.getDestination();
        this.seatType = flight.getSeatType();
        this.price = UtilPriceToInt.priceToIntWithDots(flight.getPrice());
        DateTimeFormatter simpleDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.dateFrom = LocalDate.parse(flight.getDateFrom(), simpleDateFormat);
        this.dateTo = LocalDate.parse(flight.getDateTo(), simpleDateFormat);
    }

}
