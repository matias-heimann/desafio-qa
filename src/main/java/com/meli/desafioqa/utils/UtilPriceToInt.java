package com.meli.desafioqa.utils;

import com.meli.desafioqa.exceptions.InvalidPriceFormat;
import org.apache.commons.lang3.math.NumberUtils;

public class UtilPriceToInt {

    public static Integer priceToInt(String price) throws InvalidPriceFormat {
        if(price == null){
            throw new InvalidPriceFormat("Price can't be null");
        }
        if(price.charAt(0) != '$'){
            throw new InvalidPriceFormat("Price must start with $");
        }
        price = price.substring(1);

        if(!NumberUtils.isCreatable(price)){
            throw new InvalidPriceFormat("Invalid number");
        }

        return Integer.valueOf(price);
    }

    public static Integer priceToIntWithDots(String price) throws InvalidPriceFormat {
        if(price == null){
            throw new InvalidPriceFormat("Price can't be null");
        }
        if(price.charAt(0) != '$'){
            throw new InvalidPriceFormat("Price must start with $");
        }
        price = price.substring(1);

        String splitPrice[] = price.split("\\.");

        int totalPrice = 0;
        for(int i = 0; i < splitPrice.length; i++){
            if(i != 0 && splitPrice[i].length() != 3){
                throw new InvalidPriceFormat("Invalid dot point position");
            } else if (i == 0 && splitPrice[i].length() > 3) {
                throw new InvalidPriceFormat("Invalid dot point position");
            }
            if(!NumberUtils.isCreatable(splitPrice[i])){
                throw new InvalidPriceFormat("Invalid number");
            }
            totalPrice *= 1000;
            totalPrice += Integer.valueOf(splitPrice[i]);
        }
        return totalPrice;
    }

}
