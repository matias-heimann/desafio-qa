package com.meli.desafioqa.repositories;

import com.meli.desafioqa.exceptions.NotFoundException;
import com.meli.desafioqa.exceptions.NotValidFilterException;
import com.meli.desafioqa.exceptions.PlaceDoesNotExist;
import com.meli.desafioqa.model.FlightDao;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public interface FlightRepository {

    public List<FlightDao> getAll();
    public List<FlightDao> getFlights(@NotNull String origin, @NotNull String destination, @NotNull LocalDate dateFrom, @NotNull LocalDate dateTo) throws PlaceDoesNotExist, NotFoundException, NotValidFilterException;
}
