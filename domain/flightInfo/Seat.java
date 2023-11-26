package flightInfo;
public class Seat {

    private int price;           // Price of the seat
    private boolean isAvailable; // Availability status of the seat
    private int seatNum;         // Seat number
    private String seatClass;      // seat class

    // Parameterized constructor
    public Seat(int price, int seatNum, String seatClass) {
        this.price = price;
        isAvailable = true;
        this.seatNum = seatNum;
        this.seatClass = seatClass;
    }
    public Seat getSeat(){
        return this;
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

    // Gets the seat class
    public String getSeatClass() {
        return seatClass;
    }
}