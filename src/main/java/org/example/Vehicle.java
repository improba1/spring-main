package org.example;

enum Type{
    CAR,
    MOTOR
}
abstract class Vehicle {
    String brand;
    String model;
    int year;
    int price;
    boolean rented;
    int id;
    Type type;
    

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