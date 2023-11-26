package controller;
import java.util.ArrayList;

import database.*;
import flightInfo.*;

public class AircraftController {
    Database db;
    AircraftController(Database db){
        this.db = db;
    }
    public ArrayList<Seat> seatByAircraft(Aircraft aircraft){
        ArrayList<Aircraft> ac = db.getAircraftData();
        for(Aircraft craft : ac){
            if(craft == aircraft){
                return craft.getSeats();
            }
        }
        return null;
    }
}
