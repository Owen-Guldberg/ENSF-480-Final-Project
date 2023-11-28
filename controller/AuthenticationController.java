package controller;

import role.*;

import java.util.ArrayList;

import com.owen_guldberg.gmailsender.GMailer;

import database.*;
import util.*;

public class AuthenticationController {

    public AuthenticationController() { }

    public boolean loginUser(String email, String password) {

        ArrayList<RegisteredCustomer> registeredCustomers = Database.getOnlyInstance().getRegisteredCustomerData();

        for (RegisteredCustomer customer : registeredCustomers) {
            if (customer.getEmail().equals(email) && customer.getPassword().equals(password)) {
                // Login successful
                return true;
            }
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
        

        // Send user a promotion code via gmail for signing up
        // First time running app will be prompted to login to the gmail acc in browser
        // user: skywardboundpromotions@gmail.com
        // pass: skyward_bound1
        try {
        GMailer gMailer = new GMailer();
        gMailer.sendMail(email,"Thank you for registering with Skyward Bound!",
                "As a token of our appreciation, we're delighted to offer you a special promotion. Use the promo code below for 10% of your first flight!\r\n" +
                "\r\n" +
                "Promo Code: FIRSTFLIGHT\r\n" +
                "\r\n" +
                "We look forward to serving you on board and providing you with a great travel experience.\r\n" +
                "Best regards, \r\n" +
                "The Skyward Bound Team");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Attempt to register the user in the database
        return registerUser(newCustomer);
    }

    public boolean registerUser(RegisteredCustomer user) {
        ArrayList<RegisteredCustomer> passengerData = Database.getOnlyInstance().getRegisteredCustomerData();
        for(RegisteredCustomer passenger : passengerData){
            if (user == passenger) {
                return false;
            }
        }
        // user doesnt exist in RegisteredCustomer
        Database.getOnlyInstance().saveUser(user);
        return true;
    }
}
