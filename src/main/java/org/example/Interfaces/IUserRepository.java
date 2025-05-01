package org.example.Interfaces;


import java.util.ArrayList;

import org.example.modules.User;

public interface IUserRepository {
    ArrayList<User> getUsers();
    User getUser(String login);
}

