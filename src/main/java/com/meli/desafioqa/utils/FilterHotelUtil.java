package com.meli.desafioqa.utils;

import com.meli.desafioqa.model.HotelDao;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class FilterHotelUtil {

    private HotelDao hotelDao;
    private Object filter;

}
