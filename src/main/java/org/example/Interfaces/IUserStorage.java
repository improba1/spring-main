package org.example.Interfaces;

import java.util.ArrayList;

import org.example.modules.User;

public interface IUserStorage {
    public void saveRent(String login, String vehicle_id, String rentTime);
    public void delete(String login);
    public void saveReturn(String login, String vehicleId);
    public ArrayList<User> load();
    public void add(String login, String password, String role);
}
