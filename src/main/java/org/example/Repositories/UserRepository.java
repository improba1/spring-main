package org.example.Repositories;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import org.example.AuthService;
import org.example.Car;
import org.example.InputScanner;
import org.example.Motorcycle;
import org.example.Role;
import org.example.User;
import org.example.Vehicle;
import org.example.Interfaces.IUserRepository;
import org.mindrot.jbcrypt.BCrypt;

public class UserRepository implements IUserRepository{
    protected ArrayList<User> users = new ArrayList<>() {};
    VehicleRepository vehicleRepository;

    private String fileName = "users.txt";

    public void start(VehicleRepository vehicleRepository) throws IOException {
        this.vehicleRepository = vehicleRepository;
        users = load(fileName);
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

    public User addUser(String login, String password, Role role){
        User user = new User(role, login, password, new ArrayList<>());
        users.add(user);
        save();
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
        System.out.println("User not found");
        return null;
    }

    

    public ArrayList<User> load(String filename) throws IOException {
        users.clear();
        File file = new File(filename);
        if (!file.exists() || file.length() == 0) {
            System.out.println("File is empty or does not exist");
            return users;
        }

        Scanner scan = new Scanner(new File(filename));
        
        String login = null, password = null;
        Role role = null;
        ArrayList<Vehicle> rentedVehicles = new ArrayList<>() {};

        while (scan.hasNextLine()) {
            String text = scan.nextLine().trim();
            if(text.isEmpty())continue;

            String[] arr = text.trim().split(";");
            try{
                Integer.parseInt(arr[0]);

                if(login != null){
                    users.add(new User(role, login, password, rentedVehicles));
                    rentedVehicles = new ArrayList<>();
                }
                if(arr[1].trim().equals("ADMIN")){
                    role = Role.ADMIN;
                }else{
                    role = Role.USER;
                }

                login = arr[2];
                password = arr[3];

            }catch(Exception e){
                arr = text.trim().split(",\\s*");
                for (Vehicle v: vehicleRepository.getVehicles()){
                    if(v.getId().equals(arr[4])){
                        v.setRentalTime(arr[5]);
                        rentedVehicles.add(v);
                    }
                }
            }
        }

        if (login != null){
            users.add(new User(role, login, password, rentedVehicles));
        }
        return users;
    }

    public void save(){
        try (FileWriter fileWriter = new FileWriter(fileName, false)) {
            int index = 1;
            for(User v: users) {
                fileWriter.write(index + "; " + v.toCSV());
                index++;
            }
        } catch (IOException e) {
            System.out.println("Error in fileWriter");
        }
    }

    public void removeVehicle(Vehicle vehicle, String login){
        for(int i=0;i<users.size();i++){
            if(users.get(i).getLogin().equals(login)){
                users.get(i).removeVehicle(vehicle);
                save();
                return;
            }
        }
    }

    public void addVehicle(Vehicle vehicle, String login){
        for(int i=0;i<users.size();i++){
            if(users.get(i).getLogin().equals(login)){
                users.get(i).addVehicle(vehicle);
                save();
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
