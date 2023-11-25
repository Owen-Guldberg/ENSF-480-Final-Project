package flightInfo;
public class Seat {

    private int price;           // Price of the seat
    private boolean isAvailable; // Availability status of the seat
    private int seatNum;         // Seat number

    // Default constructor
    public Seat() {
        price = 0;
        isAvailable = true;
        seatNum = 1;
    }

    // Parameterized constructor
    public Seat(int price, int seatNum) {
        this.price = price;
        isAvailable = true;
        this.seatNum = seatNum;
    }

    // Reserves the seat, marking it as unavailable
    public void reserveSeat() {
        isAvailable = false;
    }
    public void setAvailable(boolean availability){
        isAvailable = availability;
    }
    // Sets the price of the seat
    public void setPrice(int price) {
        this.price = price;
    }

    // Gets the price of the seat
    public int getPrice() {
        return price;
    }

    // Checks the availability status of the seat
    public boolean getAvailability() {
        return isAvailable;
    }

    // Gets the seat number
    public int getSeatNum() {
        return seatNum;
    }
}