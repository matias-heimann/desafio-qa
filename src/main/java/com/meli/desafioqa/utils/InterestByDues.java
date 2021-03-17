package com.meli.desafioqa.utils;

import com.meli.desafioqa.exceptions.NotValidDuesNumber;

public class InterestByDues {

    public static Double getInterestByDues(Integer dues) throws NotValidDuesNumber {
        if(dues == null){
            throw new NotValidDuesNumber("Dues can't be null");
        }
        else if(dues == 1){
            return 0.0;
        }
        else if(dues > 1 && dues <= 3){
            return 5.0;
        }
        else if(dues > 3 && dues <= 6){
            return 10.0;
        }
        else if(dues > 6 && dues <= 12){
            return 15.0;
        }
        throw new NotValidDuesNumber("There can't be " + dues + "dues");
    }

}
