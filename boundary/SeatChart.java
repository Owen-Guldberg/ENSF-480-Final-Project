package boundary;

import controller.AircraftController;
import flightInfo.Seat;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SeatChart extends JPanel {

    private AircraftController aircraftController;

    public SeatChart(ArrayList<Seat> seats, AircraftController aircraftController) {
        this.aircraftController = aircraftController;
        setLayout(new GridLayout(0, 4)); // Assuming a 4-seat per row configuration

        for (Seat seat : seats) {
            JButton seatButton = new JButton();
            seatButton.setEnabled(seat.getAvailability()); // Enable only if the seat is available
            seatButton.setBackground(seat.getAvailability() ? Color.GREEN : Color.RED);
            seatButton.setText(String.valueOf(seat.getSeatNum()));
            seatButton.addActionListener(e -> showSeatDetails(seat));
            add(seatButton);
        }
    }

    private void showSeatDetails(Seat seat) {
        JOptionPane.showMessageDialog(this, aircraftController.getSeatDetails(seat));
    }

    // Method to add a legend for seat colors
    public void addLegend() {
        JLabel availableLabel = new JLabel("Available");
        availableLabel.setForeground(Color.GREEN);
        add(availableLabel);

        JLabel takenLabel = new JLabel("Taken");
        takenLabel.setForeground(Color.RED);
        add(takenLabel);
    }
}
