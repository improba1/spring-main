package org.example.app;

import java.io.IOException;

import org.example.authentication.AuthService;
import org.example.modules.InputScanner;
import org.example.modules.User;
import org.example.repositories.UserRepository;
import org.example.repositories.VehicleRepository;


public class Main {

    
    public static void main(String[] args) throws IOException {
        while(true){
            VehicleRepository vehicleRepository = new VehicleRepository();
            UserRepository userRepository = new UserRepository();

            String input;
            while(true){
                System.out.println("Choose the source of data (json/database): ");
                input = InputScanner.SCANNER.nextLine().trim().toLowerCase();
                try{
                    switch (input) {
                        case "json":
                            vehicleRepository.setStorage(input);
                            break;
                        case "database":
                            vehicleRepository.setStorage(input);
                            break;
                        default:
                            System.out.println("Invalid choice!");
                            continue;
                    }
                    break;
                } catch(Exception e){
                    System.out.println("Error: " + e.getMessage());
                }
            }

            vehicleRepository.start();
            userRepository.start(vehicleRepository, input);
            
            User user = null;
            boolean log_out = false;

            while(true) {
                AuthService authService = new AuthService(userRepository);
                String login;
                System.out.println("Enter login:");
                login = InputScanner.SCANNER.nextLine();
                if(login.equals("log out")){
                    System.out.println("Are you sure you want to log out? y/n");
                    login = InputScanner.SCANNER.nextLine();
                    if(login.equals("y")){
                        System.out.println("Successful log out");
                        log_out = true;
                        break;
                    }
                }
                user = authService.checkLogin(login);
                if (user == null) {
                    System.out.println("Error");
                    return;
                }
                break;
            }
            
            if(log_out) continue;

            

            CommandProcessor commandProcessor = new CommandProcessor(vehicleRepository, userRepository, input);
            while(true){
                System.out.println("Enter instruction");
                String instruction = InputScanner.SCANNER.nextLine(); 
                if(instruction.equals("log out")){
                    System.out.println("Are you sure you want to log out? y/n");
                    instruction = InputScanner.SCANNER.nextLine();
                    if(instruction.equals("y")){
                        System.out.println("Successful log out");
                        break;
                    }
                }
                commandProcessor.processCommand(instruction, user);
            }

        }
    }

}
