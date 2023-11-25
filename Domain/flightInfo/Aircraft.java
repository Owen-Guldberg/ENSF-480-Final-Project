package flightInfo;
import java.util.ArrayList;



public class Aircraft{

    // HOW DO WE KNOW PRICE and implement seat class?????
    
    private int id;
    private String name;
    Flight assignedFlight;
    private int capacity;
    ArrayList<Seat> seats;
    
    // Constructor
    public Aircraft(int id, String name, Flight assignedFlight, int capacity) {
        this.id = id;
        this.name = name;
        this.assignedFlight = assignedFlight;
        this.capacity = capacity;
        this.seats = new ArrayList<>();

        // Initialize seats one by one with a max of 120, price default 100?
        for (int seatNum = 1; seatNum <= capacity; seatNum++) {
            Seat seat = new Seat(100, seatNum);
            this.seats.add(seat);
        }
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Flight getAssignedFlight() {
        return assignedFlight;
    }

    public int getCapacity() {
        return capacity;
    }

    public ArrayList<Seat> getSeats() {
        return seats;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    // methods
    public void assignFlight(Flight flight){
        assignedFlight = flight;
    }
    // used for when customer books
    public void changeSeatStatus(boolean status, int seatNum){
        // Check if seatNum is valid
        if (seatNum < 1 || seatNum > capacity) {
            System.out.println("Invalid seat number");
            return;
        }

        // Find the seat with the given seatNum
        for (Seat seat : seats) {
            if (seat.getSeatNum() == seatNum) {
                // Change the seat status
                seat.setAvailable(status);
                return;
            }
        }
    }
    public ArrayList<Seat> findAvailableSeats(){
        ArrayList<Seat> availableSeats = new ArrayList<>();
        for(Seat seat : seats){
            if(seat.getAvailability() == true){
                availableSeats.add(seat);
            }
        }
        return availableSeats;
    }
}
