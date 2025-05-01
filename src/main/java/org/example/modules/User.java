package org.example.modules;

import java.util.ArrayList;

public class User {
    private String login;
    private String password;
    private String role;
    private transient ArrayList<Vehicle> rentedVehicles = new ArrayList<>();

    public User(String role, String login, String password, ArrayList<Vehicle> rentedVehicles){
        this.login = login;
        this.password = password;
        this.role = role;
        this.rentedVehicles = rentedVehicles;
    }

    public String getLogin(){
        return login;
    }

    public String getPassword(){
        return password;
    }

    public String getRole(){
        return role;
    }

    public ArrayList<Vehicle> getVehicles(){
        return rentedVehicles;
    }

    public void removeVehicle(Vehicle vehicle){
        rentedVehicles.remove(vehicle);
    }

    public void addVehicle(Vehicle vehicle){
        rentedVehicles.add(vehicle);
    }

    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append("login: " + login + ", role: " + role + '\n');

        for(Vehicle v: rentedVehicles){
            str.append('\t' + v.toString() + '\n');
        }
        return str.toString();
    }
}
