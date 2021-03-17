package com.meli.desafioqa;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meli.desafioqa.exceptions.*;
import com.meli.desafioqa.model.HotelDao;
import com.meli.desafioqa.model.HotelReservationRequest;
import com.meli.desafioqa.model.PaymentMethod;
import com.meli.desafioqa.model.dto.*;
import com.meli.desafioqa.repositories.HotelRepository;
import com.meli.desafioqa.services.HotelService;
import com.meli.desafioqa.services.impl.HotelServiceImpl;
import com.meli.desafioqa.utils.HotelsFromJson;
import com.meli.desafioqa.utils.RoomSizeTranslateUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.MockitoAnnotations.initMocks;

@WebMvcTest(HotelService.class)
public class HotelServiceTest {

    @MockBean
    private HotelRepository hotelRepository;
    private HotelService hotelService;
    private List<HotelDao> allHotels;
    private List<HotelDao> filteredHotels;
    private HotelReservationDTO hotelReservationDTO;


    @BeforeEach
    private void beforeEach() throws IOException, InvalidPriceFormat {
        initMocks(this);
        this.hotelService = new HotelServiceImpl(this.hotelRepository);

        ObjectMapper objectMapper = new ObjectMapper();
        this.allHotels = new LinkedList<>();
        for(HotelsFromJson h: objectMapper.readValue(new File("src/test/resources/Hoteles.json"),
                new TypeReference<List<HotelsFromJson>>() {}))
            allHotels.add(new HotelDao(h));

        this.filteredHotels = this.allHotels.stream().filter(f -> f.getId().contains("CH-000")).collect(Collectors.toList());

        List<PersonDTO> people = new LinkedList<>();
        people.add(new PersonDTO("41111", "matias", "heimann", "25/06/1997", "matias@gmail.com"));
        this.hotelReservationDTO = new HotelReservationDTO("matias@gmail.com", 31500.0,10.0, 34650.0,
                new BookingDTO("10/02/2021", "15/02/2021",
                        "Puerto Iguazú", "CH-0002", 1, "DOUBLE", people, null),
                new StatusDTO(HttpStatus.OK, "El proceso termino satisfactoriamente"));

    }

    @Test
    public void testGetHotelsWithNullParameters(){
        Assertions.assertThrows(NotValidFilterException.class, () -> this.hotelService.getHotels(null, null, ""));
    }

    @Test
    public void testGetHotelsSomeEmptyParameters(){
        Assertions.assertThrows(NotValidFilterException.class, () -> this.hotelService.getHotels("", "", "Buenos Aires"));
    }

    @Test
    public void testGetAllHotels() throws RoomTypeDoesNotExist, PlaceDoesNotExist, NotValidFilterException, NotFoundException, InvalidDateFormat {
        Mockito.when(this.hotelRepository.getAll()).thenReturn(allHotels);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        List<HotelDTO> hotelDTOS = new LinkedList<>();
        for(HotelDao h: allHotels)
            hotelDTOS.add(new HotelDTO(h.getId(), h.getName(),
                    h.getCity(), RoomSizeTranslateUtil.roomType(h.getRoomMaxCapacity()),
                    h.getPrice(), dtf.format(h.getAvailableSince()), dtf.format(h.getAvailableUntil()), h.getBooked()));

        Assertions.assertIterableEquals(hotelDTOS, this.hotelService.getHotels("", "", ""));
    }

    @Test
    public void testGetFilteredHotels() throws RoomTypeDoesNotExist, PlaceDoesNotExist, NotValidFilterException, NotFoundException, InvalidDateFormat {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Mockito.when(this.hotelRepository.getHotels(LocalDate.parse("10/02/2021", dtf), LocalDate.parse("15/02/2021", dtf),
                "Puerto Iguazú")).thenReturn(filteredHotels);
        List<HotelDTO> hotelDTOS = new LinkedList<>();
        for(HotelDao h: filteredHotels)
            hotelDTOS.add(new HotelDTO(h.getId(), h.getName(),
                    h.getCity(), RoomSizeTranslateUtil.roomType(h.getRoomMaxCapacity()),
                    h.getPrice(), dtf.format(h.getAvailableSince()), dtf.format(h.getAvailableUntil()), h.getBooked()));

        Assertions.assertIterableEquals(hotelDTOS, this.hotelService.getHotels("10/02/2021", "15/02/2021", "Puerto Iguazú"));
    }

    @Test
    public void testGetHotelsWithASmallerDateTo() {
        Assertions.assertThrows(InvalidDateFormat.class, () -> this.hotelService.getHotels("15/02/2021", "10/02/2021", "Puerto Iguazú"));
    }

    @Test
    public void testBookANullHotel() {
        Assertions.assertThrows(InvalidReservationException.class, () -> this.hotelService.bookHotel(null));
    }

    @Test
    public void testBookAHotelWithCredit() throws RoomTypeDoesNotExist, InvalidDateFormat, InvalidReservationException, NotValidDuesNumber, PlaceDoesNotExist, NotFoundException, NotValidFilterException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Mockito.when(this.hotelRepository.getHotels(LocalDate.parse("10/02/2021", dtf), LocalDate.parse("15/02/2021", dtf),
                "Puerto Iguazú")).thenReturn(filteredHotels);
        HotelReservationRequest hotelReservationRequest = new HotelReservationRequest(
                hotelReservationDTO.getUserName(), hotelReservationDTO.getBooking());
        hotelReservationRequest.getBooking().setPaymentMethod(new PaymentMethod("CREDIT", "1234-1234-1234-1234", 5));
        Assertions.assertEquals(this.hotelReservationDTO, this.hotelService.bookHotel(hotelReservationRequest));
    }

    @Test
    public void testBookAHotelWithDebit() throws RoomTypeDoesNotExist, InvalidDateFormat, InvalidReservationException, NotValidDuesNumber, PlaceDoesNotExist, NotFoundException, NotValidFilterException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Mockito.when(this.hotelRepository.getHotels(LocalDate.parse("10/02/2021", dtf), LocalDate.parse("15/02/2021", dtf),
                "Puerto Iguazú")).thenReturn(filteredHotels);
        this.hotelReservationDTO.setInterest(0.0);
        this.hotelReservationDTO.setTotal(this.hotelReservationDTO.getAmount());
        HotelReservationRequest hotelReservationRequest = new HotelReservationRequest(
                hotelReservationDTO.getUserName(), hotelReservationDTO.getBooking());
        hotelReservationRequest.getBooking().setPaymentMethod(new PaymentMethod("DEBIT", "1234-1234-1234-1234", 1));
        Assertions.assertEquals(this.hotelReservationDTO, this.hotelService.bookHotel(hotelReservationRequest));
    }

    @Test
    public void testBookAHotelWithInvalidPaymentMethod() throws RoomTypeDoesNotExist, InvalidDateFormat, InvalidReservationException, NotValidDuesNumber, PlaceDoesNotExist, NotFoundException, NotValidFilterException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Mockito.when(this.hotelRepository.getHotels(LocalDate.parse("10/02/2021", dtf), LocalDate.parse("15/02/2021", dtf),
                "Puerto Iguazú")).thenReturn(filteredHotels);
        HotelReservationRequest hotelReservationRequest = new HotelReservationRequest(
                hotelReservationDTO.getUserName(), hotelReservationDTO.getBooking());
        hotelReservationRequest.getBooking().setPaymentMethod(new PaymentMethod("NOT VALID", "1234-1234-1234-1234", 1));
        Assertions.assertThrows(InvalidReservationException.class, () -> this.hotelService.bookHotel(hotelReservationRequest));
    }

    @Test
    public void testPayWithDebitWithDues() throws NotFoundException, PlaceDoesNotExist, NotValidFilterException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Mockito.when(this.hotelRepository.getHotels(LocalDate.parse("10/02/2021", dtf), LocalDate.parse("15/02/2021", dtf),
                "Puerto Iguazú")).thenReturn(filteredHotels);
        HotelReservationRequest hotelReservationRequest = new HotelReservationRequest(
                hotelReservationDTO.getUserName(), hotelReservationDTO.getBooking());
        hotelReservationRequest.getBooking().setPaymentMethod(new PaymentMethod("DEBIT", "1234-1234-1234-1234", 2));
        Assertions.assertThrows(InvalidReservationException.class, () -> this.hotelService.bookHotel(hotelReservationRequest));
    }

    @Test
    public void testPayWithCreditWithNegativeDues() throws NotFoundException, PlaceDoesNotExist, NotValidFilterException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Mockito.when(this.hotelRepository.getHotels(LocalDate.parse("10/02/2021", dtf), LocalDate.parse("15/02/2021", dtf),
                "Puerto Iguazú")).thenReturn(filteredHotels);
        HotelReservationRequest hotelReservationRequest = new HotelReservationRequest(
                hotelReservationDTO.getUserName(), hotelReservationDTO.getBooking());
        hotelReservationRequest.getBooking().setPaymentMethod(new PaymentMethod("CREDIT", "1234-1234-1234-1234", -1));
        Assertions.assertThrows(NotValidDuesNumber.class, () -> this.hotelService.bookHotel(hotelReservationRequest));
    }

    @Test
    public void testBookForNonExistentDestination() throws NotFoundException, PlaceDoesNotExist, NotValidFilterException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Mockito.when(this.hotelRepository.getHotels(LocalDate.parse("10/02/2021", dtf), LocalDate.parse("15/02/2021", dtf),
                "Not exist destination")).thenThrow(NotFoundException.class);
        this.hotelReservationDTO.getBooking().setDestination("Not exist destination");
        HotelReservationRequest hotelReservationRequest = new HotelReservationRequest(
                hotelReservationDTO.getUserName(), hotelReservationDTO.getBooking());
        hotelReservationRequest.getBooking().setPaymentMethod(new PaymentMethod("DEBIT", "1234-1234-1234-1234", 1));
        Assertions.assertThrows(NotFoundException.class, () -> this.hotelService.bookHotel(hotelReservationRequest));
    }

    @Test
    public void testBookForNonExistentRoomType() throws NotFoundException, PlaceDoesNotExist, NotValidFilterException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Mockito.when(this.hotelRepository.getHotels(LocalDate.parse("10/02/2021", dtf), LocalDate.parse("15/02/2021", dtf),
                "Puerto Iguazú")).thenReturn(filteredHotels);
        hotelReservationDTO.getBooking().setRoomType("TRIPLE");
        HotelReservationRequest hotelReservationRequest = new HotelReservationRequest(
                hotelReservationDTO.getUserName(), hotelReservationDTO.getBooking());
        hotelReservationRequest.getBooking().setPaymentMethod(new PaymentMethod("DEBIT", "1234-1234-1234-1234", 1));
        Assertions.assertThrows(InvalidReservationException.class, () -> this.hotelService.bookHotel(hotelReservationRequest));
    }
}
