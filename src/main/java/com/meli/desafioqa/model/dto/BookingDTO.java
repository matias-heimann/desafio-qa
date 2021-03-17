package com.meli.desafioqa.model.dto;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Getter @Setter @AllArgsConstructor @EqualsAndHashCode
public class BookingDTO {

    @NotBlank(message = "Es obligatorio llenar la fecha de entrada")
    private String dateFrom;
    @NotBlank(message = "Es obligatorio llenar fecha de salida")
    private String dateTo;
    @NotBlank(message = "Es obligatorio llenar el destino")
    private String destination;
    @NotBlank(message = "Es obligatorio utilizar el codigo de hotel")
    private String hotelCode;
    @NotNull(message = "Es obligatorio llenar el campo de cantidad de personas")
    @Min(value = 1, message = "Tiene que haber al menos una persona")
    private Integer peopleAmount;
    @NotBlank(message = "Es obligatorio usar el tipo de habitacion")
    private String roomType;
    @NotEmpty(message = "La lista de personas no debe ser vacia") @Valid
    private List<PersonDTO> people;

    public BookingDTO(){}


}
