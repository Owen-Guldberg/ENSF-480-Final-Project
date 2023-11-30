package boundary;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class UserPagePanel extends JPanel {
    private JButton viewFlightsButton;
    private JButton logoutButton;
    private JScrollPane originScrollPane;
    private JScrollPane destinationScrollPane;
    private JList<String> originList;
    private JList<String> destinationList;
    private String user;

    public UserPagePanel(String name, ActionListener viewMyFlightsListener, ActionListener viewFlightsListener, ActionListener logoutListener, ArrayList<String> locationStrings) {
        this.user = name;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initializeComponents(viewMyFlightsListener, viewFlightsListener, logoutListener, locationStrings);
    }

    private void initializeComponents(ActionListener viewMyFlightsListener, ActionListener viewFlightsListener, ActionListener logoutListener, ArrayList<String> locationStrings) {
        // Welcome Label
        JLabel welcomeLabel = new JLabel("Welcome, " + (user.isEmpty() ? "Guest" : user) + "!");
        welcomeLabel.setFont(new Font(welcomeLabel.getFont().getName(), Font.PLAIN, 20));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createVerticalStrut(20));
        add(welcomeLabel);
        add(Box.createVerticalStrut(20));

        // View Flights Button
        if (!user.isEmpty()) {
            JButton myFlightsButton = new JButton("View My Flights");
            myFlightsButton.addActionListener(viewMyFlightsListener);
            myFlightsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(myFlightsButton);
            add(Box.createVerticalStrut(10));
        }

        JLabel enterLabel = new JLabel("Please select an origin and a destination below.");
        enterLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(enterLabel);
        add(Box.createVerticalStrut(10));

        // Location selection
        originList = new JList<>(locationStrings.toArray(new String[0]));
        originList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        originScrollPane = new JScrollPane(originList);
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Origin Locations");
        originScrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10), titledBorder));

        destinationList = new JList<>(locationStrings.toArray(new String[0]));
        destinationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        destinationScrollPane = new JScrollPane(destinationList);
        TitledBorder titledBorder2 = BorderFactory.createTitledBorder("Destination Locations");
        destinationScrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10), titledBorder2));

        JPanel locationSelectionPanel = new JPanel();
        locationSelectionPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); 
        locationSelectionPanel.add(originScrollPane);
        locationSelectionPanel.add(destinationScrollPane);
        add(locationSelectionPanel);
        add(Box.createVerticalStrut(10));

        viewFlightsButton = new JButton("View Available Flights");
        viewFlightsButton.addActionListener(viewFlightsListener);
        viewFlightsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(viewFlightsButton);
        add(Box.createVerticalStrut(10));

        // Logout Button
        logoutButton = new JButton(user.isEmpty() ? "Return to Home Page" : "Logout");
        logoutButton.addActionListener(logoutListener);
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(logoutButton);
        add(Box.createVerticalStrut(20));
    }

    public String getSelectedOrigin() {
        return originList.getSelectedValue();
    }

    public String getSelectedDestination() {
        return destinationList.getSelectedValue();
    }
}
