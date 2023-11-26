package boundary;

import controller.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.*;
import java.sql.*;
import java.text.*;
import java.io.*;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;


public class GUI extends JFrame implements ActionListener {

    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel loginPanel;
    private JPanel registerPanel;
    private JPanel userPanel;
    private JPanel flightInfoPanel;  // New panel for flight information
    private JButton backToMainButton;
    private JButton actionButton; // Used for both login and register actions
    private JButton loginBackButton; // Separate button for the back action on the login page
    private JTextField usernameField;
    private JPasswordField passwordField;
    private String currentUsername; // Track username of currently logged in user

    public GUI() {
        setTitle("Skyward Bound Flight Reservation System");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        mainPanel = createMainPanel();
        cardPanel.add(mainPanel, "main");

        loginPanel = createLoginPanel();
        cardPanel.add(loginPanel, "login");

        registerPanel = createRegisterPanel();
        cardPanel.add(registerPanel, "register");

        userPanel = new JPanel();
        cardPanel.add(userPanel, "user");

        add(cardPanel);

        cardLayout.show(cardPanel, "main");

        setVisible(true);
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel welcome = new JLabel("Welcome to Skyward Bound!");
        welcome.setFont(new Font(welcome.getFont().getName(), Font.PLAIN, 16));
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel instructions = new JLabel("Please register or login to continue.");
        instructions.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttonsAndLinkPanel = new JPanel();
        buttonsAndLinkPanel.setLayout(new BoxLayout(buttonsAndLinkPanel, BoxLayout.Y_AXIS));

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());

        // Register Button
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(this);
        buttonsPanel.add(registerButton);
        
        // Login Button
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        buttonsPanel.add(loginButton);
        buttonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Continue as Guest Link
        JButton guestLink = new JButton("Continue as Guest");
        guestLink.setBorderPainted(false);
        guestLink.setContentAreaFilled(false);
        guestLink.setFocusPainted(false);
        guestLink.setOpaque(false);
        guestLink.setForeground(Color.BLUE);
        guestLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        guestLink.setFont(new Font(guestLink.getFont().getName(), Font.PLAIN, guestLink.getFont().getSize()));

        // Action listener
        guestLink.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                continueAsGuest();
            }
        });
        guestLink.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonsAndLinkPanel.add(buttonsPanel);
        buttonsAndLinkPanel.add(guestLink);
        panel.add(welcome);
        panel.add(instructions);
        panel.add(buttonsAndLinkPanel);

        return panel;
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel usernameLabel = new JLabel("Username/Email:");

        JPanel usernamePanel = new JPanel();
        usernamePanel.setLayout(new BoxLayout(usernamePanel, BoxLayout.X_AXIS));

        usernameField = new JTextField(15);
        usernameField.setMaximumSize(new Dimension(200, 25));

        usernamePanel.add(Box.createHorizontalGlue());
        usernamePanel.add(usernameField);
        usernamePanel.add(Box.createHorizontalGlue());

        JLabel passwordLabel = new JLabel("Password:");

        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.X_AXIS));

        passwordField = new JPasswordField(15);
        passwordField.setMaximumSize(new Dimension(200, 25));

        passwordPanel.add(Box.createHorizontalGlue());
        passwordPanel.add(passwordField);
        passwordPanel.add(Box.createHorizontalGlue());

        // Show/Hide password checkbox
        JCheckBox showPasswordCheckBox = new JCheckBox("Show Password");
        showPasswordCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Toggle password visibility
                passwordField.setEchoChar(showPasswordCheckBox.isSelected() ? '\u0000' : '*');
            }
        });

        JPanel showPasswordPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        showPasswordPanel.add(showPasswordCheckBox);

        actionButton = new JButton("Login");
        actionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // Implement login logic here
                String username = usernameField.getText();
                char[] password = passwordField.getPassword();
                currentUsername = username;


                // For now, just print the entered values
                System.out.println("Username/Email: " + username);
                System.out.println("Password: " + new String(password));

                // Assuming login is successful, show the user page
                showUserPage(username);
            }
        });
        actionButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        backToMainButton = new JButton("Back");
        backToMainButton.addActionListener(this);
        backToMainButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginBackButton = new JButton("Back");
        loginBackButton.addActionListener(this);
        loginBackButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(actionButton);
        buttonPanel.add(loginBackButton);
        panel.add(Box.createVerticalStrut(10));

        panel.add(usernameLabel);
        panel.add(usernamePanel);


        panel.add(Box.createVerticalStrut(10));
        panel.add(passwordLabel);
        panel.add(passwordPanel);

        // Add show/hide password checkbox
        panel.add(showPasswordPanel);

        panel.add(Box.createVerticalStrut(10));
        panel.add(buttonPanel);
        panel.add(Box.createVerticalStrut(10));

        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel usernameLabel = new JLabel("Username:");
        JLabel emailLabel = new JLabel("Email:");

        JPanel usernamePanel = new JPanel();
        usernamePanel.setLayout(new BoxLayout(usernamePanel, BoxLayout.X_AXIS));

        JTextField registerUsernameField = new JTextField(15);
        registerUsernameField.setMaximumSize(new Dimension(200, 25));

        usernamePanel.add(Box.createHorizontalGlue());
        usernamePanel.add(registerUsernameField);
        usernamePanel.add(Box.createHorizontalGlue());

        JPanel emailPanel = new JPanel();
        emailPanel.setLayout(new BoxLayout(emailPanel, BoxLayout.X_AXIS));

        JTextField registerEmailField = new JTextField(15);
        registerEmailField.setMaximumSize(new Dimension(200, 25));

        emailPanel.add(Box.createHorizontalGlue());
        emailPanel.add(registerEmailField);
        emailPanel.add(Box.createHorizontalGlue());

        JLabel passwordLabel = new JLabel("Password:");

        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.X_AXIS));

        JPasswordField registerPasswordField = new JPasswordField(15);
        registerPasswordField.setMaximumSize(new Dimension(200, 25));

        passwordPanel.add(Box.createHorizontalGlue());
        passwordPanel.add(registerPasswordField);
        passwordPanel.add(Box.createHorizontalGlue());

        // Show/Hide password checkbox
        JCheckBox showPasswordCheckBox = new JCheckBox("Show Password");
        showPasswordCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Toggle password visibility
                registerPasswordField.setEchoChar(showPasswordCheckBox.isSelected() ? '\u0000' : '*');
            }
        });


        JPanel showPasswordPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        showPasswordPanel.add(showPasswordCheckBox);

        actionButton = new JButton("Register");
        actionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                // Implement registration logic here
                String username = registerUsernameField.getText();
                String email = registerEmailField.getText();
                char[] password = registerPasswordField.getPassword();


                // for now just print the entered values
                System.out.println("New Username: " + username);
                System.out.println("Email: " + email);
                System.out.println("New Password: " + new String(password));

                // Show the home page so the user can log in
                showMainScreen();

                // Clear the fields
                registerUsernameField.setText("");
                registerEmailField.setText("");
                registerPasswordField.setText("");
            }
        });
        actionButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        backToMainButton = new JButton("Back");
        backToMainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show the main screen when the back button is pressed
                showMainScreen();

                // Reset the register fields
                registerUsernameField.setText("");
                registerEmailField.setText("");
                registerPasswordField.setText("");
            }
        });
        backToMainButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(actionButton);
        buttonPanel.add(backToMainButton);
        panel.add(Box.createVerticalStrut(10));

        panel.add(usernameLabel);
        panel.add(usernamePanel);
        panel.add(Box.createVerticalStrut(10));

        panel.add(emailLabel); // Email label
        panel.add(emailPanel); // Email panel
        panel.add(Box.createVerticalStrut(10));

        panel.add(passwordLabel);
        panel.add(passwordPanel);

        // Add show/hide password checkbox
        panel.add(showPasswordPanel);
        panel.add(Box.createVerticalStrut(10));

        panel.add(buttonPanel);
        panel.add(Box.createVerticalStrut(10));

        return panel;
    }

    private JPanel createUserPage(String username) {
        JPanel userPage = new JPanel();
        userPage.setLayout(new BoxLayout(userPage, BoxLayout.Y_AXIS));

        JLabel welcomeLabel = new JLabel("Welcome, " + (username.isEmpty() ? "Guest" : username) + "!");
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        if (username.isEmpty()) {
            // If the user is a guest display "Return to Home Page" button
            actionButton = new JButton("Return to Home Page");
            actionButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showMainScreen();
                }
            });
        } else {
            // If the user is logged in display "Logout" button
            actionButton = new JButton("Logout");
            actionButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showMainScreen();
                }
            });
        }
        actionButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        userPage.add(Box.createVerticalStrut(20));
        userPage.add(welcomeLabel);
        userPage.add(Box.createVerticalStrut(20));

        // Scrollable menu for 'Flights'
        JScrollPane flightsScrollPane = createScrollMenu("Flights");
        userPage.add(flightsScrollPane);
        userPage.add(Box.createVerticalStrut(10));
        userPage.add(actionButton);

        return userPage;
    }

    private JScrollPane createScrollMenu(String menuTitle) {
        String[] flights = {"Flight 1", "Flight 2", "Flight 3", "Flight 4", "Flight 5", "Flight 6"}; // Temporary

        JList<String> list = new JList<>(flights);
        list.setFont(new Font(list.getFont().getName(), Font.PLAIN, 16));


        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(200, 100));


        TitledBorder titledBorder = BorderFactory.createTitledBorder(menuTitle);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10), titledBorder));

        // Add a ListSelectionListener to handle item selection
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    // Get the selected flight
                    String selectedFlight = list.getSelectedValue();
                    handleSelectedFlight(selectedFlight);
                }
            }
        });

        return scrollPane;
    }

    // Method to handle the action when a flight is selected
    private void handleSelectedFlight(String selectedFlight) {

        // Database connectivity here
        System.out.println("Selected Flight: " + selectedFlight);

        // Create and show the flightInfoPanel
        flightInfoPanel = createFlightInfoPanel(selectedFlight);
        cardPanel.add(flightInfoPanel, "flightInfo");
        cardLayout.show(cardPanel, "flightInfo");
    }

    // Method to create the flightInfoPanel
    private JPanel createFlightInfoPanel(String flightInfo) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel infoLabel = new JLabel("Flight Information for " + flightInfo);
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalStrut(20));
        panel.add(infoLabel);
        panel.add(Box.createVerticalStrut(20));


        JButton backToUserPageButton = new JButton("Back to User Page");
        backToUserPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showUserPage(currentUsername); // Pass in either Guest or current Username
            }
        });
        backToUserPageButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(backToUserPageButton);
        panel.add(Box.createVerticalStrut(20));

        return panel;
    }


    private void continueAsGuest() {
        // Show the user page for the guest
        currentUsername = "";
        showUserPage(currentUsername);
    }

    private void showLoginScreen() {
        cardLayout.show(cardPanel, "login");
    }

    private void showMainScreen() {
        cardLayout.show(cardPanel, "main");

        // Clear the username and password fields
        usernameField.setText("");
        passwordField.setText("");
    }

    private void showUserPage(String username) {
        // Check if the user panel already exists and remove it
        Component[] components = cardPanel.getComponents();
        for (Component component : components) {
            if (component == userPanel) {
                cardPanel.remove(userPanel);
                break;
            }
        }

        // Create a new user panel and add it to the cardPanel
        userPanel = createUserPage(username);
        cardPanel.add(userPanel, "user");

        // Show user page
        cardLayout.show(cardPanel, "user");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backToMainButton) {
            // Show the main screen
            showMainScreen();
        } else if (e.getSource() == loginBackButton) {
            // Show the main screen when the back button is pressed on the login page
            showMainScreen();
        } else if (e.getActionCommand().equals("Register")) {
            // Show the registration screen
            cardLayout.show(cardPanel, "register");
        } else {
            // Show or hide the login screen
            showLoginScreen();
        }
    }

    public static void main(String[] args) {
        try {
            GUI gui = new GUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
