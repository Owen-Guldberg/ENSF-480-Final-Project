package flightInfo;
import java.util.ArrayList;

import role.*;
import util.*;
import flightInfo.*;
public class tester {
    public static void main(String[] args) {
        Name na = new Name("thomas","mattern");
        RegisteredCustomer customer = new RegisteredCustomer(na, "thmat@gmail.com");

        ArrayList<RegisteredCustomer> na1 = new ArrayList<>();

        ArrayList<CrewMember> cm = new ArrayList<>();
        Location loc1 = new Location(null, null, null);
        Location loc = new Location("yyx", "calgary", "canada");
        Flight fl = new Flight(loc, loc, null, null, null, null, cm, na1);
        fl.addPassenger(customer);
        Aircraft ac = new Aircraft(1, "fbb-3", fl, 120);
        ArrayList<Aircraft> ac1 = new ArrayList<>();
        ac1.add(ac);
        Airline air = new Airline("westjet", ac1, null, cm);
        if(fl.getPassengers().get(0).getName().getFirstName() == "thomas"){
            System.out.println("hey");
        }

    }
}

