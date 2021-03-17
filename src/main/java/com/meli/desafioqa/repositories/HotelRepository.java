package com.meli.desafioqa.repositories;

import com.meli.desafioqa.exceptions.DestinationDoesNotExist;
import com.meli.desafioqa.exceptions.NotFoundHotelsException;
import com.meli.desafioqa.exceptions.NotValidHotelFilterException;
import com.meli.desafioqa.model.HotelDao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HotelRepository {

    public List<HotelDao> getAll();
    public List<HotelDao> getHotels(LocalDate dateFrom, LocalDate dateTo, String city) throws NotValidHotelFilterException, DestinationDoesNotExist, NotFoundHotelsException;
    public Optional<HotelDao> getById(String id);

}
