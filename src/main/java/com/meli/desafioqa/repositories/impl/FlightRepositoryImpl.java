package com.meli.desafioqa.repositories.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meli.desafioqa.exceptions.NotFoundException;
import com.meli.desafioqa.exceptions.NotValidFilterException;
import com.meli.desafioqa.exceptions.PlaceDoesNotExist;
import com.meli.desafioqa.exceptions.InvalidPriceFormat;
import com.meli.desafioqa.model.FlightDao;
import com.meli.desafioqa.repositories.FlightRepository;
import com.meli.desafioqa.utils.FlightFromJson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Repository
public class FlightRepositoryImpl implements FlightRepository {

    @Value("${flights-json}")
    private String filename;

    private HashMap<String, FlightDao> flights;

    public FlightRepositoryImpl(){}

    @PostConstruct
    public void postConstruct() throws IOException, InvalidPriceFormat {
        this.flights = new HashMap<>();

        ObjectMapper objectMapper = new ObjectMapper();
        List<FlightFromJson> flightFromJsons = objectMapper.readValue(new File(this.filename), new TypeReference<>() {});
        for(FlightFromJson f: flightFromJsons) this.flights.put(f.getFlightNumber(), new FlightDao(f));
    }

    @Override
    public List<FlightDao> getAll() {
        return this.flights.values().stream().collect(Collectors.toList());
    }

    @Override
    public List<FlightDao> getFlights(String origin, String destination, LocalDate dateFrom, LocalDate dateTo) throws PlaceDoesNotExist, NotFoundException, NotValidFilterException {
        if(origin == null || destination == null || dateFrom == null || dateTo == null) throw new NotValidFilterException("Not valid parameters");

        List<FlightDao> filteredByOrigin = this.flights.values().stream()
                .filter(f -> f.getOrigin().toLowerCase(Locale.ROOT).equals(origin.toLowerCase(Locale.ROOT)))
                .collect(Collectors.toList());
        if(filteredByOrigin.isEmpty()) throw new PlaceDoesNotExist("El origen elegido no existe");

        List<FlightDao> filteredByDestination = this.flights.values().stream()
                .filter(f -> f.getDestination().toLowerCase(Locale.ROOT).equals(destination.toLowerCase(Locale.ROOT)))
                .collect(Collectors.toList());
        if(filteredByDestination.isEmpty()) throw new PlaceDoesNotExist("El destinto elegido no existe");

        filteredByOrigin = filteredByOrigin.stream()
                .filter(f -> f.getDestination().toLowerCase(Locale.ROOT).equals(destination.toLowerCase(Locale.ROOT)))
                .collect(Collectors.toList());
        if(filteredByOrigin.isEmpty()) throw new PlaceDoesNotExist("El vuelo seleccionado no existe");

        filteredByOrigin =  filteredByOrigin.stream()
                .filter(f -> f.getDateFrom().equals(dateFrom) && f.getDateTo().equals(dateTo))
                .collect(Collectors.toList());
        if(filteredByOrigin.isEmpty()) throw new NotFoundException("No se encontraron vuelos entre ese origen y ese destino en esas fechas");

        return filteredByOrigin;
    }

}
