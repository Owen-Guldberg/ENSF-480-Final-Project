package controller;
import util.*;

import java.util.ArrayList;
import java.util.HashMap;

import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;

import database.*;
import flightInfo.*;
import role.*;

public class SystemController {
	private ArrayList<Flight> flights = Database.getOnlyInstance().getFlightData();
	private ArrayList<Aircraft> aircrafts;
	private ArrayList<Location> locations = Database.getOnlyInstance().getLocationData();
	private ArrayList<CrewMember> crewMembers;
	private ArrayList<RegisteredCustomer> registeredCustomers;
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

	public void start() {
		
	}
	public void save(){
		// write to db
	}

	public Location getLocationByName(String name) {
        return locationMap.get(name);
    }

	public ArrayList<Location> getLocations(){
		return locations;
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
	public ArrayList<Ticket> getTickets(){
		tickets.clear();
		tickets = Database.getOnlyInstance().getTicketData();
		return tickets;
	}

		public static void main(String[] args) {
		SystemController system = new SystemController();
	}
	
}
