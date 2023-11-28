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

    // Setter for price
    public void setPrice(int price) {
        this.price = price;
    }
}

