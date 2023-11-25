package flightInfo;

public class Ticket {
    private Seat seat;
    private int price;
    private boolean hasCancellationInsurance = false;

    // Constructor
    public Ticket(Seat seat, int price, boolean hasCancellationInsurance) {
        this.seat = seat;
        this.price = price;
        this.hasCancellationInsurance = hasCancellationInsurance;
    }

    // Getter for seat
    public Seat getSeat() {
        return seat;
    }

    // Setter for seat
    public void setSeat(Seat seat) {
        this.seat = seat;
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
        this.hasCancellationInsurance = true;
    }
    // todo email and display? 
}
