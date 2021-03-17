package com.meli.desafioqa.repositories;

import com.meli.desafioqa.exceptions.NotFoundException;
import com.meli.desafioqa.exceptions.NotValidFilterException;
import com.meli.desafioqa.exceptions.PlaceDoesNotExist;
import com.meli.desafioqa.model.FlightDao;

import java.time.LocalDate;
import java.util.List;

public interface FlightRepository {

    public List<FlightDao> getAll();
    public List<FlightDao> getFlights(String origin, String destination, LocalDate dateFrom, LocalDate dateTo) throws PlaceDoesNotExist, NotFoundException, NotValidFilterException;
}
