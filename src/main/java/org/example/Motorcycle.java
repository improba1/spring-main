package org.example;

public class Motorcycle extends Vehicle {
    private String kategoria;
    public Motorcycle( String brand, String model, int year, int price, String kategoria, boolean rented, String id){
        super.setBrand(brand);
        super.setModel(model);
        super.setModel(model);
        super.setYear(year);
        super.setPrice(price);
        this.kategoria = kategoria;
        super.setType(Type.MOTOR);
        super.setId(id);
        super.setIsRented(rented);
    }
    public String toCSV(){
        if(getIsRented())
            return getType() + ", " + getBrand() + ", " + getModel() + ", " + getYear() + ", " + getPrice() + ", rented("+ getRentalTime() + "), " + kategoria + ", " + getId() + "\n";
        else
        return getType() + ", " + getBrand() + ", " + getModel() + ", " + getYear() + ", " + getPrice() + ", not rented" + ", " + kategoria + ", " + getId() + "\n";
    }

    public String toString(){
        return "brand: " + getBrand() + ", model: " + getModel() + ", year: " + getYear() + ", price: " + getPrice() + "$" + ", category: " + kategoria + ", id: " + getId() + "\n";
    }

    public String toStringForUsers(){
        return getBrand() + ", " + getModel() + ", " + getYear() + ", " + getPrice() + ", " + getId() + ", " + getRentalTime() + ", " + kategoria;
    }
}