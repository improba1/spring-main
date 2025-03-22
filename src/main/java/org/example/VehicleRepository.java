package org.example;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

enum Valid{
    TRUE,
    EXIT
}

public class VehicleRepository implements IVehicleRepository{
    private ArrayList<Vehicle> vehicles = new ArrayList<>() {};
    private String filename = "vehicles.txt";

    public void start() throws IOException {
        vehicles = load(filename);
    }

    public void removeVehicle(){
        if(vehicles.isEmpty()){
            System.out.println("No vehicles available");
            return;
        }
        
        System.out.println("Which vehicle you want to remove? (0 - back)");

        for(int i =0;i<vehicles.size();i++){
            Vehicle v = vehicles.get(i);
            System.out.println(i+1 + ". " + v.brand + ", " + v.model + ", " + v.year + ", " + v.price);
        }
        int choice;
        while(true){
            //System.out.println("Enter choice");
            try{
                choice = InputScanner.SCANNER.nextInt();
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
        if(selectedVehicle.rented == true){
            System.out.println("This vehicle is rented!");
            return;
        }
        vehicles.remove(selectedVehicle);
        System.out.println("Vehicle removed: "+ selectedVehicle.brand);
        save();
    }

    public void addVehicle(Type type) throws IOException {
        String brand, model = "",year = "", price = "", category = "";
        if(type.equals(Type.CAR)) {
            System.out.println("Enter brand, model, year, price or \"exit\" to return");
        }else{ //motor
            System.out.println("Enter brand, model, year, price, category or \"exit\" to return");
        }
        brand = InputScanner.SCANNER.nextLine();
        if (brand.equalsIgnoreCase("exit")) {
            return;
        }
        model = InputScanner.SCANNER.nextLine();
        if(model.equalsIgnoreCase("exit")) {
            return;
        }
        year = InputScanner.SCANNER.nextLine();
        Valid valid = checkValid(year);
        if(valid == Valid.EXIT){
            return;
        }
        price = InputScanner.SCANNER.nextLine();
        Valid valid1 = checkValid(price);
        if(valid1 == Valid.EXIT){
            return;
        }
        if(type.equals(Type.MOTOR)) {
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
        if(type.equals(Type.CAR)) {
            Car car = new Car(brand, model, Integer.parseInt(year), Integer.parseInt(price), false, vehicles.size());
            vehicles.add(car);
            System.out.print("Added car " + car);
            save();
        }
        else{
            Motorcycle moto = new Motorcycle(brand, model, Integer.parseInt(year), Integer.parseInt(price), category, false, vehicles.size());
            vehicles.add(moto);
            System.out.print("Added motorcycle " + moto);
            save();
        }


    }
    @Override
    public void rentVehicle(Type type, UserRepository ur, String login) {
            ArrayList<Vehicle> availableVehicles = new ArrayList<>();
            for(Vehicle v: vehicles){
                if(v.type.equals(type) && !v.rented){
                    availableVehicles.add(v);
                }
            }
            if(availableVehicles.isEmpty()){
                System.out.println("No vehicles available");
                return;
            }

            System.out.println("Which vehicle you want to rent? (0 - back)");
            for(int i =0;i<availableVehicles.size();i++){
                Vehicle v = availableVehicles.get(i);
                System.out.println(i+1 + ". " + v.brand + ", " + v.model + ", " + v.year + ", " + v.price);
            }
            int choice;
            while(true){
                //System.out.println("Enter choice");
                try{
                    choice = InputScanner.SCANNER.nextInt();
                    if(choice == 0){
                        return;
                    }
                    else if(choice >= 1 && choice <= availableVehicles.size()) {
                        break;
                    }
                    else{
                        System.out.println("Invalid choice \nEnter valid choice (1 - " + availableVehicles.size() + ")");
                    }
                }catch (Exception e){
                    System.out.println("Error: invalid choice \n Enter valid choice (1 - " + availableVehicles.size() + ")");
                }
            }
        Vehicle selectedVehicle = availableVehicles.get(choice - 1);
        selectedVehicle.rented = true;
        System.out.println("Vehicle rental: "+ selectedVehicle.brand);
        ur.addVehicle(selectedVehicle, login);
        save();
    }


    @Override
    public void returnVehicle(Type type, UserRepository ur, String login){
        while(true) {
            ArrayList<Vehicle> availableVehicles = new ArrayList<>();
            for (Vehicle v : ur.getVehicles(login)) {
                if (v.type.equals(type)) {
                    availableVehicles.add(v);
                }
            }
            if (availableVehicles.isEmpty()) {
                System.out.println("No vehicles to return");
                return;
            }

            System.out.println("Which vehicle you want to return? (0 - exit)");
            for (int i = 0; i < availableVehicles.size(); i++) {
                Vehicle v = availableVehicles.get(i);
                System.out.println(i + 1 + ". " + v.brand + ", " + v.model + ", " + v.year + ", " + v.price);
            }
            int choice;
            while (true) {
                //System.out.println("Enter choice");
                try {
                    choice = InputScanner.SCANNER.nextInt();
                    if(choice == 0){
                        return;
                    }
                    else if (choice >= 1 && choice <= availableVehicles.size()) {
                        break;
                    } else {
                        System.out.println("Invalid choice\nEnter valid choice (1 - " + availableVehicles.size() + ")");
                    }
                } catch (Exception e) {
                    System.out.println("Error: invalid choice\nEnter valid choice (1 - " + availableVehicles.size() + ")");
                }
            }
            Vehicle selectedVehicle = availableVehicles.get(choice - 1);
            selectedVehicle.rented = false;
            save();
            System.out.println("Vehicle returned: " + selectedVehicle.brand);
            ur.removeVehicle(selectedVehicle, login);
            return;
        }
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
        vehicles.clear();

        File file = new File(filename);

        if (!file.exists() || file.length() == 0) {
            System.out.println("File is empty or does not exist");
            return vehicles;
        }

        Scanner scan = new Scanner(new File(filename));

        while (scan.hasNextLine()) {
            String text = scan.nextLine();
            if(text.isEmpty())continue;
            String[] arr = text.trim().split(",\\s*");
            if (arr[0].equals("MOTOR")) {  //motor String brand, String model, int year, int price, String kategoria
                //MOTOR, bajaj, pulsar, 2021, 1800, not rented, a2, 8
                // type + brand + model + year + price + rented + kategoria + id
                if (arr[5].trim().equalsIgnoreCase("rented")) {
                    Vehicle v = (new Motorcycle(arr[1], arr[2], Integer.parseInt(arr[3]), Integer.parseInt(arr[4]), arr[6], true, Integer.parseInt(arr[7])));
                    vehicles.add(v);
                } else {
                    Vehicle v = new Motorcycle(arr[1], arr[2], Integer.parseInt(arr[3]), Integer.parseInt(arr[4]), arr[6], false, Integer.parseInt(arr[7]));
                    vehicles.add(v);
                }
            } else {
                if (arr[5].trim().equalsIgnoreCase("rented")) {
                    Vehicle v = new Car(arr[1], arr[2], Integer.parseInt(arr[3]), Integer.parseInt(arr[4]), true, Integer.parseInt(arr[6]));
                    vehicles.add(v);
                } else {
                    Vehicle v = new Car(arr[1], arr[2], Integer.parseInt(arr[3]), Integer.parseInt(arr[4]), false, Integer.parseInt(arr[6]));
                    vehicles.add(v);
                    //CAR, akura, 4, 2000, 3000, not rented, 4
                }   //String brand, String model, int year, int price
                //brand + model + year + price + rented + id
            }
        }
        return vehicles;
    }

    @Override
    public ArrayList<Vehicle> getVehicles(){
        ArrayList<Vehicle> newVehicles = new ArrayList<>();
        for(Vehicle v: vehicles){
            newVehicles.add(v);
        }
        return newVehicles;
    }



    @Override
    public void save() {
        try (FileWriter fileWriter = new FileWriter(filename, false)) {
            for(Vehicle v: getVehicles()) {
                fileWriter.write(v.toCSV());
            }
        } catch (IOException e) {
            System.out.println("Error in fileWriter");
        }
    }

    public Vehicle getVehicle(String id){
        try{
            int newId = Integer.parseInt(id);
            for(Vehicle v: vehicles){
                if(v.id == newId){
                    return v;
                }
            }
            }catch(Exception e){
                System.out.println("Invalid id!");
            }
            return null;
        }
}




