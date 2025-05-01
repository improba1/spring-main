package org.example.repositories;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import org.example.Interfaces.IUserRepository;
import org.example.Interfaces.IUserStorage;
import org.example.jdbc.UserDatabaseManager;
import org.example.json.JsonUserStorage;
import org.example.modules.User;
import org.example.modules.Vehicle;

public class UserRepository implements IUserRepository{
    protected ArrayList<User> users = new ArrayList<>() {};
    private VehicleRepository vehicleRepository;
    private String fileName = "users.json";
    private IUserStorage storage;

    public void start(VehicleRepository vehicleRepository, String s) throws IOException {
        this.vehicleRepository = vehicleRepository;
        if(s.equals("json")){
            storage = new JsonUserStorage(fileName, vehicleRepository.getVehicles());
        }else{
            storage = new UserDatabaseManager(vehicleRepository.getVehicles());
        }
        users = storage.load();
    }

    public ArrayList<User> getUsers(){
        ArrayList<User> newUser = new ArrayList<>() {};
        for(User u: users){
            newUser.add(u);
        }
        return newUser;
    }

    public ArrayList<User> getUsersInformation(){
        ArrayList<User> newUser = new ArrayList<>() {};
        int i = 1;
        for(User u: users){
            System.out.print(i++ +". " + u.toString());
            newUser.add(u);
        }
        return newUser;
    }

    public User addUser(String login, String password, String role){
        User user = new User(role, login, password, new ArrayList<>());
        users.add(user);
        storage.add(login, password, role);
        return user;
    }

    public User getUserInformation(String login){
        for(User v: users){
            if(v.getLogin().equals(login)){
                System.out.print(v.toString());
                return v;
            }
        }
        System.out.println("User not found");
        return null;
    }

    public User getUser(String login){
        for(User v: users){
            if(v.getLogin().equals(login)){
                return v;
            }
        }
        return null;
    }

    // public void save(){
    //     storage.save(users);
    // //     try (FileWriter fileWriter = new FileWriter(fileName, false)) {
    // //         int index = 1;
    // //         for(User v: users) {
    // //             fileWriter.write(index + "; " + v.toCSV());
    // //             index++;
    // //         }
    // //     } catch (IOException e) {
    // //         System.out.println("Error in fileWriter");
    // //     }
    // }

    public void returnVehicle(Vehicle vehicle, String login){
        for(int i=0;i<users.size();i++){
            if(users.get(i).getLogin().equals(login)){
                users.get(i).removeVehicle(vehicle);
                storage.saveReturn(login, vehicle.getId());
                return;
            }
        }
    }

    public void addVehicle(Vehicle vehicle, String login, String time){
        for(int i=0;i<users.size();i++){
            if(users.get(i).getLogin().equals(login)){
                users.get(i).addVehicle(vehicle);
                vehicle.setRentalTime(time);
                storage.saveRent(login, vehicle.getId(), time);
                return;
            }
        }
    }

    public ArrayList<Vehicle> getVehicles(String login){
        for(int i=0;i<users.size();i++){
            if(users.get(i).getLogin().equals(login)){
                return users.get(i).getVehicles();
                }
            }
            return null;
        }
}
