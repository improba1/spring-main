package org.example;

import com.google.gson.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;

public class JsonVehicleStorage {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Path path;

    public JsonVehicleStorage(String filename) {
        this.path = Paths.get(filename);
    }

    public ArrayList<Vehicle> load() {
        if (!Files.exists(path)){
            System.out.println("File " + path + " does not exist"); 
            return new ArrayList<>();
        }

        try {
            String json = Files.readString(path);
            JsonArray array = JsonParser.parseString(json).getAsJsonArray();

            ArrayList<Vehicle> vehicles = new ArrayList<>();

            for (JsonElement element : array) {
                JsonObject obj = element.getAsJsonObject();
                String type = obj.get("type").getAsString();

                switch (type.toLowerCase()) {
                    case "car":
                        vehicles.add(gson.fromJson(obj, Car.class));
                        break;
                    case "motorcycle":
                        vehicles.add(gson.fromJson(obj, Motorcycle.class));
                        break;
                    default:
                        vehicles.add(gson.fromJson(obj, UnknownVehicle.class));
                        break;
                }
            }

            return vehicles;

        } catch (IOException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    public void save(ArrayList<Vehicle> vehicles) {
        try {
            String json = gson.toJson(vehicles);
            Files.writeString(path, json, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}