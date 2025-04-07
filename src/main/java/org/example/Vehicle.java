package org.example;

abstract public class Vehicle {
    private Rental rental = new Rental();
    private String model;
    private String brand;
    private int year;
    private int price;
    private String id;
    private String type;
    private String extra;
    private String category;

    Vehicle( String brand, String model, int year, int price, String type, String id, String extra){
        this.model = model;
        this.brand = brand;
        this.year = year;
        this.price = price;
        this.type = type;
        this.extra = extra;
        this.id = id;
        this.rental.setIsRented(false);
    }

    public String getCategory(){
        return category;
    }

    public String getExtra(){
        return extra;
    }

    public void setExtra(String extra){
        this.extra = extra;
    }

    public void setType(String type){
        this.type = type;
    }

    public String getType(){
        return type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
     
    public void setModel(String model) {
        this.model = model;
    }

    public String getModel(){
        return model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
     
    public String getId() {
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setRentTrue(){
        rental.setRentTrue();
    }

    public void setRentFalse(){
        rental.setRentFalse();
    }

    public void setIsRented(boolean isRented){
        this.rental.setIsRented(isRented);
    }

    public boolean getIsRented(){
        return rental.getIsRented();
    }

    public String getRentalTime(){
        return rental.getRentalTime();
    }

    public void setRentalTime(String time){
        rental.setStartTime(time);
    }
    

    public String toCSV(){
        if(rental.getIsRented())
            return type + ", " + brand + ", " + model + ", " + year + ", " + price + ", rented ("+ getRentalTime() + "), " + id + "\n";
        else
            return type + ", " + brand + ", " + model + ", " + year + ", " + price + ", not rented" + ", " + id + "\n";
    }

    public String toString(){
        return "brand: " + brand + ", model: " + model + ", year: " + year + ", price: " + price + "$" + ", id: " + id + "\n";
    }

    public String toStringForUsers(){
        return brand + ", " + model + ", " + year + ", " + price  + ", " + id + ", " + rental.getStartTime();
    }
}