package org.example.app;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.example.authentication.AuthService;
import org.example.modules.InputScanner;
import org.example.modules.User;
import org.example.modules.Vehicle;
import org.example.repositories.RentalRepository;
import org.example.repositories.UserRepository;
import org.example.repositories.VehicleRepository;

public class CommandProcessor {
    private VehicleRepository vehicleRepository;
    private UserRepository userRepository;
    private RentalRepository rentalRepository;
    private AuthService authService;
    private Map<String, Consumer<User>> commands = new HashMap<>();

    public CommandProcessor(VehicleRepository vehicleRepository, UserRepository userRepository, String s){
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
        this.rentalRepository = new RentalRepository(vehicleRepository, userRepository, vehicleRepository.getStorage());
        rentalRepository.setStorage(s);
        this.authService = new AuthService(userRepository);

        commands.put("add vehicle", user -> {
            if(user.getRole().equals("ADMIN")){
                System.out.println("Ender a type of vehicle: ");
                String type = InputScanner.SCANNER.nextLine();
                try {
                    if(type.equalsIgnoreCase("car")){
                    vehicleRepository.addVehicle("car");
                    }else if(type.equalsIgnoreCase("motorcycle")){
                        vehicleRepository.addVehicle("motorcycle");
                    }else{
                        vehicleRepository.addVehicle(type);
                    }
                } catch (IOException e) {
                    System.out.println("Error");
                }
            }else{
                System.out.println("You don't have access");
            }
        });

        commands.put("rent vehicle", user->{
            System.out.println("Enter a type of vehicle: ");
            String type = InputScanner.SCANNER.nextLine();
            if(type.equalsIgnoreCase("car")){
                rentalRepository.rentVehicle("car", user.getLogin());
            }else if(type.equalsIgnoreCase("motorcycle")){
                rentalRepository.rentVehicle("motorcycle", user.getLogin());
            }else{
                rentalRepository.rentVehicle(type, user.getLogin());
            }
            
        });

        commands.put("return vehicle", user->{
            System.out.println("Enter a type of vehicle: ");
            String type = InputScanner.SCANNER.nextLine();
            if (type.equalsIgnoreCase("car")){
                rentalRepository.returnVehicle("car", user.getLogin());
            }else if(type.equalsIgnoreCase("motorcycle")){
                rentalRepository.returnVehicle("motorcycle", user.getLogin());
            }else{
                rentalRepository.returnVehicle(type, user.getLogin());
            }
           
        });

        commands.put("remove vehicle", user->{
            if(user.getRole().equals("ADMIN")){
                vehicleRepository.removeVehicle();
            }else{
                System.out.println("You don't have access");
            }
        });

        commands.put("get users", user->{
            if(user.getRole().equals("ADMIN")){
                userRepository.getUsersInformation();
            }else{
                System.out.println("You don't have access");
            }
        });

        commands.put("get user", user->{
            userRepository.getUserInformation(user.getLogin());
        }); 

        commands.put("get vehicle", user->{
            System.out.println("Enter id of vehicle: ");
            String id = InputScanner.SCANNER.nextLine();
            vehicleRepository.getVehicleInformation(id);           
        });

        commands.put("get vehicles", user ->{
            if(user.getRole().equals("ADMIN")){
                for(Vehicle v: vehicleRepository.getVehicles()){
                    System.out.print(v.toString());
                }
            }else{
                for(Vehicle v: vehicleRepository.getVehicles()){
                    if(!v.getIsRented())
                    System.out.print(v.toString());
                }
            }
        });

        commands.put("help", user ->{
            for (String command : commands.keySet()) { 
                System.out.println("- " + command);
            }
            System.out.println("- log out");
        });
    }

    public void processCommand(String instruction, User user) {
        commands.getOrDefault(instruction, u -> System.out.println("Command not found"))
                .accept(user);
    }
}
