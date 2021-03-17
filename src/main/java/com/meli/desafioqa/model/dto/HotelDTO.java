package com.meli.desafioqa.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter @AllArgsConstructor
public class HotelDTO {

    private String hotelCode;
    private String name;
    private String city;
    private String roomType;
    private Integer price;
    private String  availableSince;
    private String availableUntil;
    private Boolean booked;

}
