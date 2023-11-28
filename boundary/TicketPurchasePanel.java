package boundary;

import controller.AircraftController;
import controller.PaymentController;
import flightInfo.*;

import javax.swing.*;

import com.owen_guldberg.gmailsender.GMailer;

import java.awt.*;

public class TicketPurchasePanel extends JPanel {
    private Seat selectedSeat;
    private Flight flight;
    private AircraftController aircraftController;
    private PaymentController paymentController;
    private String userEmail;
    private double totalPrice;
    private JPanel cardPanel;
    private CardLayout cardLayout;

    public TicketPurchasePanel(String userEmail, Seat seat, AircraftController aircraftController, Flight flight, JPanel cardPanel, CardLayout cardLayout) {
        this.userEmail = userEmail;
        this.selectedSeat = seat;
        this.aircraftController = aircraftController;
        this.flight = flight;
        this.cardPanel = cardPanel;
        this.cardLayout = cardLayout;
        paymentController = new PaymentController(userEmail, seat.getSeatNum());

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JLabel ticketPurchaseLabel = new JLabel("<html>Ticket Purchase<br/></html>");
        ticketPurchaseLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Set alignment
        add(ticketPurchaseLabel);
        add(new JLabel("Flight Information"));
        add(new JLabel(flight.toString()));
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

        try {
            GMailer gMailer = new GMailer();
            gMailer.sendMail(userEmail,"Skyward Bound Ticket Reciept",
            "Flight Information: \n Date: " + flight.getFlightDate() + 
            "\nOrigin: " + flight.getOrigin() +
            "\nDestination: " + flight.getDestination() +
            "\nDeparture Time: " + flight.getDepartureTime() +
            "\nArrival Time " + flight.getArrivalTime() +
            "\nFlight Duration: " + flight.getFlightTime() +
            "\nSeat and Price Information: \n" + 
            "Selected Seat: " + selectedSeat.getSeatNum() +
            "\nPrice: $" + String.format("%.2f", totalPrice));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Process payment and booking
        // Show confirmation message
        //JOptionPane.showMessageDialog(this, "Ticket purchased successfully!");
        TicketConfirmationPanel confirmationPanel = new TicketConfirmationPanel(userEmail, flight.toString(), selectedSeat.getSeatNum(), totalPrice, cardPanel, cardLayout);
        cardPanel.add(confirmationPanel, "ticketConfirmation");
        cardLayout.show(cardPanel, "ticketConfirmation");
    }
}
