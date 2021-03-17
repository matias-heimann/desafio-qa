package com.meli.desafioqa.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HotelsFromJson {
    @JsonProperty("Código Hotel")
    private String id;
    @JsonProperty("Nombre")
    private String name;
    @JsonProperty("Lugar/Ciudad")
    private String city;
    @JsonProperty("Tipo de Habitación")
    private String roomType;
    @JsonProperty("Precio por noche")
    private String price;
    @JsonProperty("Disponible Desde")
    private String availableSince;
    @JsonProperty("Disponible hasta")
    private String availableUntil;
    @JsonProperty("Reservado")
    private String booked;

    public HotelsFromJson(){};

}
