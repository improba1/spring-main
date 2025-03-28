package org.example.Interfaces;

import org.example.Type;

public interface IRentalRepository {
    void rentVehicle(Type type, String login);
    void returnVehicle(Type type, String login);

    
}

