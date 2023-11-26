package controller;

import java.util.ArrayList;
import flightInfo.*;
import role.RegisteredCustomer;
import database.*;

public class FlightController{

    public FlightController(){ }

    public ArrayList<Flight> flightsByLocation(Location orgin,Location location){
        ArrayList<Flight> allFlights = Database.getOnlyInstance().getFlightData();
        ArrayList<Flight> locationFlights = new ArrayList<>();
        for(Flight f : allFlights){
            if(f.getOrigin() == orgin &&f.getDestination() == location){
                locationFlights.add(f);
            }
        }
        return locationFlights;
    }
    public ArrayList<Flight> allFlights(){
        ArrayList<Flight> allFlights = Database.getOnlyInstance().getFlightData();
        return allFlights;
    }
    public Flight flightByPassenger(RegisteredCustomer passenger){
        ArrayList<Flight> allFlights = Database.getOnlyInstance().getFlightData();

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
    public void bookFlightByCustomer(RegisteredCustomer customer, Flight flight, int seatNum){
        flight.addPassenger(customer);
        flight.getAircraft().getSeats().get(seatNum).reserveSeat();
    }
}
