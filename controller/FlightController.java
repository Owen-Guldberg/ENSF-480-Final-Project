package controller;

import java.util.ArrayList;
import flightInfo.*;
import role.RegisteredCustomer;
import database.*;
public class FlightController{
    Database db;
    FlightController(Database db){
        this.db = db;
    }
    public ArrayList<Flight> flightsByLocation(Location location){
        ArrayList<Flight> allFlights = db.getFlightData();
        ArrayList<Flight> locationFlights = new ArrayList<>();
        for(Flight f : allFlights){
            if(f.getDestination() == location){
                locationFlights.add(f);
            }
        }
        return locationFlights;
    }
    public ArrayList<Flight> allFlights(){
        ArrayList<Flight> allFlights = db.getFlightData();
        return allFlights;
    }
    public Flight flightByPassenger(RegisteredCustomer passenger){
        ArrayList<Flight> allFlights = db.getFlightData();

        for(Flight f: allFlights){
            for(int i = 0; i < f.getPassengers().size(); i++){
                if(f.getPassengers().get(i) == passenger){
                    return f;
                }

            }
        }
        // no flights booked
        return null;
    }
}
