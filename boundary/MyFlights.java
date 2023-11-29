package boundary;

import javax.swing.*;

import org.checkerframework.checker.units.qual.s;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import flightInfo.Ticket;
import role.RegisteredCustomer;
import controller.PaymentController;
import controller.SystemController;
import database.Database;

public class MyFlights extends JPanel {
    private PaymentController paymentController;
    private String userEmail;
    private int seatNum;
    private SystemController systemController;
    private JPanel cardPanel;
    private CardLayout cardLayout;

    public MyFlights(PaymentController paymentController, String userEmail, int seatNum, SystemController systemController, JPanel cardPanel, CardLayout cardLayout) {
        this.paymentController = paymentController;
        this.userEmail = userEmail;
        this.seatNum = seatNum;
        this.systemController = systemController;
        this.cardPanel = cardPanel;
        this.cardLayout = cardLayout;
        refreshPanel();
    }

    private void refreshPanel() {
        removeAll(); // Remove all existing components
        initializeComponents(); // Reinitialize components
        revalidate(); // Revalidate the panel layout
        repaint(); // Repaint the panel
    }

    private void initializeComponents() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("My Flights");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titleLabel);

        ArrayList<Ticket> tickets = systemController.getTickets();
        System.out.println(tickets.size());
        if (tickets.isEmpty()) {
            add(new JLabel("No flights booked."));
        } else {
            for (Ticket ticket : tickets) {
                add(createTicketPanel(ticket));
            }
        }

        JButton backButton = new JButton("Browse More Flights");
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "user"));
        add(backButton);
        setVisible(true);
    }

    private JPanel createTicketPanel(Ticket ticket) {
        JPanel ticketPanel = new JPanel();
        ticketPanel.setLayout(new BoxLayout(ticketPanel, BoxLayout.Y_AXIS));
        ticketPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        ticketPanel.setBackground(Color.WHITE);
        ticketPanel.add(new JLabel("Flight Information:"));

        String flightInfo = systemController.getFlightByNum(ticket.getFlightNumber()).toString();
        JLabel flightInfoLabel = new JLabel(flightInfo);
        ticketPanel.add(flightInfoLabel);

        ticketPanel.add(new JLabel("Ticket Information:"));
        JLabel ticketDetailsLabel = new JLabel(ticket.toString());
        ticketPanel.add(ticketDetailsLabel);

        JButton cancelButton = new JButton("Cancel Ticket");
        cancelButton.addActionListener(e -> cancelTicket(ticket));
        ticketPanel.add(cancelButton);

        return ticketPanel;
    }

    private void cancelTicket(Ticket ticket) {
        // Implement cancellation logic
        // Remove ticket from user's list and update in database
        // Show confirmation message
        systemController.getUserByEmail(userEmail).removeTicket(ticket);
        JOptionPane.showMessageDialog(this, "Ticket cancelled successfully.");
        paymentController.deleteTicket(userEmail, ticket.getSeatNum());
        // Refresh the MyFlights panel to update the list of tickets
        refreshPanel();
    }
}
