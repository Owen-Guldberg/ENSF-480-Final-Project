package boundary;

import controller.AircraftController;
import controller.PaymentController;
import flightInfo.*;

import javax.swing.*;
import java.awt.*;

public class TicketPurchasePanel extends JPanel {
    private Seat selectedSeat;
    private Flight flight;
    private AircraftController aircraftController;
    private PaymentController paymentController;
    private String userEmail;
    private double totalPrice;

    public TicketPurchasePanel(String userEmail, Seat seat, AircraftController aircraftController, Flight flight) {
        this.userEmail = userEmail;
        this.selectedSeat = seat;
        this.aircraftController = aircraftController;
        this.flight = flight;
        paymentController = new PaymentController(userEmail);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JLabel ticketPurchaseLabel = new JLabel("<html>Ticket Purchase<br></html>");
        ticketPurchaseLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Set alignment
        add(ticketPurchaseLabel);
        add(new JLabel("Flight Information"));
        add(new JLabel("<html>" + flight.toString() + "</html>"));
        add(new JLabel("Selected Seat: " + seat.getSeatNum()));
        add(new JLabel("Price: $" + seat.getPrice()));

        // Add options for ticket cancellation insurance
        JCheckBox insuranceCheckBox = new JCheckBox("Add Ticket Cancellation Insurance ($20)");
        add(insuranceCheckBox);

        // Add payment information fields
        JTextField cardNumberField = new JTextField(16);
        cardNumberField.setMaximumSize(new Dimension(200, 20));
        add(new JLabel("Card Number:"));
        add(cardNumberField);

        JTextField cvvField = new JTextField(4);
        cvvField.setMaximumSize(new Dimension(60, 20)); // Smaller field for CVV
        add(new JLabel("CVV:"));
        add(cvvField);

        JTextField promoCodeField = new JTextField(10);
        promoCodeField.setMaximumSize(new Dimension(200, 20));
        add(new JLabel("Promotion Code (optional):"));
        add(promoCodeField);

        JButton purchaseButton = new JButton("Purchase Ticket");
        purchaseButton.addActionListener(e -> handlePurchase(insuranceCheckBox.isSelected(), cardNumberField.getText(), cvvField.getText(),
        promoCodeField.getText()));
        add(purchaseButton);

        if (paymentController.paymentAvailable()) {
            cardNumberField.setText(paymentController.getPaymentCardNum());
            cvvField.setText(String.valueOf(paymentController.getPaymentCVV()));
        }
    }

    private void handlePurchase(boolean insuranceSelected, String cardNumber, String cvv, String promoCode) {
        // Implement ticket purchase logic
        // Update seat availability
        paymentController.setPaymentInfo(cardNumber, cvv);
        paymentController.setTicketPrice(selectedSeat.getPrice(), insuranceSelected);
        paymentController.setStrat(promoCode);
        totalPrice = paymentController.getTicketPrice();
        aircraftController.updateSeatAvailability(selectedSeat, false);

        // Process payment and booking
        // Show confirmation message
        JOptionPane.showMessageDialog(this, "Ticket purchased successfully!");
    }
}
