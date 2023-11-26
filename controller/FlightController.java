package controller;

import java.util.ArrayList;
import flightInfo.*;
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
    
}
