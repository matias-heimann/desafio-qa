package com.meli.desafioqa.services;

import com.meli.desafioqa.exceptions.*;
import com.meli.desafioqa.model.FlightReservationRequest;
import com.meli.desafioqa.model.dto.BookedFlightDTO;
import com.meli.desafioqa.model.dto.FlightDTO;
import com.meli.desafioqa.model.dto.FlightReservationDTO;

import java.util.List;

public interface FlightService {

    public List<FlightDTO> getFlights(String origin, String destination, String dateFrom, String dateTo) throws NotValidFilterException, InvalidDateFormat, NotFoundException, PlaceDoesNotExist;
    public BookedFlightDTO bookFlight(FlightReservationRequest flightReservationRequest) throws InvalidReservationException, InvalidDateFormat, PlaceDoesNotExist, NotFoundException, NotValidFilterException, NotValidDuesNumber;
}
