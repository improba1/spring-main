package org.example.json;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import org.example.Interfaces.IUserStorage;
import org.example.modules.User;
import org.example.modules.Vehicle;

public class JsonUserStorage implements IUserStorage {

    static class RentedVehicle {
        String id;
        String time;

        RentedVehicle(String id, String time) {
            this.id = id;
            this.time = time;
        }
    }

    static class UserDTO {
        String login;
        String password;
        String role;
        ArrayList<RentedVehicle> rented;

        UserDTO(String login, String password, String role, ArrayList<RentedVehicle> rented) {
            this.login = login;
            this.password = password;
            this.role = role;
            this.rented = rented;
        }
    }

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Path path;
    ArrayList<Vehicle> allVehicles;
    String fileName = "users.json";

    public JsonUserStorage(String filename, ArrayList<Vehicle> allVehicles) {
        this.path = Paths.get(filename);
        this.allVehicles = allVehicles;
    }

    public ArrayList<User> load(){
        ArrayList<User> users = new ArrayList<>();

        try {
            // Считываем JSON из файла
            Path path = Paths.get(fileName);
            if (!Files.exists(path)) {
                System.out.println("File " + fileName + " does not exists");
                return users;
            }

            String json = Files.readString(path);
            Gson gson = new Gson();

            Type listType = new TypeToken<ArrayList<UserDTO>>(){}.getType();
            ArrayList<UserDTO> userDTOs = gson.fromJson(json, listType);

            // Обрабатываем пользователей
            for (UserDTO dto : userDTOs) {
                ArrayList<Vehicle> rentedVehicles = new ArrayList<>();

                for (RentedVehicle rentedVehicle : dto.rented) {
                    Vehicle selected = null;
                    for (Vehicle v : allVehicles) {
                        if (v.getId().equals(rentedVehicle.id)) {
                            v.setIsRented(true);  // помечаем как арендованный
                            selected = v;
                            break;
                        }
                    }

                    if (selected != null) {
                        rentedVehicles.add(selected);
                    } else {
                        System.out.println("Vehicle not found for ID: " + rentedVehicle.id);
                    }
                }

                users.add(new User(dto.role, dto.login, dto.password, rentedVehicles));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return users;
    }

    private ArrayList<UserDTO> loadUsersFromFile() throws IOException {
        String json = Files.readString(path);
        Type listType = new TypeToken<List<UserDTO>>(){}.getType();
        return gson.fromJson(json, listType);
    }

    private void saveUsersToFile(ArrayList<UserDTO> users) throws IOException {
        String json = gson.toJson(users);
        Files.writeString(path, json, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public void saveRent(String login, String vehicleId, String rentTime){
        try {
            // Загружаем всех пользователей
            ArrayList<UserDTO> users = loadUsersFromFile();

            // Ищем пользователя
            for (UserDTO user : users) {
                if (user.login.equals(login)) {
                    user.rented.add(new RentedVehicle(vehicleId, rentTime));  // Примерное время аренды
                    saveUsersToFile(users);
                    return;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveReturn(String login){
        try {
            // Загружаем всех пользователей
            ArrayList<UserDTO> users = loadUsersFromFile();

            // Ищем пользователя и очищаем список арендуемых автомобилей
            for (UserDTO user : users) {
                if (user.login.equals(login)) {
                    user.rented.clear();
                    saveUsersToFile(users);
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete(String login){
        try {
            // Загружаем всех пользователей
            ArrayList<UserDTO> users = loadUsersFromFile();

            // Удаляем пользователя
            users.removeIf(user -> user.login.equals(login));
            saveUsersToFile(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add(String login, String password, String role) {
        try {
            // Загружаем всех пользователей
            ArrayList<UserDTO> users = loadUsersFromFile();

            // Добавляем нового пользователя
            users.add(new UserDTO(login, password, role, new ArrayList<>()));
            saveUsersToFile(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // public void save(ArrayList<User> users) {
    //     List<UserDTO> dtos = new ArrayList<>();
    //     for (User user : users) {
    //         List<RentedInfo> rented = new ArrayList<>();
    //         for (Vehicle v : user.getVehicles()) {
    //             rented.add(new RentedInfo(v.getId(), v.getRentalTime()));
    //         }
    //         dtos.add(new UserDTO(user.getLogin(), user.getPassword(), user.getRole(), rented));
    //     }

    //     try {
    //         String json = gson.toJson(dtos);
    //         Files.writeString(path, json, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }
}