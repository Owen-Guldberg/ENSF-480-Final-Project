package database;

import util.*;
import role.*;
import flightInfo.*;
import java.sql.*;
import java.util.ArrayList;

public class Database {
     private Connection dbConnection;
    public static Database onlyInstance;
    private ResultSet results;
    private final String URL = "jdbc:mysql://localhost:3306/skyward_bound"; 
    private final String USERNAME = "root";
    private final String PASSWORD = "";
    public ArrayList<Location> locations = new ArrayList<Location>();
    public ArrayList<RegisteredCustomer> registeredUsers = new ArrayList<RegisteredCustomer>();
    public ArrayList<Aircraft> aircrafts = new ArrayList<Aircraft>(); 
    public ArrayList<Flight> flights = new ArrayList<Flight>(); 
    public ArrayList<AirlineAgent> crew = new ArrayList<AirlineAgent>(); 
    public ArrayList<Ticket> tickets = new ArrayList<Ticket>();
    

/**
 * Constructs a new Database object and connects to the database specified
 * by the URL, username, and password instance variables.
 * @throws SQLException if there is an error connecting to the database
 */
    private Database() throws SQLException {
        this.dbConnection = connectToDatabase();
        readLocationData();
        readRegisteredUsers();
        readAircraftData();
        readFlightData();
        readCrewMemberData(); 
    }

    public static Database getOnlyInstance(){
        if(onlyInstance == null){
            try{
                onlyInstance = new Database();
            }
            catch(Exception e){
                e.printStackTrace();
            }
            
        }
        return onlyInstance; 
    }

/**
 * Establishes a connection to the database using the URL, username, and password instance variables.
 * @return a Connection object representing the connection to the database
 * @throws SQLException if there is an error connecting to the database
 */
private Connection connectToDatabase() throws SQLException {
        try{
            dbConnection = DriverManager.getConnection(getURL(), getUsername(), getPassword());
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return dbConnection;
    }


// /** WORKS
//  * Reads Registered User data from the database and populates the registeredUsers ArrayList.
//  * @throws SQLException if there is an error reading data from the database
//  */
private void readRegisteredUsers() throws SQLException{
        try {
            Statement stmt = dbConnection.createStatement();
            String query = "SELECT * FROM REGISTEREDUSERS";
            ResultSet results = stmt.executeQuery(query);
            
            while (results.next()) {
                String first = results.getString("FName");
                String last = results.getString("LName");
                String email = results.getString("Email");
                String password = results.getString("Password");

                int house = results.getInt("HouseNum");
                String street= results.getString("Street");
                String city = results.getString("City");
                String country = results.getString("Country");
                String postal= results.getString("PostalCode");
                String cc= results.getString("CreditCardNumber");
                int cvv = results.getInt("CVV"); 

                Name name = new Name(first, last);
                Address a = new Address(house, street, city, country, postal); 
                Payment p = new Payment(cc, cvv);

                RegisteredCustomer user = new RegisteredCustomer(name, email, password, a, p);
                registeredUsers.add(user);
            }
            stmt.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    ///WORKS
    private void readLocationData(){
        try{
            Statement myStmt = this.dbConnection.createStatement();
            String query = "SELECT * FROM LOCATIONS";
            results = myStmt.executeQuery(query);
            while(results.next()){
              int locID = results.getInt("LocationID");
              String airportName= results.getString("AirportName");
              String city = results.getString("City");
              String country = results.getString("Country");
    
              Location tmp = new Location(airportName, city, country); //creates food item with each item in the database
              this.locations.add(tmp); //stores food item in linkedlist of food objects
              }
              myStmt.close(); 
            }catch (SQLException ex) {
                //ex.printStackTrace();
             }
    }

    

    ///WORKS
    //** WORKS
//  * Reads Aircraft data from the database and populates the registeredUsers ArrayList.
//  * @throws SQLException if there is an error reading data from the database
//  */
    private void readAircraftData(){
        try{
            Statement myStmt = this.dbConnection.createStatement();
            String query = "SELECT * FROM AIRCRAFT";
            results = myStmt.executeQuery(query);
            while(results.next()){
              int airID = results.getInt("AircraftID");
              String airName= results.getString("Name");
              int capacity = results.getInt("Capacity");

              Aircraft tmp = new Aircraft(airID, airName, null,capacity); //creates 
              this.aircrafts.add(tmp); //stores
              }
              myStmt.close(); 
            }catch (SQLException ex) {
                //ex.printStackTrace();
             }

    }

// /** WORKS
//  * Reads FLight data from the database and populates the registeredUsers ArrayList.
//  * @throws SQLException if there is an error reading data from the database
//  */
    private void readFlightData(){
        try{
            Statement myStmt = this.dbConnection.createStatement();
            String query = "SELECT * FROM FLIGHTS";
            ResultSet results = myStmt.executeQuery(query);
            while(results.next()){
              int id = results.getInt("FlightID");
              String flightNum = results.getString("FlightNum");
              String flightDate = results.getString("FlightDate");
              String originName = results.getString("Origin");
              String destinationName = results.getString("Destination");
              String aircraft= results.getString("Aircraft");
              String departTime = results.getString("DepartureTime");
              String arriveTime = results.getString("ArrivalTime");
              String flightTime = results.getString("FlightTime");

              Location origin = new Location();
              Location destination = new Location(); 
              for (int i = 0; i < locations.size(); i++){
                if(locations.get(i).getAirportName().equals(originName) == true){
                  origin = locations.get(i); 
                }
                else if(locations.get(i).getAirportName().equals(destinationName) == true){
                  destination = locations.get(i); 
                }
              }

              Aircraft a = new Aircraft();
              for (int i = 0; i < aircrafts.size(); i++){
                if(aircrafts.get(i).getName().equals(aircraft) == true){
                    a = aircrafts.get(i); 
                }
              }

               

              Flight tmp= new Flight(origin,destination, flightNum, flightDate, departTime, arriveTime, flightTime, a); 
              this.flights.add(tmp);
              
            }
            myStmt.close(); 
            }catch (SQLException ex) {
                //ex.printStackTrace();
             }
             
    }

// /** WORKS
//  * Reads crew member data from the database and populates the registeredUsers ArrayList.
//  * @throws SQLException if there is an error reading data from the database
//  */
    private void readCrewMemberData(){

        try{
            Statement myStmt = this.dbConnection.createStatement();
            String query = "SELECT * FROM CREWMEMBER";
            results = myStmt.executeQuery(query);
            while(results.next()){
              int crewID = results.getInt("CrewID");
              String FName= results.getString("FName");
              String LName= results.getString("LName");
              String email= results.getString("Email");
              String job= results.getString("Job");
              int house = results.getInt("HouseNum");
              String street= results.getString("Street");
              String city = results.getString("City");
              String country = results.getString("Country");
              String postal= results.getString("PostalCode");
    
              Name n = new Name(FName, LName);//creates food item with each item in the database
              Address ad = new Address(house, street, city, country, postal); //dont need but maybe could add to airline agent if needed


                AirlineAgent tmp = new AirlineAgent(n, email); 
                crew.add(tmp); 
              }
              myStmt.close(); 
            }catch (SQLException ex) {
                //ex.printStackTrace();
             }

    }

    public void readTicketData(){
        //need to clarify logic for tickets in db
    }

    public ArrayList<Location> getLocationData(){
        return this.locations; 
    }

    public ArrayList<RegisteredCustomer> getRegisteredCustomerData(){
        return this.registeredUsers; 
    }

    public ArrayList<Aircraft> getAircraftData(){
        return this.aircrafts; 
    }

    public ArrayList<Flight> getFlightData(){
        return this.flights; 
    }

    public ArrayList<AirlineAgent> getCrewMemberData(){
        return this.crew; 
    }


      public boolean cancelFlight(Flight f){
        boolean success = false; 
        try{
            String query = "DELETE FROM FLIGHTS WHERE FlightNum = ?"; //matches by flight number 
               PreparedStatement myStmt = dbConnection.prepareStatement(query);
   
               myStmt.setString(1,f.getFlightNum());
            
               int rowCount = myStmt.executeUpdate();
               //System.out.println("Rows affected: " + rowCount);
               if(rowCount == 0)
               {
                 return false;
               }
               else{
                success = true;
                }
               myStmt.close();
             }
             catch (SQLException ex) {
               ex.printStackTrace();
               return false;
             }
             
             for (int i = 0; i < flights.size(); i++){
                if (f == flights.get(i)){
                    flights.remove(i);
                }
             }
             return success;
         }

    public void updateDatabase() throws SQLException{
        try{
            locations.clear(); 
            readLocationData();
            registeredUsers.clear();
            readRegisteredUsers();
            aircrafts.clear();
            readAircraftData();
            flights.clear();
            readFlightData();
            crew.clear(); 
            readCrewMemberData(); 
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        
    }

//

    /**
     * Gets the URL of the database.
     * @return the URL of the database
     */
    public String getURL(){
        return this.URL;
    }

    /**
    * Gets the username used for authentication.
    * @return the username used for authentication
    */
    public String getUsername(){
        return this.USERNAME;
    }

    /**
     * Gets the password used for authentication.
     * @return the password used for authentication
     */
    public String getPassword(){
        return this.PASSWORD;
    }

//     /**
//     * Closes the database connection and result set.
//     */
//     public void close() {
//         //close the connection and result set
//         try{
//             dbConnection.close();
//             results.close();
//         }
//         //catch exception if database cannot be accessed or any other error
//         catch(SQLException e){
//             e.printStackTrace();
//         }
//     }
}
