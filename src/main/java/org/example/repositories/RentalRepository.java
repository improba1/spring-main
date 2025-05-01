package org.example.repositories;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.example.Interfaces.IRentalRepository;
import org.example.Interfaces.IRentalStorage;
import org.example.Interfaces.IVehicleStorage;
import org.example.jdbc.DatabaseRentalStorage;
import org.example.json.JsonRentalStorage;
import org.example.modules.InputScanner;
import org.example.modules.Vehicle;

public class RentalRepository implements IRentalRepository{
    private VehicleRepository vehicleRepository;
    private UserRepository userRepository;
    private IRentalStorage storage;
    private IVehicleStorage v_storage;
    
    public RentalRepository(VehicleRepository vehicleRepository, UserRepository userRepository, IVehicleStorage v_storage){
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
        this.v_storage = v_storage;
    }

    public void setStorage(String s){
        if(s.equals("json")){
            storage = new JsonRentalStorage(vehicleRepository.getVehicles());
        }else{
            storage = new DatabaseRentalStorage();
        }
    }

    public void rentVehicle(String type, String login) {
            ArrayList<Vehicle> availableVehicles = new ArrayList<>();

            for(Vehicle v: vehicleRepository.getVehicles()){
                if(v.getType().equals(type) && !v.getIsRented()){
                    availableVehicles.add(v);
                }
            }
            if(availableVehicles.isEmpty()){
                System.out.println("No vehicles available");
                return;
            }
            //Rental rental = new Rental();
            System.out.println("Which vehicle you want to rent? (0 - back)");
            for(int i =0;i<availableVehicles.size();i++){
                Vehicle v = availableVehicles.get(i);
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
        selectedVehicle.setIsRented(true);
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        selectedVehicle.setRentalTime(time.format(formatter));
        userRepository.addVehicle(selectedVehicle, login, time.format(formatter));
        storage.addRental(userRepository.getUser(login).getLogin(), selectedVehicle.getId(), time.format(formatter));
        v_storage.updateRentalStatus(selectedVehicle.getId(), true, time.format(formatter));
        System.out.print("Vehicle rental: "+ selectedVehicle.toString());
    }

    public void returnVehicle(String type, String login){
        while(true) {
            ArrayList<Vehicle> availableVehicles = new ArrayList<>();
            for (Vehicle v : userRepository.getVehicles(login)) {
                if (v.getType().equals(type)) {
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
                System.out.print(i + 1 + ". " + v.toString());
            }
            int choice;
            while (true) {
                try {
                    choice = InputScanner.SCANNER.nextInt();
                    InputScanner.SCANNER.nextLine();
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
            selectedVehicle.setIsRented(false);
            System.out.print("Vehicle returned: " + selectedVehicle.toString());
            userRepository.returnVehicle(selectedVehicle, login);
            v_storage.updateRentalStatus(selectedVehicle.getId(), false, "");
            return;
        }
    }
}
