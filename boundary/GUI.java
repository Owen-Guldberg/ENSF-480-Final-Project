package boundary;

import controller.*;
import flightInfo.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;


public class GUI extends JFrame implements ActionListener {

    private JPanel cardPanel;
    private CardLayout cardLayout;
    private MainPanel mainPanel;
    private Login loginPanel;
    private Register registerPanel;
    private JPanel userPanel;
    private JPanel flightInfoPanel;  // New panel for flight information
    private JButton backToMainButton;
    private JButton actionButton; // Used for both login and register actions
    private JButton loginBackButton; // Separate button for the back action on the login page
    private JTextField usernameField;
    private JPasswordField passwordField;
    private String currentUsername; // Track username of currently logged in user
    private SystemController system = new SystemController();
    private FlightController flightController = new FlightController();
    private AircraftController aircraftController = new AircraftController();
    private AuthenticationController authController = new AuthenticationController();

    public GUI() {
        setTitle("Skyward Bound Flight Reservation System");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        mainPanel = new MainPanel(
            e -> showRegisterScreen(), 
            e -> showLoginScreen(), 
            e -> continueAsGuest()
        );
        cardPanel.add(mainPanel, "main");

        loginPanel = new Login(
            e -> handleLogin(), 
            e -> showMainScreen()
            //e -> handleRegister()
        );
        cardPanel.add(loginPanel, "login");

        registerPanel = new Register(
            e -> handleRegister(), 
            e -> showMainScreen()
        );
        cardPanel.add(registerPanel, "register");

        userPanel = new JPanel();
        cardPanel.add(userPanel, "user");

        add(cardPanel);

        cardLayout.show(cardPanel, "main");

        setVisible(true);
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

        // Panel to hold both location menus
        JPanel locationMenusPanel = new JPanel();
        locationMenusPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Or use GridLayout

        // Add scrollable menus for 'Origin' and 'Destination'
        JScrollPane originScrollPane = createLocationMenu("Origin Locations", true);
        JScrollPane destinationScrollPane = createLocationMenu("Destination Locations", false);

        locationMenusPanel.add(originScrollPane);
        locationMenusPanel.add(destinationScrollPane);

        userPage.add(locationMenusPanel);
        userPage.add(Box.createVerticalStrut(10));

        JButton viewFlightsButton = new JButton("View Available Flights");
        viewFlightsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedOrigin.getAirportName() != null && selectedDestination.getAirportName() != null) {
                    // Scrollable menu for 'Flights'
                    JPanel flightsPanel = createFlightsPanel();
                    cardPanel.add(flightsPanel, "flightsPanel");
                    cardLayout.show(cardPanel, "flightsPanel");
                } else {
                    JOptionPane.showMessageDialog(userPage, "Please select both an origin and a destination.");
                }
            }
        });
        userPage.add(viewFlightsButton);

        userPage.add(Box.createVerticalStrut(10));

        userPage.add(actionButton);

        return userPage;
    }

    private JScrollPane createLocationMenu(String title, boolean isOrigin) {
        ArrayList<Location> locations = system.getLocations();
        ArrayList<String> locationStrings = system.getLocationStrings(locations);
        JList<String> locationList = new JList<>(locationStrings.toArray(new String[0]));
        locationList.setFont(new Font(locationList.getFont().getName(), Font.PLAIN, 16));
        locationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane locationScrollPane = new JScrollPane(locationList);
        locationScrollPane.setPreferredSize(new Dimension(400, 400));

        TitledBorder titledBorder = BorderFactory.createTitledBorder(title);
        locationScrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10), titledBorder));

        // Add listener to handle location selection
        locationList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selectedLocation = locationList.getSelectedValue();
                    if (isOrigin) {
                        handleSelectedOrigin(selectedLocation);
                    } else {
                        handleSelectedDestination(selectedLocation);
                    }
                }
            }
        });

        return locationScrollPane;
    }

    private Location selectedOrigin;
    private Location selectedDestination;

    private void handleSelectedOrigin(String origin) {
        selectedOrigin = system.getLocationByName(origin);
    }
    
    private void handleSelectedDestination(String destination) {
        selectedDestination = system.getLocationByName(destination);
    }

    private JPanel createFlightsPanel() {
        JPanel flightsPanel = new JPanel();
        flightsPanel.setLayout(new BoxLayout(flightsPanel, BoxLayout.Y_AXIS));
        
        JLabel flightsLabel = new JLabel("Flights");
        flightsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JScrollPane flightsScrollPane = createFlightMenu("Flights");
        flightsPanel.add(flightsScrollPane);
        flightsPanel.add(Box.createVerticalStrut(10));
    
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "user"));
        flightsPanel.add(backButton);

        return flightsPanel;
    }
    
    private JScrollPane createFlightMenu(String menuTitle) {
        ArrayList<Flight> flights = flightController.flightsByLocation(selectedOrigin, selectedDestination);

        JList<String> list = new JList<>(flightController.browseFlightNums(flights).toArray(new String[0]));
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
    private JPanel createFlightInfoPanel(String flightNum) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        String flightInfo = system.getFlightByNum(flightNum).toString();
        JLabel infoLabel = new JLabel("Flight Information");
        infoLabel.setFont(new Font(infoLabel.getFont().getName(), Font.PLAIN, 20));
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        infoPanel.setBorder(BorderFactory.createEmptyBorder());
        infoPanel.setBackground(Color.WHITE);

        JLabel infoLabel2 = new JLabel(flightInfo);
        infoLabel2.setFont(new Font(infoLabel.getFont().getName(), Font.PLAIN, 16));

        panel.add(Box.createVerticalStrut(20));
        panel.add(infoLabel);
        panel.add(Box.createVerticalStrut(10));
        infoPanel.add(infoLabel2);
        panel.add(infoPanel);
        panel.add(Box.createVerticalStrut(20));

        JButton viewSeatsButton = new JButton("View Seats");
        viewSeatsButton.addActionListener(e -> showSeatChart(flightNum));

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "flightsPanel"));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(viewSeatsButton);
        buttonPanel.add(backButton);
        panel.add(buttonPanel);
        add(Box.createVerticalStrut(10));

        return panel;
    }

    private void showSeatChart(String flightNum) {
        Aircraft aircraft = system.getFlightByNum(flightNum).getAircraft();
        ArrayList<Seat> seats = aircraftController.seatByAircraft(aircraft);
        SeatChart seatChart = new SeatChart(currentUsername, seats, aircraftController, flightNum, system, cardPanel, cardLayout);

        cardPanel.add(seatChart, "seatChart");
        cardLayout.show(cardPanel, "seatChart");
    }

    private void continueAsGuest() {
        // Show the user page for the guest
        currentUsername = "";
        showUserPage(currentUsername);
    }

    private void showRegisterScreen() {
        // Switch to the register panel
        cardLayout.show(cardPanel, "register");
    }

    private void showLoginScreen() {
        cardLayout.show(cardPanel, "login");
    }

    private void showMainScreen() {
        cardLayout.show(cardPanel, "main");

        // Clear the username and password fields
        usernameField.setText("");
        passwordField.setText("");
        selectedOrigin = null;
        selectedDestination = null;
    }

    private void handleLogin() {
        String email = loginPanel.getEmail();
        char[] password = loginPanel.getPassword();
    
        if (authController.loginUser(email, new String(password))) {
            // Login successful
            currentUsername = email;
            showUserPage(email); // Show the user page for the logged-in user
        } else {
            // Login failed
            JOptionPane.showMessageDialog(this, "Invalid email or password.");
        }
    }

    private void handleRegister() {
        String firstName = registerPanel.getFirstName();
        String lastName = registerPanel.getLastName();
        String email = registerPanel.getEmail();
        char[] password = registerPanel.getPassword();
    
        int houseNum = Integer.parseInt(registerPanel.getHouseNum()); // Assuming house number is always a valid integer
        String streetName = registerPanel.getStreetName();
        String city = registerPanel.getCity();
        String country = registerPanel.getCountry();
        String postalCode = registerPanel.getPostalCode();

        // Registering user through AuthenticationController
        boolean registrationSuccess = authController.registerNewUser(firstName, lastName, email, new String(password), 
                                                                    houseNum, streetName, city, 
                                                                    country, postalCode);

        if (registrationSuccess) {
            // Registration successful
            showLoginScreen();
        } else {
            // Registration failed
            JOptionPane.showMessageDialog(this, "Registration failed. User might already exist.");
        }
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
