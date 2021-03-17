package com.meli.desafioqa.services;

import com.meli.desafioqa.exceptions.*;
import com.meli.desafioqa.model.HotelReservationRequest;
import com.meli.desafioqa.model.dto.HotelDTO;
import com.meli.desafioqa.model.dto.HotelReservationDTO;

import java.util.List;

public interface HotelService {

    public List<HotelDTO> getHotels(String dateFrom, String dateTo, String destination) throws NotValidFilterException, InvalidDateFormat, PlaceDoesNotExist, NotFoundException, RoomTypeDoesNotExist;
    public HotelReservationDTO bookHotel(HotelReservationRequest hotelReservationRequest) throws InvalidReservationException, InvalidDateFormat, NotFoundException, NotValidFilterException, PlaceDoesNotExist, RoomTypeDoesNotExist, NotValidDuesNumber;
}
