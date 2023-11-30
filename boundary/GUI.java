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
        setBackground(Color.WHITE);
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
            e -> showMainScreen(),
            e -> showRegisterScreen()
        );
        cardPanel.add(loginPanel, "login");

        registerPanel = new Register(
            e -> handleRegister(), 
            e -> showMainScreen(),
            e -> showLoginScreen()
        );
        cardPanel.add(registerPanel, "register");

        userPanel = new JPanel();
        cardPanel.add(userPanel, "user");

        add(cardPanel);

        cardLayout.show(cardPanel, "main");

        setVisible(true);
    }
    private JPanel createCrewMemberPage(String username){
        return new JPanel();
    }
    private JPanel createUserPage(String username) {
        JPanel userPage = new JPanel();
        userPage.setLayout(new BoxLayout(userPage, BoxLayout.Y_AXIS));

        JLabel welcomeLabel = new JLabel("Welcome, " + (username.isEmpty() ? "Guest" : system.getNameByEmail(username)) + "!");
        welcomeLabel.setFont(new Font(welcomeLabel.getFont().getName(), Font.PLAIN, 20));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        userPage.add(Box.createVerticalStrut(20));
        userPage.add(welcomeLabel);
        userPage.add(Box.createVerticalStrut(20));

        if (!username.isEmpty()) {
            JButton myFlightsButton = new JButton("View My Flights");
            myFlightsButton.addActionListener(e -> showMyFlights(username));
            myFlightsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            userPage.add(myFlightsButton);
            userPage.add(Box.createVerticalStrut(10));
        }

        JLabel enterLabel = new JLabel("Please select an origin and a destination below.");
        enterLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        userPage.add(enterLabel);
        userPage.add(Box.createVerticalStrut(10));
        JPanel locationMenusPanel = new JPanel();
        locationMenusPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); 
        JScrollPane originScrollPane = createLocationMenu("Origin Locations", true);
        JScrollPane destinationScrollPane = createLocationMenu("Destination Locations", false);
        locationMenusPanel.add(originScrollPane);
        locationMenusPanel.add(destinationScrollPane);
        userPage.add(locationMenusPanel);
        userPage.add(Box.createVerticalStrut(10));

        // View Available Flights Button
        JButton viewFlightsButton = new JButton("View Available Flights");
        viewFlightsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewFlightsButton.addActionListener(e -> {
            if (selectedOrigin.getAirportName() != null && selectedDestination.getAirportName() != null) {
                JPanel flightsPanel = createFlightsPanel();
                cardPanel.add(flightsPanel, "flightsPanel");
                cardLayout.show(cardPanel, "flightsPanel");
            } else {
                JOptionPane.showMessageDialog(userPage, "Please select both an origin and a destination.");
            }
        });
        userPage.add(viewFlightsButton);
        userPage.add(Box.createVerticalStrut(100));

        // Logout or Return to Home Page Button
        actionButton = username.isEmpty() ? new JButton("Return to Home Page") : new JButton("Logout");
        actionButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        actionButton.addActionListener(e -> showMainScreen());
        userPage.add(actionButton);
        userPage.add(Box.createVerticalStrut(20));

        return userPage;
    }

    private void showMyFlights(String username) {
        PaymentController paymentController = new PaymentController(username, null, "");
        MyFlights myFlightsPanel = new MyFlights(paymentController, username, -1, system, cardPanel, cardLayout);
        cardPanel.add(myFlightsPanel, "myFlights");
        cardLayout.show(cardPanel, "myFlights");
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
        
        flightsPanel.add(Box.createVerticalStrut(20));
        JLabel flightsLabel = new JLabel("Browse Flights");
        flightsLabel.setFont(new Font(flightsLabel.getFont().getName(), Font.PLAIN, 20));
        flightsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        flightsPanel.add(flightsLabel);
        flightsPanel.add(Box.createVerticalStrut(20));

        JScrollPane flightsScrollPane = createFlightMenu("Flights");
        flightsScrollPane.setMaximumSize(new Dimension(900, 600));
        flightsPanel.add(flightsScrollPane);
        flightsPanel.add(Box.createVerticalStrut(40));
    
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "user"));
        flightsPanel.add(backButton);

        return flightsPanel;
    }
    
    private JScrollPane createFlightMenu(String menuTitle) {
        ArrayList<String> flightStrings = flightController.browseFlightNums(selectedOrigin, selectedDestination);
        JPanel flightsPanel = new JPanel();
        flightsPanel.setLayout(new BoxLayout(flightsPanel, BoxLayout.Y_AXIS));
    
        for (String flightNum : flightStrings) {
            JButton flightButton = new JButton(flightNum);
            flightButton.setMaximumSize(new Dimension(900, 30)); // Set maximum size for uniformity
            flightButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    
            flightButton.addActionListener(e -> handleSelectedFlight(flightNum));
    
            flightsPanel.add(flightButton);
            flightsPanel.add(Box.createVerticalStrut(5)); // Spacing between buttons
        }
    
        JScrollPane scrollPane = new JScrollPane(flightsPanel);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(menuTitle);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10), titledBorder));
    
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
        registerPanel.clearFields();
        cardLayout.show(cardPanel, "register");
    }

    private void showLoginScreen() {
        loginPanel.clearFields();
        cardLayout.show(cardPanel, "login");
    }

    private void showMainScreen() {
        cardLayout.show(cardPanel, "main");

        // Clear the username and password fields
        currentUsername = "";
        //usernameField.setText("");
        //passwordField.setText("");
        selectedOrigin = null;
        selectedDestination = null;
    }

    private void handleLogin() {
        String email = loginPanel.getEmail();
        char[] password = loginPanel.getPassword();

        if (email.isEmpty() || password.length == 0) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(authController.loginCrewMember(email, new String(password))){
            currentUsername = email;
            showCrewMemberPage(email);
            // implement crew member, should see flights, cancel flight
        }
        else if (authController.loginUser(email, new String(password))) {
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
        String houseNumStr = registerPanel.getHouseNum().trim();
        String streetName = registerPanel.getStreetName();
        String city = registerPanel.getCity();
        String country = registerPanel.getCountry();
        String postalCode = registerPanel.getPostalCode();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() ||
            houseNumStr.isEmpty() || streetName.isEmpty() || city.isEmpty() ||
            country.isEmpty() || postalCode.isEmpty() || password.length == 0) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate and parse house number
        int houseNum;
        try {
            houseNum = Integer.parseInt(houseNumStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid house number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

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
    private void showCrewMemberPage(String username) {
            Component[] components = cardPanel.getComponents();
        for (Component component : components) {
            if (component == userPanel) {
                cardPanel.remove(userPanel);
                break;
            }
        }

        // Create a new user panel and add it to the cardPanel
        userPanel = createCrewMemberPage(username);
        cardPanel.add(userPanel, "user");

        // Show user page
        cardLayout.show(cardPanel, "user");
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
