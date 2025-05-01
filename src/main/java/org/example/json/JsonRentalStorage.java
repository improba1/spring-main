package org.example.json;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import org.example.Interfaces.IRentalStorage;
import org.example.modules.Rental;
import org.example.modules.Vehicle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonRentalStorage implements IRentalStorage {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Path folderPath = Paths.get("src/main/java/org/example/rentals");
    private ArrayList<Vehicle> allVehicles;

    public JsonRentalStorage(ArrayList<Vehicle> allVehicles) {
        this.allVehicles = allVehicles;
        if (!Files.exists(folderPath)) {
            try {
                Files.createDirectories(folderPath);
            } catch (IOException e) {
                System.out.println("Error in creating directories: " + e.getMessage());
            }
        }
        load();
    }

    public void load(){
        for (Vehicle vehicle : allVehicles) {
        String vehicleId = vehicle.getId();
        Path filePath = getRentalFilePath(vehicleId);

        // Если файл не существует — создаём его с пустым rental
        if (!Files.exists(filePath)) {
            vehicle.setRental(new Rental());
            saveHistory(vehicleId, new ArrayList<>()); // Создаём файл с пустым массивом
            continue;
        }

        try {
            String json = Files.readString(filePath);
            if (json.isBlank()) {
                vehicle.setRental(new Rental());
                continue;
            }

            JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
            JsonArray timesArray = obj.getAsJsonArray("rentalTimes");

            ArrayList<String> rentalTimes = new ArrayList<>();
            if (timesArray != null) {
                for (JsonElement el : timesArray) {
                    rentalTimes.add(el.getAsString());
                }
            }

            Rental rental = new Rental();
            rental.setRentalHistory(rentalTimes);
            vehicle.setRental(rental);

        } catch (Exception e) {
            System.out.println("Error reading rental for vehicle " + vehicleId + ": " + e.getMessage());
            vehicle.setRental(new Rental());
        }
    }
    }

    private Path getRentalFilePath(String vehicleId) {
        return folderPath.resolve(vehicleId + ".json");
    }

    public void addRental(String login, String vehicleId, String startTime) {
        List<String> history = getRentalHistory(vehicleId);
        history.add(startTime);
        saveHistory(vehicleId, history);
    }

    public List<String> getRentalHistory(String vehicleId) {
        Path path = getRentalFilePath(vehicleId);
        if (!Files.exists(path)) return new ArrayList<>();

        String json = null;
        try {
            json = Files.readString(path);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
        JsonArray timesArray = obj.getAsJsonArray("rentalTimes");

        List<String> rentalTimes = new ArrayList<>();
        for (JsonElement el : timesArray) {
            rentalTimes.add(el.getAsString());
        }

        return rentalTimes;
    }

    public void deleteRentals(String vehicleId) {
        Path path = getRentalFilePath(vehicleId);
        if (Files.exists(path)) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void saveHistory(String vehicleId, List<String> history){
        JsonObject obj = new JsonObject();
        obj.addProperty("vehicleId", vehicleId);

        JsonArray times = new JsonArray();
        for (String time : history) {
            times.add(time);
        }
        obj.add("rentalTimes", times);

        try {
            Files.writeString(getRentalFilePath(vehicleId), gson.toJson(obj),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}