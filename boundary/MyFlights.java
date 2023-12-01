package boundary;

import javax.swing.*;

import org.checkerframework.checker.units.qual.s;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
        setBackground(Color.WHITE);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Box.createVerticalStrut(20));

        JLabel titleLabel = new JLabel("My Flights");
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titleLabel);
        add(Box.createVerticalStrut(20));

        RegisteredCustomer customer = systemController.getUserByEmail(userEmail);
        ArrayList<Ticket> tickets = customer.getTickets();


        // issue is now with cancel
        if (tickets.isEmpty()) {
            add(new JLabel("No flights booked."));
        } else {
            for (Ticket ticket : tickets) {
                add(createTicketPanel(ticket));
                add(Box.createVerticalStrut(5));
            }
        }

        add(Box.createVerticalStrut(40));
        JButton backButton = new JButton("Browse More Flights");
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "user"));
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        styleButton(backButton);
        add(backButton);
        add(Box.createVerticalStrut(80));
        setVisible(true);
    }

    private JPanel createTicketPanel(Ticket ticket) {
        JPanel ticketPanel = new JPanel();
        ticketPanel.setLayout(new BoxLayout(ticketPanel, BoxLayout.Y_AXIS));
        ticketPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        ticketPanel.setBackground(Color.WHITE);
        //ticketPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, ticketPanel.getPreferredSize().height));

        JLabel flightLabel = new JLabel("Flight and Ticket Information");
        flightLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        ticketPanel.add(flightLabel);

        String flightInfo = systemController.getFlightByNum(ticket.getFlightNumber()).toString();
        JLabel flightInfoLabel = new JLabel(flightInfo);
        flightInfoLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        ticketPanel.add(flightInfoLabel);

        //ticketPanel.add(new JLabel("Ticket Information"));
        JLabel ticketDetailsLabel = new JLabel(ticket.toString());
        ticketDetailsLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        ticketPanel.add(ticketDetailsLabel);

        JButton cancelButton = new JButton("Cancel Ticket");
        cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        styleButton(cancelButton);
        cancelButton.addActionListener(e -> cancelTicket(ticket));
        ticketPanel.add(cancelButton);

        return ticketPanel;
    }

    private void cancelTicket(Ticket ticket) {
        // Implement cancellation logic
        // Remove ticket from user's list and update in database
        // Show confirmation message
        for(int i = 0 ; i < systemController.getFlightByNum(ticket.getFlightNumber()).getAircraft().getSeats().size(); i++){
            if(systemController.getFlightByNum(ticket.getFlightNumber()).getAircraft().getSeats().get(i).getSeatNum() == ticket.getSeatNum()){
                systemController.getFlightByNum(ticket.getFlightNumber()).getAircraft().getSeats().get(i).setAvailable(true);
            }

        }
        paymentController.deleteTicket(userEmail, ticket.getSeatNum());
        systemController.getFlightByNum(ticket.getFlightNumber()).getPassengers().remove(systemController.getUserByEmail(userEmail));

        systemController.getUserByEmail(userEmail).removeTicket(ticket);

        JOptionPane.showMessageDialog(this, "Ticket cancelled successfully.");
        // Refresh the MyFlights panel to update the list of tickets
        refreshPanel();
    }

    private void styleButton(JButton button) {
        Color color = new Color(0, 102, 204);
        button.setBackground(color); // Blue background
        button.setForeground(Color.WHITE); // White text
        button.setFocusPainted(false);
        button.setFont(new Font(button.getFont().getName(), Font.BOLD, 16));
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.darker());
            }
    
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });
    }
}


