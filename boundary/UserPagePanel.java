package boundary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class UserPagePanel extends JPanel {
    private JButton viewFlightsButton;
    private JButton logoutButton;
    private JComboBox<String> originComboBox;
    private JComboBox<String> destinationComboBox;
    private String user;

    public UserPagePanel(String name, ActionListener viewFlightsListener, ActionListener logoutListener, String[] locations) {
        user = name;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initializeComponents(viewFlightsListener, logoutListener, locations);
    }

    private void initializeComponents(ActionListener viewFlightsListener, ActionListener logoutListener, String[] locations) {
        // Welcome Label
        JLabel welcomeLabel = new JLabel("Welcome, " + (user.isEmpty() ? "Guest" : user) + "!");
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(welcomeLabel);

        // Location selection
        originComboBox = new JComboBox<>(locations);
        destinationComboBox = new JComboBox<>(locations);

        JPanel originPanel = new JPanel();
        originPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        originPanel.add(new JLabel("Origin:"));
        originPanel.add(originComboBox);

        JPanel destinationPanel = new JPanel();
        destinationPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        destinationPanel.add(new JLabel("Destination:"));
        destinationPanel.add(destinationComboBox);

        JPanel locationSelectionPanel = new JPanel();
        locationSelectionPanel.setLayout(new BoxLayout(locationSelectionPanel, BoxLayout.Y_AXIS));
        locationSelectionPanel.add(originPanel);
        locationSelectionPanel.add(destinationPanel);

        add(locationSelectionPanel);

        // View Flights Button
        viewFlightsButton = new JButton("View Available Flights");
        viewFlightsButton.addActionListener(viewFlightsListener);
        viewFlightsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(viewFlightsButton);

        // Logout Button
        logoutButton = new JButton(user.isEmpty() ? "Back" : "Logout");
        logoutButton.addActionListener(logoutListener);
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(logoutButton);
    }

    public String getSelectedOrigin() {
        return (String) originComboBox.getSelectedItem();
    }

    public String getSelectedDestination() {
        return (String) destinationComboBox.getSelectedItem();
    }

}
