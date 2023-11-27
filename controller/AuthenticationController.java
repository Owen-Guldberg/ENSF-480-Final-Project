package controller;

import role.*;

import java.util.ArrayList;

import database.*;
import util.*;

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

    public boolean registerNewUser(String firstName, String lastName, String email, String password, 
                               int houseNum, String streetName, String city, 
                               String country, String postalCode) {
        // Creating Address object
        Address address = new Address(houseNum, streetName, city, country, postalCode);

        // Creating RegisteredCustomer object
        Name customerName = new Name(firstName, lastName);
        RegisteredCustomer newCustomer = new RegisteredCustomer(customerName, email, password, address, null); // Assuming null for Payment

        // Attempt to register the user in the database
        return registerUser(newCustomer);
    }

    public boolean registerUser(RegisteredCustomer user) {
        ArrayList<RegisteredCustomer> passengerData = db.getRegisteredCustomerData();
        for(RegisteredCustomer passenger : passengerData){
            if(user == passenger){
                return false;
            }
        }
        // user doesnt exist in RegisteredCustomer
        db.saveUser(user);
        return true;
    }
}
