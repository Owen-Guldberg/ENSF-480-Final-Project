package boundary;

import controller.AircraftController;
import flightInfo.Seat;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SeatChart extends JPanel {

    private List<JButton> seatButtons = new ArrayList<>();
    private ActionListener seatSelectionListener;

    public SeatChart(List<Seat> seats, ActionListener seatSelectionListener) {
        setLayout(new GridLayout(0, 4)); // Assuming a 4-seat per row configuration
        this.seatSelectionListener = seatSelectionListener;

        for (Seat seat : seats) {
            JButton seatButton = new JButton();
            seatButton.setEnabled(seat.getAvailability()); // Enable only if the seat is available
            seatButton.setBackground(seat.getAvailability() ? Color.GREEN : Color.RED);
            seatButton.setText(seat.getSeatNum());
            seatButton.addActionListener(seatSelectionListener);
            seatButton.setActionCommand(seat.getSeatNum()); // Set action command to seat label or ID
            add(seatButton);
            seatButtons.add(seatButton);
        }
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
