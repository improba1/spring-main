package org.example;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.security.AlgorithmConstraints;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonUserStorage {


    class RentedInfo {
    String id;
    String time;

    RentedInfo(String id, String time) {
        this.id = id;
        this.time = time;
    }
}

class UserDTO {
    String login;
    String password;
    String role;
    List<RentedInfo> rented;

    UserDTO(String login, String password, String role, List<RentedInfo> rented) {
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

    public ArrayList<User> load() throws IOException {
        if (!Files.exists(path)) return new ArrayList<>();
        try {
            String json = Files.readString(path);
            Type listType = new TypeToken<List<UserDTO>>(){}.getType();
            List<UserDTO> dtos = gson.fromJson(json, listType);

            ArrayList<User> users = new ArrayList<>();
            for (UserDTO dto : dtos) {
                ArrayList<Vehicle> rented = new ArrayList<>();
                for (RentedInfo info : dto.rented) {
                    for (Vehicle v : allVehicles) {
                        if (v.getId().equals(info.id)) {
                            v.setRentTrue();;
                            v.setRentalTime(info.time);
                            rented.add(v);
                        }
                    }
                }
                users.add(new User(dto.role, dto.login, dto.password, rented));
            }

            return users;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        // JsonVehicleStorage vehicleStorage = new JsonVehicleStorage("vehicles.json");
        // ArrayList<Vehicle> vehicles = vehicleStorage.load();
        // Map<String, Vehicle> vehicleMap = vehicles.stream().collect(Collectors.toMap(Vehicle::getId, v -> v));

        // Gson gson  = new Gson();
        // String userJson = Files.readString(Paths.get(fileName));

        // Type userListType = new TypeToken<ArrayList<User>>(){}.getType();
        // ArrayList<User> users = gson.fromJson(userJson, userListType);

        // if (!Files.exists(path)){
        //     System.out.println("File " + path + " does not exist"); 
        //     return new ArrayList<>();
        // }

        // for (User user : users) {
        //     for (Vehicle info : vehicles) {
        //         Vehicle v = vehicleMap.get(info.getId());
        //         if (v != null) {
        //             user.addVehicle(v);
        //         }
        //     }
        // }
        // return users;
        // try {
        //     String json = Files.readString(path);
        //     JsonArray array = JsonParser.parseString(json).getAsJsonArray();

        //     ArrayList<User> users = new ArrayList<>();

        //     for (JsonElement element : array) {
        //         JsonObject obj = element.getAsJsonObject();
        //         users.add(gson.fromJson(obj, User.class));
        //     }

        //     return users;

        // } catch (IOException e) {
        //     System.out.println(e.getMessage());
        //     return new ArrayList<>();
        // }
    }

    public void save(ArrayList<User> users) {
        List<UserDTO> dtos = new ArrayList<>();
        for (User user : users) {
            List<RentedInfo> rented = new ArrayList<>();
            for (Vehicle v : user.getVehicles()) {
                rented.add(new RentedInfo(v.getId(), v.getRentalTime()));
            }
            dtos.add(new UserDTO(user.getLogin(), user.getPassword(), user.getRole(), rented));
        }

        try {
            String json = gson.toJson(dtos);
            Files.writeString(path, json, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    //     try {
    //         String json = gson.toJson(users);
    //         Files.writeString(path, json, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }
    }
}