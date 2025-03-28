package org.example;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Rental {
    private String startTime;
    private boolean isRented;
    private DateFormat df = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");


    public String getRentalTime(){
        return startTime;
    }

    public void setIsRented(boolean rental){
        this.isRented = rental;
    }

    public void setStartTime(String time){
        this.startTime = time;
    }

    public String getStartTime(){
        return startTime;
    }

    public boolean getIsRented(){
        return isRented;
    }

    public void setRentTrue(){
        isRented = true;
        startTime = df.format(new Date());
    }

    public void setRentFalse(){
        isRented = false;
        startTime = "NULL";
    }
    
}
