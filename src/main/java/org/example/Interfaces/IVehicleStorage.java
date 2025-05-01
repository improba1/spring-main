package org.example.Interfaces;

import java.util.ArrayList;

import org.example.modules.Vehicle;

public interface IVehicleStorage {
    public ArrayList<Vehicle> load();
    public void removeVehicle(String vehicleId);
    public void addVehicle(Vehicle vehicle);
    public void updateRentalStatus(String vehicleId, boolean isRented, String time);

}
