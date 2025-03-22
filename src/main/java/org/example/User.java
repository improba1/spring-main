package org.example;

import java.util.ArrayList;

enum Role{
    ADMIN,
    USER
}

public class User {
    private String login;
    private String password;
    private Role role;
    private ArrayList<Vehicle> rentedVehicles = new ArrayList<>();

    public User(Role role, String login, String password, ArrayList<Vehicle> rentedVehicles){
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

    public Role getRole(){
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

    public String toCSV(){
        StringBuilder str = new StringBuilder();
        str.append(role + ", " + login + ", " + password + '\n');

        for(Vehicle v: rentedVehicles){
            str.append('\t' + v.toCSV());
        }

        return str.toString();
    }

    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append("login: " + login + ", role: " + role + '\n');

        for(Vehicle v: rentedVehicles){
            str.append('\t' + v.toString());
        }
        return str.toString();
    }
}
