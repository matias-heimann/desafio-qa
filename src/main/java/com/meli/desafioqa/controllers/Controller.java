package com.meli.desafioqa.controllers;

import com.meli.desafioqa.exceptions.*;
import com.meli.desafioqa.model.FlightReservation;
import com.meli.desafioqa.model.HotelReservationRequest;
import com.meli.desafioqa.model.dto.StatusDTO;
import com.meli.desafioqa.model.dto.HotelDTO;
import com.meli.desafioqa.model.dto.HotelReservationDTO;
import com.meli.desafioqa.services.FlightService;
import com.meli.desafioqa.services.HotelService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
            throws NotValidHotelFilterException, InvalidDateFormat, DestinationDoesNotExist, NotFoundHotelsException, RoomTypeDoesNotExist {
        return this.hotelService.getHotels(dateFrom, dateTo, destination);
    }

    @PostMapping("/booking")
    public HotelReservationDTO bookHotel(@RequestBody @Valid HotelReservationRequest hotelReservationRequest) throws InvalidDateFormat, InvalidReservationException, DestinationDoesNotExist, RoomTypeDoesNotExist, NotValidHotelFilterException, NotFoundHotelsException, NotValidDuesNumber {
        return this.hotelService.bookHotel(hotelReservationRequest);
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
        return new ResponseEntity<>(new StatusDTO(HttpStatus.BAD_REQUEST,
                methodArgumentNotValidException.getAllErrors().get(0).getDefaultMessage() + " and " + (methodArgumentNotValidException.getErrorCount() - 1) + " more errors"),
                HttpStatus.BAD_REQUEST);
    }
}
