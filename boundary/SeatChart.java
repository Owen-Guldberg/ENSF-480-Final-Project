package boundary;

import controller.AircraftController;
import controller.FlightController;
import controller.SystemController;
import flightInfo.Flight;
import flightInfo.Seat;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SeatChart extends JPanel {

    private AircraftController aircraftController;
    private String flightNum;
    private Color lightGreen = new Color(152, 251, 152);
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private SystemController system;
    private String userEmail;

    public SeatChart(String userEmail, ArrayList<Seat> seats, AircraftController aircraftController, String flightNum, SystemController system, JPanel cardPanel, CardLayout cardLayout) {
        this.userEmail = userEmail;
        this.aircraftController = aircraftController;
        this.flightNum = flightNum;
        this.cardPanel = cardPanel;
        this.cardLayout = cardLayout;
        this.system = system;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel seatChartLabel = new JLabel("Seat Chart");
        seatChartLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel seatsPanel = new JPanel(new GridLayout(0, 4)); // 4 seats per row
        seatsPanel.setPreferredSize(new Dimension(900, 600));

        for (Seat seat : seats) {
            JButton seatButton = new JButton();
            seatButton.setOpaque(true);
            seatButton.setEnabled(seat.getAvailability()); // Enable only if the seat is available
            seatButton.setBackground(seat.getAvailability() ? lightGreen : Color.RED);
            seatButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            seatButton.setText(String.valueOf(seat.getSeatNum()));
            seatButton.addActionListener(e -> showSeatDetails(seat));
            seatsPanel.add(seatButton);
        }

        // Create a wrapper panel with FlowLayout
        JPanel wrapperPanel = new JPanel();
        wrapperPanel.add(seatsPanel);

        add(seatChartLabel);
        add(wrapperPanel);
        addLegend();
    }

    private void showSeatDetails(Seat seat) {
        Flight flight = system.getFlightByNum(flightNum);
        int response = JOptionPane.showConfirmDialog(this, 
            aircraftController.getSeatDetails(seat) + "\n\nWould you like to select this seat?", 
            "Seat Details", JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION && seat.getAvailability()) {
            if (userEmail == "") {
                cardLayout.show(cardPanel, "login");
            } else {
                TicketPurchasePanel purchasePanel = new TicketPurchasePanel(userEmail, seat, aircraftController, flight, cardPanel, cardLayout, system);
                cardPanel.add(purchasePanel, "purchaseTicket");
                cardLayout.show(cardPanel, "purchaseTicket");
            }
            // TicketPurchasePanel purchasePanel = new TicketPurchasePanel(userEmail, seat, aircraftController, flight, cardPanel, cardLayout);
            // cardPanel.add(purchasePanel, "purchaseTicket");
            // cardLayout.show(cardPanel, "purchaseTicket");
        } else if (response == JOptionPane.YES_OPTION && !seat.getAvailability()) {
            JOptionPane.showMessageDialog(this, "Seat " + seat.getSeatNum() + " is not available!");
        }
    }

    // Method to add a legend for seat colors
    public void addLegend() {
        JPanel legendPanel = new JPanel();
        legendPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JPanel availableBox = createLegendBox("Available", lightGreen);
        JPanel takenBox = createLegendBox("Taken", Color.RED);

        legendPanel.add(availableBox);
        legendPanel.add(takenBox);

        add(legendPanel);
    }

    private JPanel createLegendBox(String labelText, Color backgroundColor) {
        JLabel label = new JLabel(labelText);
        label.setForeground(Color.BLACK); // Set text color

        JPanel box = new JPanel();
        box.setBackground(backgroundColor);
        box.add(label);
        box.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        return box;
    }

}
