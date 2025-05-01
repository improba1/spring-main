package org.example.Interfaces;


public interface IRentalRepository {
    void rentVehicle(String type, String login);
    void returnVehicle(String type, String login);

    
}

