package org.example.Repositories;

import java.util.ArrayList;

import org.example.InputScanner;
import org.example.Rental;
import org.example.Vehicle;
import org.example.VehicleDatabaseManager;
import org.example.Interfaces.IRentalRepository;

public class RentalRepository implements IRentalRepository{
    private VehicleRepository vehicleRepository;
    private UserRepository userRepository;
    VehicleDatabaseManager database;

    public RentalRepository(VehicleRepository vehicleRepository, UserRepository userRepository){
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
        database = new VehicleDatabaseManager();
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
            Rental rental = new Rental();
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
        selectedVehicle.setRentTrue();
        System.out.print("Vehicle rental: "+ selectedVehicle.toString());
        userRepository.addVehicle(selectedVehicle, login);
        vehicleRepository.save();
        database.updateVehicleRentalStatus(selectedVehicle.getId(), true, login);
    }

    @Override
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
            selectedVehicle.setRentFalse();
            System.out.print("Vehicle returned: " + selectedVehicle.toString());
            userRepository.removeVehicle(selectedVehicle, login);
            vehicleRepository.save();
            database.updateVehicleRentalStatus(selectedVehicle.getId(), false, login);
        }
    }
}
