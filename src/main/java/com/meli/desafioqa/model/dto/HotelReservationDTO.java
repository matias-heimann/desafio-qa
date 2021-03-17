package com.meli.desafioqa.model.dto;

import com.meli.desafioqa.model.HotelReservationRequest;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

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
    private PaymentMethodDTO paymentMethod;
    @NotNull
    private StatusDTO statusCode;


}
