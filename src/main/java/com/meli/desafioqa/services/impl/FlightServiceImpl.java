package com.meli.desafioqa.services.impl;

import com.meli.desafioqa.exceptions.InvalidDateFormat;
import com.meli.desafioqa.exceptions.NotFoundException;
import com.meli.desafioqa.exceptions.NotValidFilterException;
import com.meli.desafioqa.exceptions.PlaceDoesNotExist;
import com.meli.desafioqa.model.FlightDao;
import com.meli.desafioqa.model.dto.FlightDTO;
import com.meli.desafioqa.model.dto.FlightReservationDTO;
import com.meli.desafioqa.repositories.FlightRepository;
import com.meli.desafioqa.services.FlightService;
import com.meli.desafioqa.utils.DateValidationUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

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
    public FlightReservationDTO bookFlight() {
        return null;
    }
}
