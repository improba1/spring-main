package org.example.Repositories;

import java.io.IOException;
import java.util.*;
import org.example.Car;
import org.example.InputScanner;
import org.example.JsonVehicleStorage;
import org.example.Motorcycle;
import org.example.UnknownVehicle;
import org.example.Vehicle;
import org.example.VehicleDatabaseManager;
import org.example.Interfaces.IVehicleRepository;

enum Valid{
    TRUE,
    EXIT
}

public class VehicleRepository implements IVehicleRepository{
    private ArrayList<Vehicle> vehicles = new ArrayList<>() {};
    private String filename = "vehicles.json";
    JsonVehicleStorage storage = new JsonVehicleStorage(filename);
    VehicleDatabaseManager database;

    public void start() throws IOException {
        vehicles = load(filename);
        database = new VehicleDatabaseManager();
    }

    public void removeVehicle(){
        if(vehicles.isEmpty()){
            System.out.println("No vehicles available");
            return;
        }
        System.out.println("Which vehicle you want to remove? (0 - back)");
        for(int i =0;i<vehicles.size();i++){
            Vehicle v = vehicles.get(i);
            System.out.print(i+1 + ". " + v.toString());
        }
        int choice;
        while(true){
            //System.out.println("Enter choice");
            try{
                choice = InputScanner.SCANNER.nextInt();
                InputScanner.SCANNER.nextLine();
                if(choice == 0){
                    return;
                }
                else if(choice >= 1 && choice <= vehicles.size()) {
                    break;
                }
                else{
                    System.out.println("Invalid choice \nEnter valid choice (1 - " + vehicles.size() + ")");
                }
            }catch (Exception e){
                System.out.println("Error: invalid choice \n Enter valid choice (1 - " + vehicles.size() + ")");
            }
        }

        Vehicle selectedVehicle = vehicles.get(choice - 1);
        if(selectedVehicle.getIsRented()){
            System.out.println("This vehicle is rented!");
            return;
        }
        vehicles.remove(selectedVehicle);
        System.out.print("Vehicle removed: "+ selectedVehicle.toString());
        database.removeVehicle(selectedVehicle.getId());
        save();
    }

    public void addVehicle(String type) throws IOException {
        String brand, model = "",year = "", price = "", category = "", extra = "", id = UUID.randomUUID().toString();
        System.out.println("Enter brand (or exit): ");
        brand = InputScanner.SCANNER.nextLine();
        if (brand.equalsIgnoreCase("exit")) {
            return;
        }
        System.out.println("Enter model (or exit): ");
        model = InputScanner.SCANNER.nextLine();
        if(model.equalsIgnoreCase("exit")) {
            return;
        }
        System.out.println("Enter year (or exit): ");
        year = InputScanner.SCANNER.nextLine();
        Valid valid = checkValid(year);
        if(valid == Valid.EXIT){
            return;
        }
        System.out.println("Enter price (or exit): ");
        price = InputScanner.SCANNER.nextLine();
        Valid valid1 = checkValid(price);
        if(valid1 == Valid.EXIT){
            return;
        }
        if(type.equalsIgnoreCase("motorcycle")) {
            System.out.println("Enter category (or exit): ");
            category = InputScanner.SCANNER.nextLine();
            if(category.equalsIgnoreCase("exit")) {
                return;
            }else if(!category.equals("A") && !category.equals("A1") && !category.equals("A2") ) {
                while(true){
                    System.out.println("Please enter a valid category (A, A1, A2)");
                    category = InputScanner.SCANNER.nextLine();
                    if(category.equalsIgnoreCase("exit")) {
                        return;
                    }
                    if(!category.equals("A") && !category.equals("A1") && !category.equals("A2") ){
                        continue;
                    }
                    break;
                }
            }
        }
        System.out.println("Enter extra information: ");
            extra = InputScanner.SCANNER.nextLine();
            if(extra.equalsIgnoreCase("exit")){
                return;
            }
        Vehicle vehicle;
        if(type.equals("car")) {
            vehicle = new Car(brand, model, Integer.parseInt(year), Integer.parseInt(price), false, id, extra);
            vehicles.add(vehicle);
            System.out.print("Added car " + vehicle.toString());
        }
        else if(type.equals("motorcycle")){
            vehicle = new Motorcycle(brand, model, Integer.parseInt(year), Integer.parseInt(price), category, false, id, extra);
            vehicles.add(vehicle);
            System.out.print("Added motorcycle " + vehicle.toString());
        }else{
            vehicle = new UnknownVehicle(brand, model, Integer.parseInt(year), Integer.parseInt(price), type, id, extra);
            vehicles.add(vehicle);
            System.out.print("Added " + type + " " + vehicle.toString());
        }
        save();
        database.addVehicle(vehicle);
    }
    
    Valid checkValid(String number){
        if(number.equalsIgnoreCase("exit")) {
            return Valid.EXIT;
        }
        try{
            Integer.parseInt(number);
        }catch (NumberFormatException e){
            System.out.println("Invalid data");
            while(true) {
                System.out.println("Enter data");
                number = InputScanner.SCANNER.nextLine();
                if(number.equalsIgnoreCase("exit")) {
                    return Valid.EXIT;
                }
                try{
                    Integer.parseInt(number);
                }catch (NumberFormatException e1) {
                    System.out.println("Invalid data");
                    continue;
                }
                return Valid.TRUE;
            }
        }
        return null;
    }

    @Override
    public ArrayList<Vehicle> load(String filename) throws IOException {
        return storage.load();
        // vehicles.clear();
        // File file = new File(filename);
        // if (!file.exists() || file.length() == 0) {
        //     System.out.println("File is empty or does not exist");
        //     return vehicles;
        // }
        // Scanner scan = new Scanner(new File(filename));
        // while (scan.hasNextLine()) {
        //     String text = scan.nextLine();
        //     if(text.isEmpty())continue;
        //     String[] arr = text.trim().split(",\\s*");
        //     if (arr[0].equals("MOTOR")) { 
        //         if (arr[5].trim().equalsIgnoreCase("rented")) {
        //             Vehicle v = (new Motorcycle(arr[1], arr[2], Integer.parseInt(arr[3]), Integer.parseInt(arr[4]), arr[6], true, arr[7]));
        //             vehicles.add(v);
        //         } else {
        //             Vehicle v = new Motorcycle(arr[1], arr[2], Integer.parseInt(arr[3]), Integer.parseInt(arr[4]), arr[6], false, arr[7]);
        //             vehicles.add(v);
        //         }
        //     } else {
        //         if (arr[5].trim().equalsIgnoreCase("rented")) {
        //             Vehicle v = new Car(arr[1], arr[2], Integer.parseInt(arr[3]), Integer.parseInt(arr[4]), true, arr[6]);
        //             vehicles.add(v);
        //         } else {
        //             Vehicle v = new Car(arr[1], arr[2], Integer.parseInt(arr[3]), Integer.parseInt(arr[4]), false, arr[6]);
        //             vehicles.add(v);
        //         } 
        //     }
        // }
        // return vehicles;
    }

    @Override
    public ArrayList<Vehicle> getVehicles(){
        ArrayList<Vehicle> newVehicles = new ArrayList<>();
        for(Vehicle v: vehicles){
            newVehicles.add(v);
        }
        return newVehicles;
    }


    public void save() {
        storage.save(vehicles);
    //     try (FileWriter fileWriter = new FileWriter(filename, false)) {
    //         for(Vehicle v: getVehicles()) {
    //             fileWriter.write(v.toCSV());
    //         }
    //     } catch (IOException e) {
    //         System.out.println("Error in fileWriter");
    //     }
    }

    public Vehicle getVehicle(String id){
            for(Vehicle v: vehicles){
                if(v.getId().equals(id)){
                    return v;
                }
            }
            return null;
        }

        public Vehicle getVehicleInformation(String id){
            for(Vehicle v: vehicles){
                if(v.getId().equals(id)){
                    System.out.print(v.toCSV());
                    return v;
                }
            }
            return null;
        }
}




