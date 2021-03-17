package com.meli.desafioqa.services.impl;

import com.meli.desafioqa.exceptions.*;
import com.meli.desafioqa.model.HotelDao;
import com.meli.desafioqa.model.HotelReservationRequest;
import com.meli.desafioqa.model.dto.HotelDTO;
import com.meli.desafioqa.model.dto.HotelReservationDTO;
import com.meli.desafioqa.model.dto.StatusDTO;
import com.meli.desafioqa.repositories.HotelRepository;
import com.meli.desafioqa.services.HotelService;
import com.meli.desafioqa.utils.DateValidationUtil;
import com.meli.desafioqa.utils.InterestByDues;
import com.meli.desafioqa.utils.RoomSizeTranslateUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;

@Service @AllArgsConstructor
public class HotelServiceImpl implements HotelService {

    private HotelRepository hotelRepository;

    @Override
    public List<HotelDTO> getHotels(String dateFrom, String dateTo, String destination) throws NotValidFilterException, InvalidDateFormat, PlaceDoesNotExist, NotFoundException, RoomTypeDoesNotExist {
        if(dateFrom == null || dateTo == null || destination == null){
            throw new NotValidFilterException("There can't be any null parameters");
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        if(dateFrom.equals("") && dateTo.equals("") && destination.equals("")){
            List<HotelDTO> hotelDTOS = new LinkedList<>();
            for(HotelDao h: this.hotelRepository.getAll())
                hotelDTOS.add(new HotelDTO(h.getId(), h.getName(),
                        h.getCity(), RoomSizeTranslateUtil.roomType(h.getRoomMaxCapacity()),
                        h.getPrice(), dtf.format(h.getAvailableSince()), dtf.format(h.getAvailableUntil()), h.getBooked()));
            return hotelDTOS;
        }
        else if(dateFrom.equals("") || dateTo.equals("") || destination.equals("")){
            throw new NotValidFilterException("If there is one empty filter all of them must be empty");
        }

        LocalDate localDateFrom = DateValidationUtil.dateValidation(dateFrom, "dd/MM/yyyy");
        LocalDate localDateTo = DateValidationUtil.dateValidation(dateTo, "dd/MM/yyyy");

        if(localDateFrom.compareTo(localDateTo) >= 0){
            throw new InvalidDateFormat("La fecha de entrada debe ser menor a la de salida");
        }

        List<HotelDTO> hotelDTOS = new LinkedList<>();
        for(HotelDao h: this.hotelRepository.getHotels(localDateFrom, localDateTo, destination))
            hotelDTOS.add(new HotelDTO(h.getId(), h.getName(),
                    h.getCity(), RoomSizeTranslateUtil.roomType(h.getRoomMaxCapacity()),
                    h.getPrice(), dtf.format(h.getAvailableSince()), dtf.format(h.getAvailableUntil()), h.getBooked()));

        return hotelDTOS;
    }

    @Override
    public HotelReservationDTO bookHotel(HotelReservationRequest hotelReservationRequest) throws InvalidReservationException, InvalidDateFormat, NotFoundException, NotValidFilterException, PlaceDoesNotExist, RoomTypeDoesNotExist, NotValidDuesNumber {
        if(hotelReservationRequest == null){
            throw new InvalidReservationException("Hotel reservation can't be null");
        }

        LocalDate localDateFrom = DateValidationUtil.dateValidation(hotelReservationRequest.getBooking().getDateFrom(), "dd/MM/yyyy");
        LocalDate localDateTo = DateValidationUtil.dateValidation(hotelReservationRequest.getBooking().getDateTo(), "dd/MM/yyyy");

        if(localDateFrom.compareTo(localDateTo) >= 0){
            throw new InvalidDateFormat("La fecha de entrada debe ser menor a la de salida");
        }

        List<HotelDao> hotelDaos = this.hotelRepository.getHotels(localDateFrom, localDateTo, hotelReservationRequest.getBooking().getDestination());

        Optional<HotelDao> hotelDaoOptional = hotelDaos.stream()
                .filter(h -> h.getId().equals(hotelReservationRequest.getBooking().getHotelCode()))
                .findFirst();

        if(hotelDaoOptional.isEmpty()){
            throw new InvalidReservationException("Hotel with the given information does not exist");
        }

        if(hotelReservationRequest.getBooking().getPeople().size() != hotelReservationRequest.getBooking().getPeopleAmount()){
            throw new InvalidReservationException("El numero de gente en la lista de la reserva y en el del numero de personas no coinciden");
        }

        if(RoomSizeTranslateUtil.roomType(hotelReservationRequest.getBooking().getRoomType().toUpperCase(Locale.ROOT))
        < hotelReservationRequest.getBooking().getPeopleAmount()){
            throw new InvalidReservationException("El tipo de habitación seleccionada no coincide con la cantidad de personas que se alojarán en ella.");
        }
        Double interest = 0.0;
        Double amount = hotelDaoOptional.get().getPrice() * (double)DAYS.between(localDateFrom, localDateTo);
        Double total = 0.0;
        if(hotelReservationRequest.getPaymentMethod().getType().equals("CREDIT")){
            interest = InterestByDues.getInterestByDues(hotelReservationRequest.getPaymentMethod().getDues());
            total = amount * (1 + interest/100);
        }
        else if(hotelReservationRequest.getPaymentMethod().getType().equals("DEBIT")){
            if(hotelReservationRequest.getPaymentMethod().getDues() != 1){
                throw new InvalidReservationException("Se debe pagar maximo una cuota pagando con DEBIT");
            }
            interest = 0.0;
            total = amount * (1 + interest/100);
        }
        else{
            throw new InvalidReservationException("El tipo de pago no es valido");
        }

        return new HotelReservationDTO(hotelReservationRequest.getUserName(),
                amount, interest, total, hotelReservationRequest.getBooking(),
                new StatusDTO(HttpStatus.OK, "El proceso termino satisfactoriamente"));
    }
}
