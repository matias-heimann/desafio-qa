package com.meli.desafioqa.utils;

import com.meli.desafioqa.exceptions.RoomTypeDoesNotExist;

public class RoomSizeTranslateUtil {

    public static String roomType(Integer i) throws RoomTypeDoesNotExist {
        if(i == 1){
            return "SINGLE";
        }
        else if(i == 2){
            return "DOUBLE";
        }
        else if(i == 3){
            return "TRIPLE";
        }
        else if(i == 10){
            return "MULTIPLE";
        }
        else {
            throw new RoomTypeDoesNotExist("Room with " + i + " places does not existe");
        }
    }

    public static Integer roomType(String s) throws RoomTypeDoesNotExist {
        if(s.equals("SINGLE")){
            return 1;
        }
        else if(s.equals("DOUBLE")){
            return 2;
        }
        else if(s.equals("TRIPLE")){
            return 3;
        }
        else if(s.equals("MULTIPLE")){
            return 10;
        }
        else {
            throw new RoomTypeDoesNotExist("Room type " + s + " does not existe");
        }
    }

}
