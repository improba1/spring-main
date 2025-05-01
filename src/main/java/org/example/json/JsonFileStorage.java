package org.example.json;
// package org.example;

// import com.google.gson.Gson;

// import java.io.IOException;
// import java.lang.reflect.Type;
// import java.nio.file.*;
// import java.util.ArrayList;
// import java.util.List;
// import com.google.gson.GsonBuilder;
// import com.google.gson.TypeAdapterFactory;
// import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;


// /*4. Использование:
// java
// Копировать
// Редактировать
// import com.google.gson.reflect.TypeToken;

// Type vehicleListType = new TypeToken<List<Vehicle>>() {}.getType();
// JsonFileStorage<Vehicle> storage = new JsonFileStorage<>("vehicles.json", vehicleListType);

// // Пример добавления
// List<Vehicle> vehicles = storage.load();
// vehicles.add(new Car("Toyota", "Camry", 2020, 20000, false, "ID123", "Some extra info"));
// storage.save(vehicles);*/


// /**/

// public class JsonFileStorage<T> {

//     private final Gson gson = new Gson();
//     private final Path path;
//     private final Type type;

//     public JsonFileStorage(String filename, Type type) {
//         this.path = Paths.get(filename);
//         this.type = type;

//         RuntimeTypeAdapterFactory<Vehicle> vehicleAdapterFactory = RuntimeTypeAdapterFactory
//                 .of(Vehicle.class, "type") // ключ `type` будет хранить тип
//                 .registerSubtype(Car.class, "car")
//                 .registerSubtype(Motorcycle.class, "motorcycle")
//                 .registerSubtype(UnknownVehicle.class, "unknown");

//         this.gson = new GsonBuilder()
//                 .registerTypeAdapterFactory(vehicleAdapterFactory)
//                 .setPrettyPrinting()
//                 .create();
//     }

//     public List<T> load() {
//         if (!Files.exists(path)) return new ArrayList<>();
//         try {
//             String json = Files.readString(path);
//             List<T> list = gson.fromJson(json, type);
//             return list != null ? list : new ArrayList<>();
//         } catch (IOException e) {
//             e.printStackTrace();
//             return new ArrayList<>();
//         }
//     }

//     public void save(List<T> data) {
//         try {
//             String json = gson.toJson(data);
//             Files.writeString(path, json, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }

// }