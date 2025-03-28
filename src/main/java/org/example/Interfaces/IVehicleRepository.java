package org.example.Interfaces;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.example.Type;
import org.example.Vehicle;
import org.example.Repositories.UserRepository;

public interface IVehicleRepository {

    //void rentVehicle(Type type, UserRepository ur, String login) throws IOException;
    //void returnVehicle(Type type, UserRepository ur, String login) throws IOException;
    ArrayList<Vehicle> getVehicles() throws FileNotFoundException, IOException;
    void save() throws IOException;
    ArrayList<Vehicle> load(String filename) throws IOException;
    void addVehicle(Type type) throws IOException;
    void removeVehicle() throws IOException;
    Vehicle getVehicle(String id);


}