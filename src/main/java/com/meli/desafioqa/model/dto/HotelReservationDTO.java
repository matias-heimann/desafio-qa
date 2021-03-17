package com.meli.desafioqa.model.dto;

import com.meli.desafioqa.model.HotelReservationRequest;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
@AllArgsConstructor @EqualsAndHashCode
public class HotelReservationDTO {

    @Email
    private String userName;
    @NotNull
    private Double amount;
    @NotNull
    private Double interest;
    @NotNull
    private Double total;
    @NotNull
    private BookingDTO booking;
    @NotNull
    private StatusDTO statusCode;


}
