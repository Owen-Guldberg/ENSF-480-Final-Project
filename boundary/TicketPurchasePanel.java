package boundary;

import controller.AircraftController;
import flightInfo.*;

import javax.swing.*;
import java.awt.*;

public class TicketPurchasePanel extends JPanel {
    private Seat selectedSeat;
    private Flight flight;
    private AircraftController aircraftController;

    public TicketPurchasePanel(Seat seat, AircraftController aircraftController, Flight flight) {
        this.selectedSeat = seat;
        this.aircraftController = aircraftController;
        this.flight = flight;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel("Ticket Purchase"));
        add(new JLabel("Flight: " + flight.toString()));
        add(new JLabel("Selected Seat: " + seat.getSeatNum()));
        add(new JLabel("Price: $" + seat.getPrice()));

        // Add options for ticket cancellation insurance
        JCheckBox insuranceCheckBox = new JCheckBox("Add Ticket Cancellation Insurance ($20)");
        add(insuranceCheckBox);

        // Add payment information fields
        JTextField cardNumberField = new JTextField(16);
        add(new JLabel("Card Number:"));
        add(cardNumberField);

        JButton purchaseButton = new JButton("Purchase Ticket");
        purchaseButton.addActionListener(e -> handlePurchase(insuranceCheckBox.isSelected(), cardNumberField.getText()));
        add(purchaseButton);
    }

    private void handlePurchase(boolean insuranceSelected, String cardNumber) {
        // Implement ticket purchase logic
        // Update seat availability

        //aircraftController.updateSeatAvailability(selectedSeat, false);
        
        // Process payment and booking
        // Show confirmation message
        JOptionPane.showMessageDialog(this, "Ticket purchased successfully!");
    }
}
