package flightInfo;
import role.*;
import java.util.ArrayList;

public class Flight {
    private Location destination;
    private String flightNum;
    private Location origin;
    private Aircraft aircraft;
    private String departureTime;
    private String arrivalTime;
    private String flightTime;
    private ArrayList<CrewMember> crewMembers;
    private ArrayList<RegisteredCustomer> passengers;

    // Getters
    public Location getDestination() {
        return destination;
    }

    public String getFlightNum() {
        return flightNum;
    }

    public Location getOrigin() {
        return origin;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getFlightTime() {
        return flightTime;
    }

    public ArrayList<CrewMember> getCrewMembers() {
        return crewMembers;
    }

    public ArrayList<RegisteredCustomer> getPassengers() {
        return passengers;
    }

    // Setters
    public void setDestination(Location destination) {
        this.destination = destination;
    }

    public void setFlightNum(String flightNum) {
        this.flightNum = flightNum;
    }

    public void setOrigin(Location origin) {
        this.origin = origin;
    }

    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setFlightTime(String flightTime) {
        this.flightTime = flightTime;
    }

    public void setCrewMembers(ArrayList<CrewMember> crewMembers) {
        this.crewMembers = crewMembers;
    }

    public void setPassengers(ArrayList<RegisteredCustomer> passengers) {
        this.passengers = passengers;
    }

    public void addPassenger(RegisteredCustomer passenger){
        this.passengers.add(passenger);
    }
}
