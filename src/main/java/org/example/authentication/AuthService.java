package org.example.authentication;

import java.util.ArrayList;

import org.example.modules.InputScanner;
import org.example.modules.User;
import org.example.repositories.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {
    private UserRepository userRepository;

    public AuthService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    
    private User createUser(String login){
        while (true) {
            System.out.println("Create password: ");
            String password = InputScanner.SCANNER.nextLine();
            System.out.println("Enter your password again: ");
            String password2 = InputScanner.SCANNER.nextLine();
            if (password.equals(password2)) {
                String bcryptHash = BCrypt.hashpw(password, BCrypt.gensalt());
                User user = new User("USER", login, bcryptHash, new ArrayList<>());
                userRepository.addUser(login, bcryptHash, "USER");
                System.out.println("Login and password saved");
                return user;
            }
            System.out.println("Wrang password!");
        }
    }

    public User checkLogin(String login) {
        if (!userRepository.getUsers().isEmpty()){
            for (User u: userRepository.getUsers()) {
                if (u.getLogin().equalsIgnoreCase(login)) {
                    System.out.println("Enter password: ");
                    String password = InputScanner.SCANNER.nextLine();
                    if (BCrypt.checkpw(password, u.getPassword())) {
                        System.out.println("Welcome, " + login + "!");
                        return u;
                    } else {
                        while (true) {
                            System.out.println("Wrang! Enter password: ");
                            password = InputScanner.SCANNER.nextLine();
                            if (BCrypt.checkpw(password, u.getPassword())) {
                                System.out.println("Welcome, " + login + "!");
                                return u;
                            }
                        }
                    }

                } 
            }
            
        }
        return createUser(login);
    }
}
