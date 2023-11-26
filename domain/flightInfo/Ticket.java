package flightInfo;
import util.*;
public class Ticket {
    private int seatNum;
    private int price;
    private String departureTime;
    private boolean hasCancellationInsurance = false;
    private String classSeat;

    // Constructor ONLY CALLED BY CONTROLLER
    public Ticket(int seatNum, int price, boolean hasCancellationInsurance, Name name,String departureTime, String classSeat) {
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
    public int getPrice() {
        return price;
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
}
