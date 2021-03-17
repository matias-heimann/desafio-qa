package com.meli.desafioqa.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class FlightFromJson {

    @JsonProperty("Nro Vuelo")
    private String flightNumber;
    @JsonProperty("Origen")
    private String origin;
    @JsonProperty("Destino")
    private String destination;
    @JsonProperty("Tipo Asiento")
    private String seatType;
    @JsonProperty("Precio por persona")
    private String price;
    @JsonProperty("Fecha ida")
    private String dateFrom;
    @JsonProperty("Fecha Vuelta")
    private String dateTo;

    public FlightFromJson(){}
}
