package com.meli.desafioqa.services;

import com.meli.desafioqa.exceptions.InvalidDateFormat;
import com.meli.desafioqa.exceptions.NotFoundException;
import com.meli.desafioqa.exceptions.NotValidFilterException;
import com.meli.desafioqa.exceptions.PlaceDoesNotExist;
import com.meli.desafioqa.model.dto.FlightDTO;
import com.meli.desafioqa.model.dto.FlightReservationDTO;

import java.util.List;

public interface FlightService {

    public List<FlightDTO> getFlights(String origin, String destination, String dateFrom, String dateTo) throws NotValidFilterException, InvalidDateFormat, NotFoundException, PlaceDoesNotExist;
    public FlightReservationDTO bookFlight();
}
