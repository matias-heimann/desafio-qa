package com.meli.desafioqa.utils;

import com.meli.desafioqa.exceptions.InvalidDateFormat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateValidationUtil {
    public static LocalDate dateValidation(String date, String format) throws InvalidDateFormat {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
        DateFormat sdf = new SimpleDateFormat(format);
        sdf.setLenient(true);

        try {
            sdf.parse(date);
            return LocalDate.parse(date, dtf);
        } catch (ParseException e) {
            throw new InvalidDateFormat("Formato de fecha debe ser " + format);
        }

    }
}
