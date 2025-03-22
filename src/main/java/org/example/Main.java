package org.example;

import java.io.IOException;

public class Main {

    
    public static void main(String[] args) throws IOException {

        VehicleRepository vehicleRepository = new VehicleRepository();
        //UserRepository userRepository = new UserRepository();
        UserRepository userRepository = new UserRepository();

        vehicleRepository.start();
        userRepository.start();
        
        String login;

        User user = null;

        while(true) {
            System.out.println("Enter login:");
            login = InputScanner.SCANNER.nextLine();
            user = userRepository.checkLogin(login);
            if (user == null) {
                System.out.println("Error");
                return;
            }
            break;
        }

        while(true){
            System.out.println("Enter instruction");
            String instruction = InputScanner.SCANNER.nextLine(); //add car, add motorcycle, rent car, rent motorcycle

            if(instruction.equals("add car")){
                if(user.getRole().equals(Role.ADMIN)) {
                    vehicleRepository.addVehicle(Type.CAR);
                }else{
                    System.out.println("Command not found");
                }
                continue;
            }else if(instruction.equals("add motorcycle")){
                if(user.getRole().equals(Role.ADMIN)) {
                    vehicleRepository.addVehicle(Type.MOTOR);
                }else{
                    System.out.println("Command not found");
                }
                continue;
            }else if(instruction.equals("rent car")){

                vehicleRepository.rentVehicle(Type.CAR, userRepository, login);
                continue;
            }else if(instruction.equals("rent motorcycle")){

                vehicleRepository.rentVehicle(Type.MOTOR, userRepository, login);
                userRepository.save();
                continue;
            }else if(instruction.equals("return car")){

                vehicleRepository.returnVehicle(Type.CAR, userRepository, login);
                userRepository.save();
                continue;
            }else if(instruction.equals("return motorcycle")){
                vehicleRepository.returnVehicle(Type.MOTOR, userRepository, login);
                userRepository.save();
                continue;
            }else if(instruction.equals("remove vehicle")){
                if(user.getRole().equals(Role.ADMIN)) {
                    vehicleRepository.removeVehicle();
                }
                else{
                    System.out.println("Command not found");
                }
                continue;
            }else if(instruction.equals("get users")){
                if(user.getRole().equals(Role.ADMIN)) {
                    userRepository.getUsers();
                }else{
                    System.out.println("Command not found");
                }
                continue;
            }else if(instruction.equalsIgnoreCase("get user")){
                userRepository.getUser(login);
                continue;

            }else if(instruction.equalsIgnoreCase("get vehicle")){
                System.out.println("Enter id of vehicle: ");
                String id = InputScanner.SCANNER.nextLine();
                vehicleRepository.getVehicle(id);
                continue;
            }
            System.out.println("Command not found");
        }

    }

}
