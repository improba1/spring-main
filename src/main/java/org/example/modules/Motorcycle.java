package org.example.modules;

public class Motorcycle extends Vehicle {
    public Motorcycle( String brand, String model, int year, double price, String type, String id, String extra, String category, boolean isRented){
        super(brand, model, year, price, "motorcycle", id, extra, category, isRented);
    }

    public String toString(){
        return "brand: " + getBrand() + ", model: " + getModel() + ", year: " + getYear() + ", price: " + getPrice() + "$" + ", category: " + getCategory() + ", id: " + getId() + "\n";
    }
}