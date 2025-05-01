package org.example.Interfaces;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.example.modules.Vehicle;
import org.example.repositories.UserRepository;

public interface IVehicleRepository {

    //void rentVehicle(Type type, UserRepository ur, String login) throws IOException;
    //void returnVehicle(Type type, UserRepository ur, String login) throws IOException;
    ArrayList<Vehicle> getVehicles() throws FileNotFoundException, IOException;
    void addVehicle(String type) throws IOException;
    void removeVehicle() throws IOException;
    Vehicle getVehicle(String id);


}