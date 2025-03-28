package org.example;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.example.Repositories.RentalRepository;
import org.example.Repositories.UserRepository;
import org.example.Repositories.VehicleRepository;

public class CommandProcessor {
    private VehicleRepository vehicleRepository;
    private UserRepository userRepository;
    private RentalRepository rentalRepository;
    private AuthService authService;
    private Map<String, Consumer<User>> commands = new HashMap<>();

    public CommandProcessor(VehicleRepository vehicleRepository, UserRepository userRepository){
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
        this.rentalRepository = new RentalRepository(vehicleRepository, userRepository);
        this.authService = new AuthService(userRepository);

        commands.put("add car", user -> {
            if(user.getRole().equals(Role.ADMIN)){
                try {
                    vehicleRepository.addVehicle(Type.CAR);
                } catch (IOException e) {
                    System.out.println("Error");
                }
            }else{
                System.out.println("You don't have access");
            }
        });

        commands.put("add motorcycle",user -> {
            if(user.getRole().equals(Role.ADMIN)){
                try {
                    vehicleRepository.addVehicle(Type.MOTOR);
                } catch (IOException e) {
                    System.out.println("Error");
                }
            }else{
                System.out.println("You don't have access");
            }
        });

        commands.put("rent car", user->{
            rentalRepository.rentVehicle(Type.CAR, user.getLogin());
        });

        commands.put("rent motorcycle", user->{
            rentalRepository.rentVehicle(Type.MOTOR, user.getLogin());
        });

        commands.put("return car", user->{
            rentalRepository.returnVehicle(Type.CAR, user.getLogin());
        });

        commands.put("return motorcycle", user->{
            rentalRepository.returnVehicle(Type.MOTOR, user.getLogin());
        });

        commands.put("remove vehicle", user->{
            if(user.getRole().equals(Role.ADMIN)){
                vehicleRepository.removeVehicle();
            }else{
                System.out.println("You don't have access");
            }
        });

        commands.put("get users", user->{
            if(user.getRole().equals(Role.ADMIN)){
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

        // commands.put("log out", user ->{
        //     System.exit(0);
        // });

        commands.put("get vehicles", user ->{
            if(user.getRole().equals(Role.ADMIN)){
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
