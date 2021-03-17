package com.meli.desafioqa;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meli.desafioqa.exceptions.*;
import com.meli.desafioqa.model.FlightDao;
import com.meli.desafioqa.model.FlightReservationRequest;
import com.meli.desafioqa.model.HotelDao;
import com.meli.desafioqa.model.PaymentMethod;
import com.meli.desafioqa.model.dto.*;
import com.meli.desafioqa.repositories.FlightRepository;
import com.meli.desafioqa.services.FlightService;
import com.meli.desafioqa.services.impl.FlightServiceImpl;
import com.meli.desafioqa.utils.FlightFromJson;
import com.meli.desafioqa.utils.HotelsFromJson;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.MockitoAnnotations.initMocks;

@SpringBootTest
public class FlightServiceTest {

    @MockBean
    private FlightRepository flightRepository;

    private FlightService flightService;
    private List<FlightDao> flightDaos;
    private List<FlightDao> filteredFlightDaos;
    private LinkedList<PersonDTO> people;
    private BookedFlightDTO bookedFlightDTO;

    @BeforeEach
    private void beforeEach() throws IOException, InvalidPriceFormat {
        initMocks(this);
        this.flightService = new FlightServiceImpl(flightRepository);
        this.flightDaos = new LinkedList<>();

        ObjectMapper objectMapper = new ObjectMapper();
        for(FlightFromJson f: objectMapper.readValue(new File("src/test/resources/Vuelos.json"),
                new TypeReference<List<FlightFromJson>>() {}))
            flightDaos.add(new FlightDao(f));

        this.filteredFlightDaos = this.flightDaos.stream().filter(f -> f.getFlightNumber().equals("BAPI-1235")).collect(Collectors.toList());

        this.people = new LinkedList<>();
        people.add(new PersonDTO("41111", "matias", "heimann", "25/06/1997", "matias@gmail.com"));


        this.bookedFlightDTO = new BookedFlightDTO("matias@gmail.com", 6500.0,10.0, 7150.0,
                new FlightReservationDTO("10/02/2021", "15/02/2021",
                        "Buenos Aires", "Puerto Iguazú", "BAPI-1235", 1, "Economy", people),
                new StatusDTO(HttpStatus.OK, "El proceso termino satisfactoriamente"));
    }

    @Test
    public void testGetFlightsWithNullParameters(){
        Assertions.assertThrows(NotValidFilterException.class, () -> this.flightService.getFlights(null, null, "", ""));
    }

    @Test
    public void testGetFlightWithSomeEmptyParameters(){
        Assertions.assertThrows(NotValidFilterException.class, () -> this.flightService.getFlights("Buenos Aires", "", "", ""));
    }

    @Test
    public void testGetAllFlights() throws PlaceDoesNotExist, NotValidFilterException, NotFoundException, InvalidDateFormat {
        Mockito.when(this.flightRepository.getAll()).thenReturn(this.flightDaos);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        List<FlightDTO> expected = new LinkedList<>();
        for(FlightDao f: flightDaos){
            expected.add(new FlightDTO(f.getFlightNumber(), f.getOrigin(),
                    f.getDestination(), f.getSeatType(), f.getPrice(),
                    dtf.format(f.getDateFrom()), dtf.format(f.getDateTo())));
        }
        Assertions.assertIterableEquals(expected, this.flightService.getFlights("", "", "", ""));
    }

    @Test
    public void testGetFlightFiltered() throws PlaceDoesNotExist, NotFoundException, NotValidFilterException, InvalidDateFormat {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Mockito.when(this.flightRepository.getFlights("Buenos Aires", "Puerto Iguazú",
                LocalDate.parse("10/02/2021", dtf), LocalDate.parse("15/02/2021", dtf))).thenReturn(this.filteredFlightDaos);
        List<FlightDTO> expected = new LinkedList<>();
        for(FlightDao f: filteredFlightDaos){
            expected.add(new FlightDTO(f.getFlightNumber(), f.getOrigin(),
                    f.getDestination(), f.getSeatType(), f.getPrice(),
                    dtf.format(f.getDateFrom()), dtf.format(f.getDateTo())));
        }
        Assertions.assertIterableEquals(expected, this.flightService.getFlights("Buenos Aires", "Puerto Iguazú", "10/02/2021", "15/02/2021"));
    }

    @Test
    public void testGetFlightsWithDateToSmallerThanDateFrom(){
        Assertions.assertThrows(InvalidDateFormat.class, () -> this.flightService.getFlights("Buenos Aires", "Puerto Iguazú", "15/02/2021","10/02/2021" ));
    }

    @Test
    public void testGetFlightsWithNoDestination() throws PlaceDoesNotExist, NotFoundException, NotValidFilterException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Mockito.when(this.flightRepository.getFlights("Buenos Aires", "Null Place",
                LocalDate.parse("10/02/2021", dtf), LocalDate.parse("15/02/2021", dtf))).thenThrow(NotFoundException.class);
        Assertions.assertThrows(NotFoundException.class, () -> this.flightService.getFlights("Buenos Aires", "Null Place", "10/02/2021","15/02/2021" ));
    }

    @Test
    public void testGetFlightsWithNoOrigin() throws PlaceDoesNotExist, NotFoundException, NotValidFilterException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Mockito.when(this.flightRepository.getFlights("Null Place", "Buenos Aires",
                LocalDate.parse("10/02/2021", dtf), LocalDate.parse("15/02/2021", dtf))).thenThrow(NotFoundException.class);
        Assertions.assertThrows(NotFoundException.class, () -> this.flightService.getFlights("Null Place", "Buenos Aires", "10/02/2021","15/02/2021" ));
    }

    @Test
    public void testGetFlightsWithInvalidDateFormat() throws PlaceDoesNotExist, NotFoundException, NotValidFilterException {
        Assertions.assertThrows(InvalidDateFormat.class, () -> this.flightService.getFlights("Buenos Aires", "Puerto Iguazú", "10-02-2021","15-02-2021" ));
    }

    @Test
    public void testBookFlightWithNull() throws InvalidDateFormat, InvalidReservationException, NotValidDuesNumber, PlaceDoesNotExist, NotFoundException, NotValidFilterException {
        Assertions.assertThrows(InvalidReservationException.class, () ->    this.flightService.bookFlight(null));
    }

    @Test
    public void testBookFlight() throws PlaceDoesNotExist, NotFoundException, NotValidFilterException, NotValidDuesNumber, InvalidReservationException, InvalidDateFormat {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Mockito.when(this.flightRepository.getFlights("Buenos Aires", "Puerto Iguazú",
                LocalDate.parse("10/02/2021", dtf), LocalDate.parse("15/02/2021", dtf))).thenReturn(this.filteredFlightDaos);

        FlightReservationRequest flightReservationRequest = new FlightReservationRequest(this.bookedFlightDTO.getUserName(),
                this.bookedFlightDTO.getFlightReservationDTO(), new PaymentMethod("CREDIT", "1234-1234-1234-1234", 5));

        Assertions.assertEquals(this.bookedFlightDTO, this.flightService.bookFlight(flightReservationRequest));
    }
}
