package org.example.modules;

abstract public class Vehicle {
    private Rental rental = new Rental();
    private boolean isRented;
    private String model;
    private String brand;
    private int year;
    private double price;
    private String id;
    private String type;
    private String extra;
    private String category;

    Vehicle( String brand, String model, int year, double price, String type, String id, String extra, String category, boolean isRented){
        this.model = model;
        this.brand = brand;
        this.year = year;
        this.price = price;
        this.type = type;
        this.extra = extra;
        this.id = id;
        this.category = category;
        this.isRented = isRented;
    }

    public Rental getRental(){
        return rental;
    }

    public void setRental(Rental rental){
        this.rental = rental;
    }

    public void setIsRented(boolean isRented){
        this.isRented = isRented;
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

    public double getPrice() {
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

    public boolean getIsRented(){
        return isRented;
    }

    public String getRentalTime(){
        return rental.getLastRentalTime();
    }

    public void setRentalTime(String time){
        rental.addRentalTime(time);
    }

    public String toString(){
        return "brand: " + brand + ", model: " + model + ", year: " + year + ", price: " + price + "$" + ", id: " + id + "\n";
    }
}