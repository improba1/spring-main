package org.example.modules;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Rental {
    private transient DateFormat df = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
    private ArrayList<String> startTimes = new ArrayList<>();

    public void addRentalTime(String time){
        startTimes.add(time);
    }

    public String getLastRentalTime(){
        if(startTimes.isEmpty()) {
            return null;
        }
        return startTimes.getLast();
    }

    public ArrayList<String> getRentalHistory(){
        return startTimes;
    }

    public void setRentalHistory(ArrayList<String> startTimes){
        this.startTimes = startTimes;
    }

    public void clearRentalHistory(){
        startTimes.clear();
    }
}
