package com.meli.desafioqa;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meli.desafioqa.exceptions.InvalidPriceFormat;
import com.meli.desafioqa.exceptions.NotFoundException;
import com.meli.desafioqa.exceptions.NotValidFilterException;
import com.meli.desafioqa.exceptions.PlaceDoesNotExist;
import com.meli.desafioqa.model.FlightDao;
import com.meli.desafioqa.model.HotelDao;
import com.meli.desafioqa.repositories.HotelRepository;
import com.meli.desafioqa.repositories.impl.HotelRepositoryImpl;
import com.meli.desafioqa.utils.HotelsFromJson;
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

@WebMvcTest(HotelRepository.class)
public class HotelRepositoryTest {

    private List<HotelDao> hotelDaos;
    private List<HotelDao> filteredHotelDaos;
    private HotelRepository hotelRepository;

    @BeforeEach
    private void beforeEach() throws IOException, InvalidPriceFormat {
        this.hotelRepository = new HotelRepositoryImpl("src/test/resources/Hoteles.json");
        List<HotelsFromJson> hotelsFromJsons = new ObjectMapper().readValue(new File("src/test/resources/Hoteles.json"), new TypeReference<>(){});
        hotelDaos = new LinkedList<>();
        for(HotelsFromJson h: hotelsFromJsons) hotelDaos.add(new HotelDao(h));

        filteredHotelDaos = hotelDaos.stream().filter(h -> h.getId().contains("CH-000")).collect(Collectors.toList());
    }

    @Test
    public void testGetAll(){
        Assertions.assertIterableEquals(this.hotelDaos, this.hotelRepository.getAll());
    }

    @Test
    public void testGetFilteredHotels() throws NotFoundException, PlaceDoesNotExist, NotValidFilterException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Assertions.assertIterableEquals(this.filteredHotelDaos, this.hotelRepository.getHotels(LocalDate.parse("10/02/2021", dtf), LocalDate.parse("15/02/2021", dtf),
                "Puerto Iguazú"));
    }

    @Test
    public void testGetNotExistLocation() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Assertions.assertThrows(PlaceDoesNotExist.class, () -> this.hotelRepository.getHotels(LocalDate.parse("10/02/2021", dtf), LocalDate.parse("15/02/2021", dtf),
                "NOT EXIST"));
    }

    @Test
    public void testNullFilter() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Assertions.assertThrows(NotValidFilterException.class, () -> this.hotelRepository.getHotels(LocalDate.parse("10/02/2021", dtf), LocalDate.parse("15/02/2021", dtf),
                null));
    }

    @Test
    public void testNotFoundHotelInValidDestination() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Assertions.assertThrows(NotFoundException.class, () -> this.hotelRepository.getHotels(LocalDate.parse("10/01/2021", dtf), LocalDate.parse("15/02/2021", dtf),
                "Puerto Iguazú"));
    }

}
