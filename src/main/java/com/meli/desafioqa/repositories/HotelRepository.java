package com.meli.desafioqa.repositories;

import com.meli.desafioqa.exceptions.PlaceDoesNotExist;
import com.meli.desafioqa.exceptions.NotFoundException;
import com.meli.desafioqa.exceptions.NotValidFilterException;
import com.meli.desafioqa.model.HotelDao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HotelRepository {

    public List<HotelDao> getAll();
    public List<HotelDao> getHotels(LocalDate dateFrom, LocalDate dateTo, String city) throws NotValidFilterException, PlaceDoesNotExist, NotFoundException;
    public Optional<HotelDao> getById(String id);

}