package org.example;

public class Motorcycle extends Vehicle {
    String kategoria;
    public Motorcycle( String brand, String model, int year, int price, String kategoria, boolean rented, int id){
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.kategoria = kategoria;
        this.rented = rented;
        this.type = Type.MOTOR;
        this.id = id;
    }
    public String toCSV(){
        if(rented)
            return type + ", " + brand + ", " + model + ", " + year + ", " + price + ", rented" + ", " + kategoria + ", " + id + "\n";
        else
            return type + ", " + brand + ", " + model + ", " + year + ", " + price + ", not rented" + ", " + kategoria + ", " + id + "\n";
    }

    public String toString(){
        return "brand: " + brand + ", model: " + model + ", year: " + year + ", price: " + price + "$, " + ", category: " + kategoria + ", id: " + id + "\n";
    }
}