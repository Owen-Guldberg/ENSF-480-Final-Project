package controller;

import role.*;

public class AuthenticationController {
    private RegisteredCustomer user;

    public AuthenticationController(RegisteredCustomer user) {
        this.user = user;
    }

    public boolean loginUser(String username, String password) {
        RegisteredCustomer user = user.getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            // Login successful
            return true;
        }
        return false; // Login failed
    }

    public boolean registerUser(RegisteredCustomer user) {
        if (user.getUserByUsername(user.getUsername()) != null) {
            // User already exists
            return false;
        }
        user.saveUser(user);
        return true;
    }
}
