package org.example;

import java.util.ArrayList;

import org.apache.commons.codec.digest.DigestUtils;

public class Authentication {

    public boolean checkLogin(String login, ArrayList<User> users) {
        if (!users.isEmpty()){
            for (User u: users) {
                if (u.getLogin().equalsIgnoreCase(login)) {
                    System.out.println("Enter password: ");
                    String password = InputScanner.SCANNER.nextLine();
                    String passwordHASH = new DigestUtils("SHA3-256").digestAsHex(password);
                    if (passwordHASH.equals(u.getPassword())) {
                        System.out.println("Welcome, " + login + "!");
                        return true;
                    } else {
                        while (true) {
                            System.out.println("Wrang! Enter password: ");
                            password = InputScanner.SCANNER.nextLine();
                            passwordHASH = new DigestUtils("SHA3-256").digestAsHex(password);
                            if (passwordHASH.equals(u.getPassword())) {
                                System.out.println("Welcome, " + login + "!");
                                return true;
                            }
                        }
                    }

                } 
            }
            
        }
        return false;
    }
}
