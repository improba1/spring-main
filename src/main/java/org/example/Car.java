package org.example;

import java.io.IOException;

public class Car extends Vehicle {
    public Car( String brand, String model, int year, int price, boolean rented, String id, String extra) throws IOException {
        super(brand,model,year, price, "car", id, extra);
    }
    public String toCSV(){
        if(getIsRented())
            return getType() + ", " + getBrand() + ", " + getModel() + ", " + getYear() + ", " + getPrice() + ", rented("+ getRentalTime() + "), " + getId() + "\n";
        else
        return getType() + ", " + getBrand() + ", " + getModel() + ", " + getYear() + ", " + getPrice() + ", not rented" + ", " + getId() + "\n";
    }

    public String toString(){
        return "brand: " + getBrand() + ", model: " + getModel() + ", year: " + getYear() + ", price: " + getPrice() + "$" + ", id: " + getId() + "\n";
    }

    public String toStringForUsers(){
        return getBrand() + ", " + getModel() + ", " + getYear() + ", " + getPrice() + ", " + getId() + ", " + getRentalTime();
    }
}