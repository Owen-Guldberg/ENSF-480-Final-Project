package flightInfo;
import util.*;

public class Ticket {
    private int seatNum;
    private double price;
    private String flightNumber;
    private String departureTime;
    private boolean hasCancellationInsurance = false;
    private String classSeat;

    // Constructor ONLY CALLED BY CONTROLLER
    public Ticket(int seatNum, double price, String flightNum, boolean insurance, String departureTime, String classSeat) {
        this.seatNum = seatNum;
        this.price = price;
        this.flightNumber = flightNum;
        this.hasCancellationInsurance = insurance;
        this.departureTime = departureTime;
        this.classSeat = classSeat;
    }
    
    public Ticket getTicket(){
        return this;
    }

    // Getter for price
    public double getPrice() {
        return price;
    }

    // Getter for seatNum
    public int getSeatNum() {
        return seatNum;
    }

    // Getter for flightNumber
    public String getFlightNumber() {
        return flightNumber;
    }

    // Getter for hasCancellationInsurance
    public boolean getHasCancellationInsurance() {
        return hasCancellationInsurance;
    }

    // Getter for departureTime
    public String getDepartureTime() {
        return departureTime;
    }

    // Getter for classSeat
    public String getClassSeat() {
        return classSeat;
    }

    // Setter for price
    public void setPrice(int price) {
        this.price = price;
    }

    public String toString() {
        return "<html><u>Seat Number:</u> " + seatNum + "<br><u>Price:</u> $" + String.format("%.2f", price) + "<br><u>Departure Time:</u> " + departureTime + "<br><u>Class:</u> " + classSeat + "<br><u>Cancellation Insurance:</u> " + (hasCancellationInsurance ? "Yes" : "No"+ "</html>");
    }
}

