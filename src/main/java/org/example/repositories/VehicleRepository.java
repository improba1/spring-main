package org.example.repositories;

import java.io.IOException;
import java.util.*;

import org.example.Interfaces.IVehicleRepository;
import org.example.Interfaces.IVehicleStorage;
import org.example.jdbc.VehicleDatabaseManager;
import org.example.json.JsonVehicleStorage;
import org.example.modules.Car;
import org.example.modules.InputScanner;
import org.example.modules.Motorcycle;
import org.example.modules.UnknownVehicle;
import org.example.modules.Vehicle;

enum Valid{
    TRUE,
    EXIT
}

public class VehicleRepository implements IVehicleRepository{
    private ArrayList<Vehicle> vehicles = new ArrayList<>() {};
    private String filename = "vehicles.json";
    private IVehicleStorage storage;

    public void start() throws IOException {
        vehicles = storage.load();
    }

    public IVehicleStorage getStorage(){
        return storage;
    }

    public void setStorage(String s){
        if(s.equals("json")){
            storage = new JsonVehicleStorage(filename);
        }else{
            storage = new VehicleDatabaseManager();
        }
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
        storage.removeVehicle(selectedVehicle.getId());
        System.out.print("Vehicle removed: "+ selectedVehicle.toString());
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
        if(type.equals("car")) {//brand, model, year, price, "motorcycle", id, extra, category, isRented
            vehicle = new Car(brand, model, Integer.parseInt(year), Double.parseDouble(price), "car", id, extra, category, false);
            vehicles.add(vehicle);
            System.out.print("Added car " + vehicle.toString());
        }
        else if(type.equals("motorcycle")){
            vehicle = new Motorcycle(brand, model, Integer.parseInt(year), Double.parseDouble(price), "motorcycle", id, extra, category, false);
            vehicles.add(vehicle);
            System.out.print("Added motorcycle " + vehicle.toString());
        }else{
            vehicle = new UnknownVehicle(brand, model, Integer.parseInt(year), Double.parseDouble(price), type, id, extra, category, false);
            vehicles.add(vehicle);
            System.out.print("Added " + type + " " + vehicle.toString());
        }
        storage.addVehicle(vehicle);
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

    public ArrayList<Vehicle> getVehicles(){
        ArrayList<Vehicle> newVehicles = new ArrayList<>();
        for(Vehicle v: vehicles){
            newVehicles.add(v);
        }
        return newVehicles;
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
                    System.out.print(v.toString());
                    return v;
                }
            }
            return null;
        }
}




