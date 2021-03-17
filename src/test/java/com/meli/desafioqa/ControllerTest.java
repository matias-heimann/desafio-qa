package com.meli.desafioqa;

import com.meli.desafioqa.controllers.Controller;
import com.meli.desafioqa.exceptions.*;
import com.meli.desafioqa.model.FlightReservationRequest;
import com.meli.desafioqa.model.HotelReservationRequest;
import com.meli.desafioqa.model.PaymentMethod;
import com.meli.desafioqa.model.dto.*;
import com.meli.desafioqa.services.FlightService;
import com.meli.desafioqa.services.HotelService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.MockitoAnnotations.initMocks;

@WebMvcTest(Controller.class)
public class ControllerTest {

    @MockBean
    private HotelService hotelService;
    @MockBean
    private FlightService flightService;
    private Controller controller;
    private List<FlightDTO> allFlightDTOS;
    private List<HotelDTO> allHotelDTOS;
    private List<FlightDTO> filteredFlightDTOS;
    private List<HotelDTO> filteredHotelDTOS;
    private BookedFlightDTO bookedFlightDTO;
    private HotelReservationDTO hotelReservationDTO;
    private List<PersonDTO> people;


    @BeforeEach
    private void beforeEach(){
        initMocks(this);
        this.controller = new Controller(hotelService, flightService);
        this.allFlightDTOS = new LinkedList<>();
        allFlightDTOS.add(new FlightDTO("BAPI-1235", "Buenos Aires", "Puerto Iguazú", "Economy",
                6500,  "10/02/2021", "15/02/2021"));
        allFlightDTOS.add(new FlightDTO("PIBA-1420", "Puerto Iguazú", "Bogotá", "Business",
                43200,  "10/02/2021", "20/02/2021"));
        allFlightDTOS.add(new FlightDTO("PIBA-1420", "Puerto Iguazú", "Bogotá", "Economy",
                25735,  "10/02/2021", "21/02/2021"));
        allFlightDTOS.add(new FlightDTO("BATU-5536", "Buenos Aires", "Tucumán", "Economy",
                7320,  "10/02/2021", "17/02/2021"));

        this.filteredFlightDTOS = allFlightDTOS.stream().filter(f -> f.getFlightNumber().equals("BATU-5536")).collect(Collectors.toList());


        this.people = new LinkedList<>();
        people.add(new PersonDTO("41111", "matias", "heimann", "25/06/1997", "matias@gmail.com"));


        this.bookedFlightDTO = new BookedFlightDTO("matias@gmail.com", 10.0,1.0, 11.0,
                new FlightReservationDTO("10/02/2021", "15/02/2021",
                        "Buenos Aires", "Tucumán", "BATU-5536", 1, "Economy", people),
                new StatusDTO(HttpStatus.OK, "El proceso termino satisfactoriamente"));

        this.allHotelDTOS = new LinkedList<>();
        allHotelDTOS.add(new HotelDTO("CH-0002", "Cataratas Hotel", "Puerto Iguazú", "DOUBLE",
                6300, "10/02/2021", "20/03/2021", false));
        allHotelDTOS.add(new HotelDTO("CH-0003", "Cataratas Hotel 2", "Puerto Iguazú", "TRIPLE",
                8200, "10/02/2021", "23/03/2021", false));
        allHotelDTOS.add(new HotelDTO("HB-0001", "Hotel Bristol", "Buenos Aires", "DOUBLE",
                5435, "10/02/2021", "19/03/2021", false));
        allHotelDTOS.add(new HotelDTO("HB-0001", "Hotel Bristol 2", "Buenos Aires", "DOUBLE",
                7200, "10/02/2021", "17/04/2021", false));

        this.filteredHotelDTOS = allHotelDTOS.stream().filter(h -> h.getHotelCode().contains("CH-00")).collect(Collectors.toList());

        this.hotelReservationDTO = new HotelReservationDTO("matias@gmail.com", 10.0,1.0, 11.0,
                new BookingDTO("10/02/2021", "15/02/2021",
                        "Tucumán", "CH-0002", 1, "DOUBLE", people),
                new StatusDTO(HttpStatus.OK, "El proceso termino satisfactoriamente"));

    }

    @Test
    public void testGetHotelsWithNullParameters() throws Exception {
        Mockito.when(this.hotelService.getHotels(null, null, null)).thenThrow(NotValidFilterException.class);
        Assertions.assertThrows(NotValidFilterException.class, () ->this.controller.getHotels( null, null, null));
    }

    @Test
    public void testGetHotelsWithSomeEmptyParameters() throws Exception {
        Mockito.when(this.hotelService.getHotels("", "", "Buenos Aires")).thenThrow(NotValidFilterException.class);
        Assertions.assertThrows(NotValidFilterException.class, () ->this.controller.getHotels( "", "", "Buenos Aires"));
    }

    @Test
    public void testGetAllHotels() throws PlaceDoesNotExist, RoomTypeDoesNotExist, NotValidFilterException, NotFoundException, InvalidDateFormat {
        Mockito.when(this.hotelService.getHotels("", "", "")).thenReturn(this.allHotelDTOS);
        Assertions.assertIterableEquals(this.allHotelDTOS, this.controller.getHotels("", "", ""));
    }

    @Test
    public void testGetFilteredHotels() throws PlaceDoesNotExist, RoomTypeDoesNotExist, NotValidFilterException, NotFoundException, InvalidDateFormat {
        Mockito.when(this.hotelService.getHotels("10/02/2021", "15/02/2021", "Buenos Aires")).thenReturn(this.filteredHotelDTOS);
        Assertions.assertIterableEquals(this.filteredHotelDTOS, this.controller.getHotels("10/02/2021", "15/02/2021", "Buenos Aires"));
    }

    @Test
    public void bookAHotelWithNull() throws RoomTypeDoesNotExist, InvalidDateFormat, InvalidReservationException, NotValidDuesNumber, PlaceDoesNotExist, NotFoundException, NotValidFilterException {
        Mockito.when(this.hotelService.bookHotel(null)).thenThrow(InvalidReservationException.class);
        Assertions.assertThrows(InvalidReservationException.class, () -> this.controller.bookHotel(null));
    }

    @Test
    public void bookAHotel() throws RoomTypeDoesNotExist, InvalidDateFormat, InvalidReservationException, NotValidDuesNumber, PlaceDoesNotExist, NotFoundException, NotValidFilterException {
        HotelReservationRequest hotelReservationRequest = new HotelReservationRequest(hotelReservationDTO.getUserName(), hotelReservationDTO.getBooking(),
                new PaymentMethod("CREDIT", "101010", 5));
        Mockito.when(this.hotelService.bookHotel(hotelReservationRequest)).thenReturn(this.hotelReservationDTO);
        Assertions.assertEquals(this.hotelReservationDTO, this.controller.bookHotel(hotelReservationRequest));
    }

    @Test
    public void testGetAllFlights() throws PlaceDoesNotExist, NotValidFilterException, NotFoundException, InvalidDateFormat {
        Mockito.when(this.flightService.getFlights("","", "", "")).thenReturn(this.allFlightDTOS);
        Assertions.assertIterableEquals(this.allFlightDTOS, this.controller.getFlights("", "", "", ""));
    }

    @Test
    public void testGetFlightsWithSomeEmptyParameters() throws Exception {
        Mockito.when(this.flightService.getFlights("Buenos Aires", "", "", "")).thenThrow(NotValidFilterException.class);
        Assertions.assertThrows(NotValidFilterException.class, () ->this.controller.getFlights( "Buenos Aires", "", "", ""));
    }

    @Test
    public void testGetFilteredFlights() throws PlaceDoesNotExist, RoomTypeDoesNotExist, NotValidFilterException, NotFoundException, InvalidDateFormat {
        Mockito.when(this.flightService.getFlights("10/02/2021", "17/02/2021", "Buenos Aires", "Tucumán")).thenReturn(this.filteredFlightDTOS);
        Assertions.assertIterableEquals(this.filteredFlightDTOS, this.controller.getFlights("10/02/2021", "17/02/2021", "Buenos Aires", "Tucumán"));
    }

    @Test
    public void bookAFlightWithNull() throws InvalidDateFormat, InvalidReservationException, NotValidDuesNumber, PlaceDoesNotExist, NotFoundException, NotValidFilterException {
        Mockito.when(this.flightService.bookFlight(null)).thenThrow(InvalidReservationException.class);
        Assertions.assertThrows(InvalidReservationException.class, () -> this.controller.bookFlight(null));
    }

    @Test
    public void bookAFlight() throws InvalidDateFormat, InvalidReservationException, NotValidDuesNumber, PlaceDoesNotExist, NotFoundException, NotValidFilterException {
        FlightReservationRequest flightReservationRequest = new FlightReservationRequest(bookedFlightDTO.getUserName(), bookedFlightDTO.getFlightReservationDTO(),
                new PaymentMethod("CREDIT", "101010", 5));
        Mockito.when(this.flightService.bookFlight(flightReservationRequest)).thenReturn(this.bookedFlightDTO);
        Assertions.assertEquals(this.bookedFlightDTO, this.controller.bookFlight(flightReservationRequest));
    }

}
