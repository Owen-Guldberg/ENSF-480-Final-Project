package controller;
import util.*;

import java.util.ArrayList;

import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;

import database.*;
import flightInfo.*;
import role.*;

public class SystemController {
	private ArrayList<Flight> flights;
	private ArrayList<Aircraft> aircrafts;
	private ArrayList<Location> locations = Database.getOnlyInstance().getLocationData();
	private ArrayList<CrewMember> crewMembers;
	private ArrayList<RegisteredCustomer> registeredCustomers;
	private ArrayList<Ticket> tickets;
	//private ArrayList<Payment> payment;


    public SystemController(){
		start();
    }

	public void start() {
		
	}
	public void save(){
		// write to db
	}

	public ArrayList<Location> getLocations(){
		return locations;
	}

	public ArrayList<String> getLocationStrings(){
		ArrayList<String> locationStrings = new ArrayList<>();
		for (Location location : locations) {
			locationStrings.add(location.getString());
		}
		return locationStrings;
	}

	public void returnFlights(){
		
	}
    public static void main(String[] args) {
		SystemController system = new SystemController();
	}
	
}
