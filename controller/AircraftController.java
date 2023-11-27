package controller;
import java.util.ArrayList;

import database.*;
import flightInfo.*;

public class AircraftController {

    public AircraftController(){ }

    public ArrayList<Seat> seatByAircraft(Aircraft aircraft){
        ArrayList<Aircraft> ac = Database.getOnlyInstance().getAircraftData();
        for(Aircraft craft : ac){
            if(craft.getId() == aircraft.getId()){
                return craft.getSeats();
            }
        }
        return new ArrayList<>();
    }
    public String getSeatDetails(Seat seat) {
        // Construct a string with all the details of the seat
        return "Seat Number: " + seat.getSeatNum() + 
               "\nPrice: " + seat.getPrice() + 
               "\nClass: " + seat.getSeatClass() +
               "\nAvailable: " + (seat.getAvailability() ? "Yes" : "No");
    }

    public void updateSeatAvailability(Seat seat, boolean availability) {
        // Update the availability of the seat
        seat.setAvailable(availability);
    }
}
