package org.example.modules;

public class Car extends Vehicle {
    public Car( String brand, String model, int year, double price, String type, String id, String extra, String category, boolean isRented) {
        super(brand,model,year, price, "car", id, extra, category, isRented);
    }

    public String toString(){
        return "brand: " + getBrand() + ", model: " + getModel() + ", year: " + getYear() + ", price: " + getPrice() + "$" + ", id: " + getId() + "\n";
    }
}