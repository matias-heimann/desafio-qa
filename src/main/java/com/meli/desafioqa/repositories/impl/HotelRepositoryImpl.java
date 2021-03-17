package com.meli.desafioqa.repositories.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meli.desafioqa.exceptions.PlaceDoesNotExist;
import com.meli.desafioqa.exceptions.InvalidPriceFormat;
import com.meli.desafioqa.exceptions.NotFoundException;
import com.meli.desafioqa.exceptions.NotValidFilterException;
import com.meli.desafioqa.model.HotelDao;
import com.meli.desafioqa.repositories.HotelRepository;
import com.meli.desafioqa.utils.HotelsFromJson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class HotelRepositoryImpl implements HotelRepository {

    @Value("${hotels-json}")
    private String filename;

    private List<HotelDao> hotels;

    public HotelRepositoryImpl(){
    }

    public HotelRepositoryImpl(String filename) throws IOException, InvalidPriceFormat {
        this.filename = filename;
        this.postConstruct();
    }

    @PostConstruct
    private void postConstruct() throws IOException, InvalidPriceFormat {
        this.hotels = new LinkedList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        List<HotelsFromJson> hotelsFromJsons = objectMapper.readValue(new File(filename), new TypeReference<>(){});
        for(HotelsFromJson h: hotelsFromJsons) hotels.add(new HotelDao(h));
    }


    @Override
    public List<HotelDao> getAll() {
        return this.hotels.stream().collect(Collectors.toList());
    }

    @Override
    public List<HotelDao> getHotels(LocalDate dateFrom, LocalDate dateTo, String city) throws NotValidFilterException, PlaceDoesNotExist, NotFoundException {
        if(dateFrom == null || dateTo == null || city == null){
            throw new NotValidFilterException("Cannot be null");
        }
        List<HotelDao> hotelDaos = this.hotels.stream().collect(Collectors.toList());
        hotelDaos = hotelDaos.stream()
                .filter(h -> h.getCity().toLowerCase(Locale.ROOT).equals(city.toLowerCase(Locale.ROOT)))
                .collect(Collectors.toList());
        if(hotelDaos.size() == 0){
            throw new PlaceDoesNotExist("El destino elegido no existe");
        }
        hotelDaos = hotelDaos.stream().filter(h ->
                h.getAvailableSince().compareTo(dateFrom) <= 0 && h.getAvailableUntil().compareTo(dateTo) >= 0
        ).collect(Collectors.toList());
        if(hotelDaos.size() == 0){
            throw new NotFoundException("No se encontraron hoteles para ese destino en esas fechas");
        }
        return hotelDaos;
    }

}
