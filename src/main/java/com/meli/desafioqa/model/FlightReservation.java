package com.meli.desafioqa.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Getter @Setter @AllArgsConstructor @EqualsAndHashCode
public class FlightReservation {


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date date;

    public FlightReservation(){}
}
