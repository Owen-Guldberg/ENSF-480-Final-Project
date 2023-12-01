package controller;
import util.*;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.internal.bind.DateTypeAdapter;
import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;

import database.*;
import flightInfo.*;
import role.*;

public class SystemController {
	private ArrayList<Flight> flights = Database.getOnlyInstance().getFlightData();
	private ArrayList<Aircraft> aircrafts;
	private ArrayList<Location> locations = Database.getOnlyInstance().getLocationData();
	private ArrayList<CrewMember> crewMembers = Database.getOnlyInstance().getCrewMemberData();
	private ArrayList<RegisteredCustomer> registeredCustomers = Database.getOnlyInstance().getRegisteredCustomerData();
	private ArrayList<Ticket> tickets = Database.getOnlyInstance().getTicketData();
	//private ArrayList<Payment> payment;
	private HashMap<String, Location> locationMap = new HashMap<>();
	private HashMap<String, Flight> flightMap = new HashMap<>();


    public SystemController(){
		start();
		for (Location loc : locations) {
            locationMap.put(loc.toString(), loc);
        }
		for (Flight flight : flights) {
            flightMap.put(flight.getFlightNum(), flight);
        }
    }
	public ArrayList<RegisteredCustomer> getRegisteredCustomers() {
		return registeredCustomers;
	}
	public void start() {
		
	}
	public void save(){
		// write to db
	}

	public Location getLocationByName(String name) {
        return locationMap.get(name);
    }
	public ArrayList<Flight> getFlightsCrewmembers(String crewmemberEmail){
		CrewMember crewmember = null;
		for(CrewMember c : crewMembers){
			if(c.getEmail().equals(crewmemberEmail)){
				crewmember = c;
			}
		}
		ArrayList<Flight> f = new ArrayList<>();
		for(int i = 0 ; i < flights.size(); i++){
			if(flights.get(i).getCrewMembers() == null){
				System.out.println(flights.get(i).getCrewMembers().get(0));
			}
			else if(flights.get(i).getCrewMembers().contains(crewmember)){
				f.add(flights.get(i));
			}
			
		}
		return f;
	}
	public ArrayList<RegisteredCustomer> getFlightsPassengers(String flightnumer){

		for(Flight f : flights){
			if(f.getFlightNum().equals(flightnumer)){
				return f.getPassengers();
			}
		}
		return null;
	}
	public ArrayList<String> getPassengerStrings(ArrayList<RegisteredCustomer> customers){
		ArrayList<String> customerStrings = new ArrayList<>();
		for(RegisteredCustomer customer: customers){
			customerStrings.add(customer.toString());
		}
		return customerStrings;
	}
	public ArrayList<Location> getLocations(){
		return locations;
	}

	public ArrayList<String> getFlightStrings(ArrayList<Flight> flights){
		ArrayList<String> flightStrings = new ArrayList<>();
		for (Flight flight : flights) {
			flightStrings.add(flight.toString());
		}
		return flightStrings;
	}
	public ArrayList<String> getLocationStrings(ArrayList<Location> locations){
		ArrayList<String> locationStrings = new ArrayList<>();
		for (Location location : locations) {
			locationStrings.add(location.toString());
		}
		return locationStrings;
	}

	public ArrayList<String> getLocationStrings(){
		ArrayList<Location> locations = getLocations();
		ArrayList<String> locationStrings = new ArrayList<>();
		for (Location location : locations) {
			locationStrings.add(location.toString());
		}
		return locationStrings;
	}

	public Flight getFlightByNum(String num) {
        return flightMap.get(num);
    }

	public ArrayList<Flight> getFlights(){
		return flights;
	}

	//get user by email
	public RegisteredCustomer getUserByEmail(String email){
		registeredCustomers = Database.getOnlyInstance().getRegisteredCustomerData();
		for(RegisteredCustomer customer : registeredCustomers){
			if(customer.getEmail().equals(email)){
				return customer;
			}
		}
		return null;
	}

	//get name from email
	public String getNameByEmail(String email){
		registeredCustomers = Database.getOnlyInstance().getRegisteredCustomerData();
		for(RegisteredCustomer customer : registeredCustomers){
			if(customer.getEmail().equals(email)){
				return customer.getName().toString();
			}
		}
		return null;
	}

	// get tickets
    public ArrayList<Ticket> getTickets() {

        return tickets;
    }

    private boolean containsTicket(Ticket newTicket) {
        for (Ticket existingTicket : tickets) {
            if (existingTicket.getSeatNum() == newTicket.getSeatNum() && existingTicket.getFlightNumber().equals(newTicket.getFlightNumber())) {
                return true; // Ticket with the same seat number and flight number already exists
            }
        }
        return false; // Ticket is unique based on seat number and flight number
    }


		public static void main(String[] args) {
		SystemController system = new SystemController();
	}

	// Cancel flight via controller accessing database instance
	public boolean cancelFlight(Flight f) {
		return Database.getOnlyInstance().cancelFlight(f);
	}

	public boolean addFlight(Flight f) {
		return Database.getOnlyInstance().addFlightToDB(f);
	}

	public ArrayList<Aircraft> getAircrafts() {
		return Database.getOnlyInstance().getAircraftData();
	}

	public boolean addAircraft(Aircraft a) {
		return Database.getOnlyInstance().addAircraftToDB(a);
	}

	public boolean deleteAircraft(Aircraft a) {
		return Database.getOnlyInstance().deleteAircraft(a);
	}
	
}






