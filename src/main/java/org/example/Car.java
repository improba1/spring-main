package org.example;

import java.io.IOException;

public class Car extends Vehicle {
    public Car( String brand, String model, int year, int price, boolean rented, int id) throws IOException {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.type = Type.CAR;
        this.rented = rented;
        this.id = id;
    }
    public String toCSV(){
        if(rented)
            return type + ", " + brand + ", " + model + ", " + year + ", " + price + ", rented" + ", " + id + "\n";
        else
            return type + ", " + brand + ", " + model + ", " + year + ", " + price + ", not rented" + ", " + id + "\n";
    }

    public String toString(){
        return "brand: " + brand + ", model: " + model + ", year: " + year + ", price: " + price + "$, " + ", id: " + id + "\n";
    }
}