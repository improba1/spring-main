package org.example.json;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.Interfaces.IVehicleStorage;
import org.example.modules.Car;
import org.example.modules.Motorcycle;
import org.example.modules.UnknownVehicle;
import org.example.modules.Vehicle;

public class JsonVehicleStorage implements IVehicleStorage {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Path path;

    public JsonVehicleStorage(String filename) {
        this.path = Paths.get(filename);
    }

    public ArrayList<Vehicle> load() {
            String json = "";
            try {
                json = Files.readString(path);
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
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

            // Загружаем архив аренды
            // Map<String, List<String>> rentalArchive = new HashMap<>();
            // if (Files.exists(Path.of(archive_rental))) {
            //     String archiveJson = "";
            //     try {
            //         archiveJson = Files.readString(Path.of(archive_rental));
            //     } catch (IOException e) {
            //         System.out.println("Error: " + e.getMessage());
            //     }
            //     Type mapType = new TypeToken<Map<String, List<String>>>(){}.getType();
            //     rentalArchive = gson.fromJson(archiveJson, mapType);
            // }

            // // Применяем архив аренды к каждому Vehicle
            // if(rentalArchive!=null && !rentalArchive.isEmpty() ){
            // for (Vehicle v : vehicles) {
            //     if (rentalArchive.containsKey(v.getId())) {
            //         for (String time : rentalArchive.get(v.getId())) {
            //             v.setRentalTime(time);
            //         }
            //     }
            // }
            // }

        return vehicles;
    }

    // public void save(ArrayList<Vehicle> vehicles) {
    //     try {
    //         String json = gson.toJson(vehicles);
    //         Files.writeString(path, json, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }

    @Override
    public void removeVehicle(String vehicleId) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Path rentalsPath = Paths.get("src/main/java/org/example/rentals", vehicleId + ".json");

        try {
            if (!Files.exists(path)) return;

            String json = Files.readString(path);
            JsonArray vehiclesArray = JsonParser.parseString(json).getAsJsonArray();
            JsonArray updatedArray = new JsonArray();

            for (JsonElement elem : vehiclesArray) {
                JsonObject obj = elem.getAsJsonObject();
                if (!obj.get("id").getAsString().equals(vehicleId)) {
                    updatedArray.add(obj);
                }
            }
            Files.writeString(path, gson.toJson(updatedArray), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            if (Files.exists(rentalsPath)) {
                Files.delete(rentalsPath);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        // Path archivePath = Path.of(archive_rental);
        // if (Files.exists(archivePath)) {
        //     String archiveJson  = "";
        //     try {
        //         archiveJson = Files.readString(archivePath);
        //     } catch (IOException e) {
        //        System.out.println("Error: " + e.getMessage());
        //     }
        //     JsonElement archiveParsed = JsonParser.parseString(archiveJson);
        //     if (archiveParsed.isJsonArray()) {
        //         JsonArray archiveArray = archiveParsed.getAsJsonArray();
        //         JsonArray updatedArchive = new JsonArray();

        //         for (JsonElement elem : archiveArray) {
        //             JsonObject obj = elem.getAsJsonObject();
        //             if (!obj.get("vehicleId").getAsString().equals(vehicleId)) {
        //                 updatedArchive.add(obj);
        //             }
        //         }

        //         try {
        //             Files.writeString(archivePath, gson.toJson(updatedArchive), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        //         } catch (IOException e) {
        //             System.out.println("Error: " + e.getMessage());
        //         }
        //     }
        // }
    }

    @Override
    public void addVehicle(Vehicle vehicle) {
        Path path = Paths.get("vehicles.json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonArray vehiclesArray;

        try {
            if (Files.exists(path)) {
                String json = Files.readString(path);
                vehiclesArray = JsonParser.parseString(json).getAsJsonArray();
            } else {
                vehiclesArray = new JsonArray();
            }

            // Проверяем, существует ли уже такой ID
            for (JsonElement elem : vehiclesArray) {
                JsonObject obj = elem.getAsJsonObject();
                if (obj.get("id").getAsString().equals(vehicle.getId())) {
                    System.out.println("Vehicle with this ID already exists.");
                    return;
                }
            }

            JsonObject newVehicle = new JsonObject();
            newVehicle.addProperty("id", vehicle.getId());
            newVehicle.addProperty("model", vehicle.getModel());
            newVehicle.addProperty("brand", vehicle.getBrand());
            newVehicle.addProperty("year", vehicle.getYear());
            newVehicle.addProperty("price", vehicle.getPrice());
            newVehicle.addProperty("type", vehicle.getType());
            newVehicle.addProperty("extra", vehicle.getExtra());
            newVehicle.addProperty("is_rented", vehicle.getIsRented());
            newVehicle.addProperty("category", vehicle.getCategory());

            vehiclesArray.add(newVehicle);

            Files.writeString(path, gson.toJson(vehiclesArray), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateRentalStatus(String vehicleId, boolean isRented, String time) {
        Path path = Paths.get("vehicles.json");

        try {
            if (!Files.exists(path)) return;

            String json = Files.readString(path);
            JsonArray vehiclesArray = JsonParser.parseString(json).getAsJsonArray();

            for (JsonElement elem : vehiclesArray) {
                JsonObject vehicleObj = elem.getAsJsonObject();
                if (vehicleObj.get("id").getAsString().equals(vehicleId)) {
                    vehicleObj.addProperty("is_rented", isRented);
                    break;
                }
            }

            Files.writeString(path, new GsonBuilder().setPrettyPrinting().create().toJson(vehiclesArray),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // if (isRented) {
        //     JsonArray archiveArray = new JsonArray();
        //     if (Files.exists(Path.of(archive_rental))) {
        //         String archiveJson = "";
        //         try {
        //             archiveJson = Files.readString(Path.of(archive_rental));
        //         } catch (IOException e) {
        //             System.out.println("Error: " + e.getMessage());
        //         }
        //         archiveArray = JsonParser.parseString(archiveJson).getAsJsonArray();
        //     }

        //     boolean found = false;
        //     for (JsonElement elem : archiveArray) {
        //         JsonObject obj = elem.getAsJsonObject();
        //         if (obj.get("vehicleId").getAsString().equals(vehicleId)) {
        //             JsonArray times = obj.getAsJsonArray("startTimes");
        //             times.add(time);
        //             found = true;
        //             break;
        //         }
        //     }

        //     if (!found) {
        //         JsonObject newRecord = new JsonObject();
        //         newRecord.addProperty("vehicleId", vehicleId);
        //         JsonArray times = new JsonArray();
        //         times.add(time);
        //         newRecord.add("startTimes", times);
        //         archiveArray.add(newRecord);
        //     }

        //     try {
        //         Files.writeString(Path.of(archive_rental), gson.toJson(archiveArray),
        //                 StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        //     } catch (IOException e) {
        //         System.out.println("Error: " + e.getMessage());
        //     }
        // }
    }
}