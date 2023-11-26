package controller;

import role.*;

import java.util.ArrayList;

import database.*;

public class AuthenticationController {
    private RegisteredCustomer user;
    private Database db;

    public AuthenticationController(RegisteredCustomer user, Database db) {
        this.user = user;
        this.db = db;
    }

    public boolean loginUser(String email, String password) {

        if (user != null && user.getPassword().equals(password)) {
            // Login successful
            return true;
        }
        return false; // Login failed
    }

    public boolean registerUser(RegisteredCustomer user) {
        ArrayList<RegisteredCustomer> passengerData = db.getRegisteredCustomerData();
        for(RegisteredCustomer passenger : passengerData){
            if(user == passenger){
                return false;
            }
        }
        // user doesnt exsist in RegisteredCustomer
        db.saveUser(user);
        return true;
    }
}
