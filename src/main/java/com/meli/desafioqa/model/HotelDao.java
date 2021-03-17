package com.meli.desafioqa.model;

import com.meli.desafioqa.exceptions.InvalidPriceFormat;
import com.meli.desafioqa.utils.HotelsFromJson;
import com.meli.desafioqa.utils.UtilPriceToInt;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter @Setter
public class HotelDao {

    private String id;
    private String name;
    private String city;
    private Integer roomMaxCapacity;
    private Integer price;
    private LocalDate availableSince;
    private LocalDate availableUntil;
    private Boolean booked;

    public HotelDao(HotelsFromJson hotel) throws InvalidPriceFormat {
        this.id = hotel.getId();
        this.name = hotel.getName();
        this.city = hotel.getCity();
        this.booked = !hotel.getBooked().equals("NO");
        this.price = UtilPriceToInt.priceToInt(hotel.getPrice());
        if(hotel.getRoomType().equals("Single")){
            this.roomMaxCapacity = 1;
        }
        else if(hotel.getRoomType().equals("Doble")){
            this.roomMaxCapacity = 2;
        }
        else if(hotel.getRoomType().equals("Triple")){
            this.roomMaxCapacity = 3;
        }
        else {
            this.roomMaxCapacity = 10;
        }

        DateTimeFormatter simpleDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.availableSince = LocalDate.parse(hotel.getAvailableSince(), simpleDateFormat);
        this.availableUntil = LocalDate.parse(hotel.getAvailableUntil(), simpleDateFormat);

    }

}
