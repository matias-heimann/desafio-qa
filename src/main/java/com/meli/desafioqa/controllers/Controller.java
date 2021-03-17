package com.meli.desafioqa.controllers;

import com.meli.desafioqa.exceptions.*;
import com.meli.desafioqa.model.FlightReservation;
import com.meli.desafioqa.model.HotelReservationRequest;
import com.meli.desafioqa.model.dto.FlightDTO;
import com.meli.desafioqa.model.dto.StatusDTO;
import com.meli.desafioqa.model.dto.HotelDTO;
import com.meli.desafioqa.model.dto.HotelReservationDTO;
import com.meli.desafioqa.services.FlightService;
import com.meli.desafioqa.services.HotelService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class Controller {

    private HotelService hotelService;
    private FlightService flightService;

    @GetMapping("/hotels")
    public List<HotelDTO> getHotels(@RequestParam(required = false, defaultValue = "") String dateFrom,
                                    @RequestParam(required = false, defaultValue = "") String dateTo,
                                    @RequestParam(required = false, defaultValue = "") String destination)
            throws NotValidFilterException, InvalidDateFormat, PlaceDoesNotExist, NotFoundException, RoomTypeDoesNotExist {
        return this.hotelService.getHotels(dateFrom, dateTo, destination);
    }

    @PostMapping("/booking")
    public HotelReservationDTO bookHotel(@RequestBody @Valid HotelReservationRequest hotelReservationRequest) throws InvalidDateFormat, InvalidReservationException, PlaceDoesNotExist, RoomTypeDoesNotExist, NotValidFilterException, NotFoundException, NotValidDuesNumber {
        return this.hotelService.bookHotel(hotelReservationRequest);
    }

    @GetMapping("/flights")
    public List<FlightDTO> getFlights(@RequestParam(required = false, defaultValue = "") String dateFrom,
                                      @RequestParam(required = false, defaultValue = "") String dateTo,
                                      @RequestParam(required = false, defaultValue = "") String origin,
                                      @RequestParam(required = false, defaultValue = "") String destination)
            throws PlaceDoesNotExist, NotValidFilterException, NotFoundException, InvalidDateFormat {
        return this.flightService.getFlights(origin, destination, dateFrom, dateTo);
    }

    @PostMapping("/flight-reservation")
    public String bookAFlight(@RequestBody FlightReservation flight){
        return flight.getDate().toString();
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<StatusDTO> handleException(BaseException baseException){
        return new ResponseEntity<>(new StatusDTO(baseException.getStatus(), baseException.getMessage()),
                baseException.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StatusDTO> handleExcpetion(MethodArgumentNotValidException methodArgumentNotValidException){
        StringBuffer sb = new StringBuffer("[");
        String errorConcat = methodArgumentNotValidException.getAllErrors().stream().map(e -> e.getDefaultMessage())
                .collect(Collectors.joining(", "));
        sb.append(errorConcat);
        sb.append(new StringBuffer("]"));
        return new ResponseEntity<>(new StatusDTO(HttpStatus.BAD_REQUEST, sb.toString()), HttpStatus.BAD_REQUEST);
    }
}
