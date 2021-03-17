package com.meli.desafioqa.repositories.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meli.desafioqa.exceptions.InvalidPriceFormat;
import com.meli.desafioqa.model.FlightDao;
import com.meli.desafioqa.repositories.FlightRepository;
import com.meli.desafioqa.utils.FlightFromJson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

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
}
