package org.example;


import java.util.ArrayList;

public interface IUserRepository {
    ArrayList<User> getUsers();
    User getUser(String login);
    void save();

    
}

