package com.meli.desafioqa.model.dto;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Getter @Setter @AllArgsConstructor @EqualsAndHashCode
public class BookingDTO {

    @NotNull @NotBlank
    private String dateFrom;
    @NotNull @NotBlank
    private String dateTo;
    @NotNull @NotBlank
    private String destination;
    @NotNull @NotBlank
    private String hotelCode;
    @NotNull
    private Integer peopleAmount;
    @NotNull @NotBlank
    private String roomType;
    @NotNull @NotEmpty @Valid
    private List<PersonDTO> people;

    public BookingDTO(){}


}
