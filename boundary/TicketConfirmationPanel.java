package boundary;

import javax.swing.*;
import java.awt.*;

public class TicketConfirmationPanel extends JPanel {
    private String userEmail;
    private String flightInfo;
    private int seatNum;
    private double price;
    private JPanel cardPanel;
    private CardLayout cardLayout;

    public TicketConfirmationPanel(String userEmail, String flightInfo, int seatNum, double price, JPanel cardPanel, CardLayout cardLayout) {
        this.userEmail = userEmail;
        this.flightInfo = flightInfo;
        this.seatNum = seatNum;
        this.price = price;
        this.cardPanel = cardPanel;
        this.cardLayout = cardLayout;
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        // Ticket Confirmation Title
        JLabel titleLabel = new JLabel("<html>Thank you for using Skyward Bound!</br> Your ticket and receipt have now been emailed to " + userEmail + ".</html>");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titleLabel);
    
        // Flight Information
        JPanel flightInfoPanel = createBorderedPanel("Flight Information", "<html>" + flightInfo + "</html>");
        JPanel seatPricePanel = createBorderedPanel("Seat and Price Information", 
                                                "Selected Seat: " + seatNum + "</br>Price: $" + String.format("%.2f", price));
        // add(new JLabel("Flight Information:"));
        // JTextArea flightInfoArea = new JTextArea(flightInfo);
        // flightInfoArea.setEditable(false);
        // flightInfoArea.setOpaque(false);
        // add(flightInfoArea);
    
        // Seat and Price Information
        // add(new JLabel("Selected Seat: " + seatNum));
        // add(new JLabel("Price: $" + String.format("%.2f", price)));
    
        // Receipt Section
        JPanel receiptPanel = createBorderedPanel("Receipt", "Detailed receipt information...");
        // add(new JLabel("Receipt:"));
        // JTextArea receiptArea = new JTextArea("Detailed receipt information...");
        // receiptArea.setEditable(false);
        // receiptArea.setOpaque(false);
        // add(receiptArea);
    
        // Buttons
        add(createButtonPanel());
    
        // Ensure components are aligned correctly
        alignComponents();
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());
        buttonPanel.add(logoutButton);
    
        JButton viewFlightsButton = new JButton("View My Flights");
        viewFlightsButton.addActionListener(e -> viewFlights());
        buttonPanel.add(viewFlightsButton);
    
        JButton browseFlightsButton = new JButton("Browse More Flights");
        browseFlightsButton.addActionListener(e -> browseFlights());
        buttonPanel.add(browseFlightsButton);
    
        return buttonPanel;
    }
    
    private void alignComponents() {
        Component[] components = getComponents();
        for (Component comp : components) {
            ((JComponent) comp).setAlignmentX(Component.CENTER_ALIGNMENT);
        }
    }

    private JPanel createBorderedPanel(String title, String content) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel contentLabel = new JLabel(content);
        contentLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(contentLabel);

        add(panel);
        return panel;
    }

    private void logout() {
        // Implement logout functionality
        cardLayout.show(cardPanel, "main");
    }

    private void viewFlights() {
        // Implement view flights functionality
        // cardLayout.show(cardPanel, "userFlights");
    }

    private void browseFlights() {
        // Implement browse flights functionality
        // cardLayout.show(cardPanel, "browseFlights");
    }
}
