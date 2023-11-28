package flightInfo;
import util.*;

public class Ticket {
    private int seatNum;
    private int price;
    private String flightName;

    // Constructor ONLY CALLED BY CONTROLLER
    public Ticket(int seatNum, int price, Name name,String flightName) {
        this.seatNum = seatNum;
        this.price = price;
        this.flightName = flightName;
    }
    
    public Ticket getTicket(){
        return this;
    }

    // Getter for price
    public int getPrice() {
        return price;
    }

    // Getter for seatNum
    public int getSeatNum() {
        return seatNum;
    }

    // Setter for price
    public void setPrice(int price) {
        this.price = price;
    }

    // Getter for hasCancellationInsurance
    public boolean hasCancellationInsurance() {
        return hasCancellationInsurance;
    }

    public void buyCancellationInsureance() {
        hasCancellationInsurance = true;
    }

    public String toString() {
        return "<html>Seat Number: " + seatNum + "<br>Price: $" + String.format("%.2f", price) + "<br>Departure Time: " + departureTime + "<br>Class: " + classSeat + "<br>Cancellation Insurance: " + (hasCancellationInsurance ? "Yes" : "No"+ "</html>");
    }
}

