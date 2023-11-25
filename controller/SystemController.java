package controller;
import util.*;

import java.util.ArrayList;

import flightInfo.*;
import role.*;

public class SystemController {
	private ArrayList<Flight> flights;
	private ArrayList<Aircraft> aircrafts;
	private ArrayList<Location> locations;
	private ArrayList<CrewMember> crewMembers;
	private ArrayList<RegisteredCustomer> registeredCustomers;
	private ArrayList<Ticket> tickets;
	//private ArrayList<Payment> payment;


    public SystemController(){
		start();
    }

	public void start() {
		
	}

    public static void main(String[] args) {
		SystemController system = new SystemController();
	}
	
}
