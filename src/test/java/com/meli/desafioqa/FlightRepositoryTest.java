package com.meli.desafioqa;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meli.desafioqa.exceptions.InvalidPriceFormat;
import com.meli.desafioqa.exceptions.NotFoundException;
import com.meli.desafioqa.exceptions.NotValidFilterException;
import com.meli.desafioqa.exceptions.PlaceDoesNotExist;
import com.meli.desafioqa.model.FlightDao;
import com.meli.desafioqa.repositories.FlightRepository;
import com.meli.desafioqa.repositories.impl.FlightRepositoryImpl;
import com.meli.desafioqa.utils.FlightFromJson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@WebMvcTest(FlightRepository.class)
public class FlightRepositoryTest {

    private List<FlightDao> flightDaos;
    private List<FlightDao> filteredFlightDaos;
    private FlightRepository flightRepository;

    @BeforeEach
    private void beforeEach() throws IOException, InvalidPriceFormat {
        this.flightRepository = new FlightRepositoryImpl("src/test/resources/Vuelos.json");

        this.flightDaos = new LinkedList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        List<FlightFromJson> flightFromJsons = objectMapper.readValue(new File("src/test/resources/Vuelos.json"), new TypeReference<>() {});
        for(FlightFromJson f: flightFromJsons) this.flightDaos.add(new FlightDao(f));

        this.filteredFlightDaos = this.flightDaos.stream().filter(f -> f.getFlightNumber().equals("BAPI-1235")).collect(Collectors.toList());
    }

    @Test
    public void testGetAll(){
        Assertions.assertIterableEquals(this.flightDaos, this.flightRepository.getAll());
    }

    @Test
    public void testGetFilteredFlights() throws PlaceDoesNotExist, NotFoundException, NotValidFilterException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Assertions.assertIterableEquals(this.filteredFlightDaos, this.flightRepository.getFlights("Buenos Aires",
                "Puerto Iguazú", LocalDate.parse("10/02/2021", dtf), LocalDate.parse("15/02/2021", dtf)));
    }


}
