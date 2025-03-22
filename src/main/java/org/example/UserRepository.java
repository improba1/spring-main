package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.commons.codec.digest.DigestUtils;

public class UserRepository implements IUserRepository{
    protected ArrayList<User> users = new ArrayList<>() {};
    Authentication authentication = new Authentication();

    private String fileName = "users.txt";

    public void start() throws IOException {
        users = load(fileName);
    }

    public User checkLogin(String login) {
        if(!authentication.checkLogin(login, users)){
            return createUser(login);
        }else{
            return getUser(login);
        }
    }

    private User createUser(String login){
        while (true) {
            System.out.println("Create password: ");
            String password = InputScanner.SCANNER.nextLine();
            System.out.println("Enter your password again: ");
            String password2 = InputScanner.SCANNER.nextLine();
            if (password.equals(password2)) {
                String sha3Hex = new DigestUtils("SHA3-256").digestAsHex(password);
                User user = new User(Role.USER, login, sha3Hex, new ArrayList<>());
                users.add(user);
                save();
                System.out.println("Login and password saved");
                return user;
            }
            System.out.println("Wrang password!");
        }
    }

    public ArrayList<User> getUsers(){
        ArrayList<User> newUser = new ArrayList<>() {};
        int i = 1;
        for(User u: users){
            System.out.println(i++ +". " + u.toString());
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

            String[] arr = text.trim().split("[ ,\\.]+");
            try{
                Integer.parseInt(arr[0]);

                if(login != null){
                    users.add(new User(role, login, password, rentedVehicles));
                    rentedVehicles = new ArrayList<>();
                }
                if(arr[1].equals("ADMIN")){
                    role = Role.ADMIN;
                }else{
                    role = Role.USER;
                }

                login = arr[2];
                password = arr[3];

            }catch(Exception e){
                arr = text.trim().split(",\\s*");
                if(arr[0].equalsIgnoreCase("CAR")){
                    if (arr[5].trim().equalsIgnoreCase("rented")) {
                        Vehicle v = new Car(arr[1], arr[2], Integer.parseInt(arr[3]), Integer.parseInt(arr[4]), true, Integer.parseInt(arr[6]));
                        rentedVehicles.add(v);
                    } else {
                        Vehicle v = new Car(arr[1], arr[2], Integer.parseInt(arr[3]), Integer.parseInt(arr[4]), false, Integer.parseInt(arr[6]));
                        rentedVehicles.add(v);
                    }
                }else{
                    if (arr[5].trim().equalsIgnoreCase("rented")) {
                        Vehicle v = (new Motorcycle(arr[1], arr[2], Integer.parseInt(arr[3]), Integer.parseInt(arr[4]), arr[6], true, Integer.parseInt(arr[7])));
                        rentedVehicles.add(v);
                    } else {
                        Vehicle v = new Motorcycle(arr[1], arr[2], Integer.parseInt(arr[3]), Integer.parseInt(arr[4]), arr[6], false, Integer.parseInt(arr[7]));
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
                fileWriter.write(index + ". " + v.toCSV());
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
