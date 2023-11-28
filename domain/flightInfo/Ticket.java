package flightInfo;
import util.*;
public class Ticket {
    private int seatNum;
    private double price;
    private String departureTime;
    private boolean hasCancellationInsurance = false;
    private String classSeat;

    // Constructor ONLY CALLED BY CONTROLLER
    public Ticket(int seatNum, double price, boolean hasCancellationInsurance, Name name,String departureTime, String classSeat) {
        this.seatNum = seatNum;
        this.price = price;
        this.hasCancellationInsurance = hasCancellationInsurance;
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

    // Setter for price
    public void setPrice(double price) {
        this.price = price;
    }

    // Getter for hasCancellationInsurance
    public boolean hasCancellationInsurance() {
        return hasCancellationInsurance;
    }

    public void buyCancellationInsureance() {
        hasCancellationInsurance = true;
    }
}
