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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class HotelRepositoryImpl implements HotelRepository {

    @Value("${hotels-json}")
    private String filename;

    private HashMap<String, HotelDao> hotels;

    public HotelRepositoryImpl(){
        this.hotels = new HashMap<>();
    }

    @PostConstruct
    private void postConstruct() throws IOException, InvalidPriceFormat {
        ObjectMapper objectMapper = new ObjectMapper();
        List<HotelsFromJson> hotelsFromJsons = objectMapper.readValue(new File(filename), new TypeReference<>(){});
        for(HotelsFromJson h: hotelsFromJsons) hotels.put(h.getId(), new HotelDao(h));
    }


    @Override
    public List<HotelDao> getAll() {
        return this.hotels.values().stream().collect(Collectors.toList());
    }

    @Override
    public List<HotelDao> getHotels(LocalDate dateFrom, LocalDate dateTo, String city) throws NotValidFilterException, PlaceDoesNotExist, NotFoundException {
        List<HotelDao> hotelDaos = this.hotels.values().stream().collect(Collectors.toList());
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

    @Override
    public Optional<HotelDao> getById(String id) {
        return (this.hotels.get(id) != null) ? Optional.of(this.hotels.get(id)) : Optional.empty();
    }

}
