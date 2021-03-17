package com.meli.desafioqa.services.impl;

import com.meli.desafioqa.exceptions.*;
import com.meli.desafioqa.model.FlightDao;
import com.meli.desafioqa.model.FlightReservationRequest;
import com.meli.desafioqa.model.dto.BookedFlightDTO;
import com.meli.desafioqa.model.dto.FlightDTO;
import com.meli.desafioqa.model.dto.FlightReservationDTO;
import com.meli.desafioqa.model.dto.StatusDTO;
import com.meli.desafioqa.repositories.FlightRepository;
import com.meli.desafioqa.services.FlightService;
import com.meli.desafioqa.utils.DateValidationUtil;
import com.meli.desafioqa.utils.InterestByDues;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@AllArgsConstructor
public class FlightServiceImpl implements FlightService {

    private FlightRepository flightRepository;

    @Override
    public List<FlightDTO> getFlights(String origin, String destination, String dateFrom, String dateTo) throws NotValidFilterException, InvalidDateFormat, NotFoundException, PlaceDoesNotExist {
        if(origin == null || destination == null || dateFrom == null || dateTo == null) throw new NotValidFilterException("Invalid filters for flight");

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        if(origin.equals("") && destination.equals("") && dateFrom.equals("") && dateTo.equals("")){
            List<FlightDTO> flightDTOS = new LinkedList<>();

            for(FlightDao f: this.flightRepository.getAll())
                flightDTOS.add(new FlightDTO(f.getFlightNumber(), f.getOrigin(),
                        f.getDestination(), f.getSeatType(), f.getPrice(),
                        dtf.format(f.getDateFrom()), dtf.format(f.getDateTo())));

            return flightDTOS;
        }
        else if(origin.equals("") || destination.equals("") || dateFrom.equals("") || dateTo.equals("")){
            throw new NotValidFilterException("If there is one empty filter all of them must be empty");
        }

        LocalDate localDateFrom = DateValidationUtil.dateValidation(dateFrom, "dd/MM/yyyy");
        LocalDate localDateTo = DateValidationUtil.dateValidation(dateTo, "dd/MM/yyyy");

        if(localDateFrom.compareTo(localDateTo) >= 0){
            throw new InvalidDateFormat("La fecha de entrada debe ser menor a la de salida");
        }

        List<FlightDTO> flightDTOS = new LinkedList<>();
        for(FlightDao f: this.flightRepository.getFlights(origin, destination, localDateFrom, localDateTo)){
            flightDTOS.add(new FlightDTO(f.getFlightNumber(), f.getOrigin(),
                    f.getDestination(), f.getSeatType(), f.getPrice(),
                    dtf.format(f.getDateFrom()), dtf.format(f.getDateTo())));
        }

        return flightDTOS;
    }

    @Override
    public BookedFlightDTO bookFlight(FlightReservationRequest flightReservationRequest) throws InvalidReservationException, InvalidDateFormat, PlaceDoesNotExist, NotFoundException, NotValidFilterException, NotValidDuesNumber {
        if(flightReservationRequest == null) throw new InvalidReservationException("Flight reservation can't be null");

        LocalDate localDateFrom = DateValidationUtil.dateValidation(flightReservationRequest.getFlightReservation().getDateFrom(), "dd/MM/yyyy");
        LocalDate localDateTo = DateValidationUtil.dateValidation(flightReservationRequest.getFlightReservation().getDateTo(), "dd/MM/yyyy");

        if(localDateFrom.compareTo(localDateTo) >= 0){
            throw new InvalidDateFormat("La fecha de entrada debe ser menor a la de salida");
        }

        List<FlightDao> flightDaos = this.flightRepository.getFlights(flightReservationRequest.getFlightReservation().getOrigin(),
                flightReservationRequest.getFlightReservation().getDestination(), localDateFrom, localDateTo);

        flightDaos = flightDaos.stream()
                .filter(f -> f.getFlightNumber().equals(flightReservationRequest.getFlightReservation().getFlightNumber()))
                .collect(Collectors.toList());

        if(flightDaos.size() == 0)
            throw new InvalidReservationException("Flight with the given information does not exist");

        flightDaos = flightDaos.stream().filter(f ->
                f.getSeatType().toUpperCase(Locale.ROOT).equals(flightReservationRequest.getFlightReservation().getSeatType().toUpperCase(Locale.ROOT))
        ).collect(Collectors.toList());

        if(flightDaos.size() == 0)
            throw new InvalidReservationException("That flight with that kind of seats doesn't exist");

        if(flightReservationRequest.getFlightReservation().getPeople().size() !=
                flightReservationRequest.getFlightReservation().getSeats())
            throw new InvalidReservationException("El numero de gente en la lista de la reserva y en el del numero de personas no coinciden");

        FlightDao flightDao = flightDaos.get(0);
        Double interest = 0.0;
        Double amount = flightDao.getPrice() * (double)flightReservationRequest.getFlightReservation().getSeats();
        Double total = 0.0;

        if(flightReservationRequest.getPaymentMethod().getType().equals("CREDIT")){
            interest = InterestByDues.getInterestByDues(flightReservationRequest.getPaymentMethod().getDues());
            total = amount * (1 + interest/100);
        }
        else if(flightReservationRequest.getPaymentMethod().getType().equals("DEBIT")){
            if(flightReservationRequest.getPaymentMethod().getDues() != 1){
                throw new InvalidReservationException("Se debe pagar maximo una cuota pagando con DEBIT");
            }
            interest = 0.0;
            total = amount * (1 + interest/100);
        }
        else{
            throw new InvalidReservationException("El tipo de pago no es valido");
        }

        return new BookedFlightDTO(flightReservationRequest.getUserName(), amount, interest, total,
                flightReservationRequest.getFlightReservation(),
                new StatusDTO(HttpStatus.OK, "El proceso termino satisfactoriamente"));
    }
}
