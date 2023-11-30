package boundary;

import controller.*;
import flightInfo.*;
import role.CrewMember;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalTime;

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
    private JButton viewAllFlightsButton; // View flighst as admin
    private JScrollPane allFlightsScrollPane; // View flights as admin
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
        JPanel userPage = new JPanel();
        userPage.setLayout(new BoxLayout(userPage, BoxLayout.Y_AXIS));

        JLabel welcomeLabel = new JLabel("Welcome, " + username+ "!");
        welcomeLabel.setFont(new Font(welcomeLabel.getFont().getName(), Font.PLAIN, 20));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        userPage.add(Box.createVerticalStrut(20));
        userPage.add(welcomeLabel);
        userPage.add(Box.createVerticalStrut(20));



        JLabel enterLabel = new JLabel("Please select flights to show passengers");
        enterLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        userPage.add(enterLabel);
        userPage.add(Box.createVerticalStrut(10));
        JPanel locationMenusPanel = new JPanel();
        locationMenusPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); 
        JScrollPane CrewMemberScrollPane = createCrewMemberMenu("Crew Member flights",username);
        locationMenusPanel.add(CrewMemberScrollPane);
        userPage.add(locationMenusPanel);
        userPage.add(Box.createVerticalStrut(10));


        userPage.add(Box.createVerticalStrut(100));

        // Logout or Return to Home Page Button
        actionButton = username.isEmpty() ? new JButton("Return to Home Page") : new JButton("Logout");
        actionButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        actionButton.addActionListener(e -> showMainScreen());
        userPage.add(actionButton);
        userPage.add(Box.createVerticalStrut(20));

        return userPage;    
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
    private JScrollPane createCrewMemberMenu(String title, String username) {
        ArrayList<Flight> flights = system.getFlightsCrewmembers(username);
        ArrayList<String> flightStrings = system.getFlightStrings(flights);
        JList<String> flightList = new JList<>(flightStrings.toArray(new String[0]));

        flightList.setFont(new Font(flightList.getFont().getName(), Font.PLAIN, 16));
        flightList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane flightScrollPane = new JScrollPane(flightList);
        flightScrollPane.setPreferredSize(new Dimension(400, 400));

        TitledBorder titledBorder = BorderFactory.createTitledBorder(title);
        flightScrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10), titledBorder));

        // Add listener to handle location selection
        flightList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selectedFlight = flightList.getSelectedValue();
                    // handleSelectedFlightInfo(selectedFlight);

                }
            }
        });

        return flightScrollPane;
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
    
        if (authController.loginCrewMember(email, new String(password))) {
            currentUsername = email;
            showCrewMemberPage(email);
        } else if (authController.loginUser(email, new String(password))) {
            // Login successful for a regular user
            currentUsername = email;
            showUserPage(email);
        } else if (email.equals("admin") && new String(password).equals("admin")) {
            // Login successful for admin
            showAdminPage();
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

    // Method to create admin page
    private void showAdminPage() {
        // Check if the user panel already exists and remove it
        Component[] components = cardPanel.getComponents();
        for (Component component : components) {
            if (component == userPanel) {
                cardPanel.remove(userPanel);
                break;
            }
        }
    
        // Create a new admin panel and add it to the cardPanel
        userPanel = createAdminPage();
        cardPanel.add(userPanel, "user");
        
        cardLayout.show(cardPanel, "user");
    }

    private void handleAddFlight(Location origin, Location destination, String flightNum, String date, String departureTime, String arrivalTime, String flightTime, Aircraft aircraft) {

        // Create a Flight object
        Flight newFlight = new Flight(origin, destination, flightNum, date, departureTime, arrivalTime, flightTime, aircraft);

        // Call the addFlight method
        boolean addSuccess = system.addFlight(newFlight);

        if (addSuccess) {
            // Flight added successfully
            JOptionPane.showMessageDialog(this, "Flight added successfully.");
            showAdminPage(); 
        } else {
            // Flight addition failed
            JOptionPane.showMessageDialog(this, "Failed to add the flight.");
        }
    }

    // Admin panel content
    private JPanel createAdminPage() {
        JPanel adminPage = new JPanel();
        adminPage.setLayout(new BoxLayout(adminPage, BoxLayout.Y_AXIS));
    
        JLabel welcomeLabel = new JLabel("Welcome, Admin!");
        welcomeLabel.setFont(new Font(welcomeLabel.getFont().getName(), Font.PLAIN, 20));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        adminPage.add(Box.createVerticalStrut(20));
        adminPage.add(welcomeLabel);
        adminPage.add(Box.createVerticalStrut(20));
    
        viewAllFlightsButton = new JButton("Manage Flights");
        viewAllFlightsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewAllFlightsButton.addActionListener(e -> showAllFlights());
        adminPage.add(viewAllFlightsButton);
        adminPage.add(Box.createVerticalStrut(20));
    
        JButton manageAircraftButton = new JButton("Manage Aircraft");
        manageAircraftButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        manageAircraftButton.addActionListener(e -> showManageAircraftPage());
        adminPage.add(manageAircraftButton);
        adminPage.add(Box.createVerticalStrut(20));
    
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> showMainScreen());
        adminPage.add(backButton);
        adminPage.add(Box.createVerticalStrut(20));
    
        return adminPage;
    }

    private void showManageAircraftPage() {
        // Create and show the page for managing aircraft
        JPanel manageAircraftPanel = createManageAircraftPage();
        cardPanel.add(manageAircraftPanel, "manageAircraftPanel");
        cardLayout.show(cardPanel, "manageAircraftPanel");
    }

    private JPanel createManageAircraftPage() {
        JPanel manageAircraftPage = new JPanel();
        manageAircraftPage.setLayout(new BoxLayout(manageAircraftPage, BoxLayout.Y_AXIS));
    
        JLabel titleLabel = new JLabel("Manage Aircraft");
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.PLAIN, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        manageAircraftPage.add(Box.createVerticalStrut(20));
        manageAircraftPage.add(titleLabel);
        manageAircraftPage.add(Box.createVerticalStrut(20));
    
        // Get the list of aircraft from the system controller
        ArrayList<Aircraft> allAircrafts = system.getAircrafts();
    
        DefaultListModel<String> aircraftListModel = new DefaultListModel<>();
        for (Aircraft aircraft : allAircrafts) {
            aircraftListModel.addElement("Aircraft ID: " + aircraft.getId() + ", Aircraft Name: " + aircraft.getName());
        }
    
        JList<String> aircraftList = new JList<>(aircraftListModel);
        aircraftList.setFont(new Font(aircraftList.getFont().getName(), Font.PLAIN, 16));
        aircraftList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    
        // Create a JScrollPane for the JList
        JScrollPane aircraftScrollPane = new JScrollPane(aircraftList);
        aircraftScrollPane.setPreferredSize(new Dimension(600, 400));
    
        TitledBorder titledBorder = BorderFactory.createTitledBorder("All Aircraft");
        aircraftScrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10), titledBorder));
    
        manageAircraftPage.add(aircraftScrollPane);
        manageAircraftPage.add(Box.createVerticalStrut(20));
    
        JButton addAircraftButton = new JButton("Add Aircraft");
        addAircraftButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addAircraftButton.addActionListener(e -> showAddAircraftPage());
        manageAircraftPage.add(addAircraftButton);
        manageAircraftPage.add(Box.createVerticalStrut(20));
    
        JButton backButton = new JButton("Back");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> showAdminPage());
        manageAircraftPage.add(backButton);
        manageAircraftPage.add(Box.createVerticalStrut(20));
    
        return manageAircraftPage;
    }
    
    private void showAddAircraftPage() {
        // Create and show the page for adding an aircraft
        JPanel addAircraftPanel = createAddAircraftPage();
        cardPanel.add(addAircraftPanel, "addAircraftPanel");
        cardLayout.show(cardPanel, "addAircraftPanel");
    }

    private JPanel createAddAircraftPage() {
        JPanel addAircraftPage = new JPanel();
        addAircraftPage.setLayout(new BoxLayout(addAircraftPage, BoxLayout.Y_AXIS));
    
        JLabel titleLabel = new JLabel("Add New Aircraft");
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.PLAIN, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        addAircraftPage.add(Box.createVerticalStrut(20));
        addAircraftPage.add(titleLabel);
        addAircraftPage.add(Box.createVerticalStrut(20));
    
        // Add text fields for ID, Name, and Capacity
        JPanel idPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        idPanel.add(new JLabel("ID: "));
        JTextField idField = new JTextField(15);
        idPanel.add(idField);
        addAircraftPage.add(idPanel);
        addAircraftPage.add(Box.createVerticalStrut(10));
    
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        namePanel.add(new JLabel("Name: "));
        JTextField nameField = new JTextField(15);
        namePanel.add(nameField);
        addAircraftPage.add(namePanel);
        addAircraftPage.add(Box.createVerticalStrut(10));
    
        JPanel capacityPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        capacityPanel.add(new JLabel("Capacity: "));
        JTextField capacityField = new JTextField(15);
        capacityPanel.add(capacityField);
        addAircraftPage.add(capacityPanel);
        addAircraftPage.add(Box.createVerticalStrut(20));
    
        JButton addButton = new JButton("Add Aircraft");
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addButton.addActionListener(e -> 
            handleAddAircraft(
                Integer.parseInt(idField.getText()), 
                nameField.getText(), 
                Integer.parseInt(capacityField.getText())
            )
        );

        addAircraftPage.add(addButton);
        addAircraftPage.add(Box.createVerticalStrut(20));
    
        JButton backButton = new JButton("Back");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> showManageAircraftPage());
        addAircraftPage.add(backButton);
        addAircraftPage.add(Box.createVerticalStrut(20));
    
        return addAircraftPage;
    }
    

    private void handleAddAircraft(int id, String name, int capacity) {
        // Create the Aircraft object using the provided parameters
        Aircraft newAircraft = new Aircraft(id, name, null, capacity);
        boolean addSuccess = system.addAircraft(newAircraft);
    
        if (addSuccess) {
            // Aircraft added successfully
            JOptionPane.showMessageDialog(this, "Aircraft added successfully.");
        } else {
            // Aircraft addition failed
            JOptionPane.showMessageDialog(this, "Failed to add the aircraft.");
        }
        showManageAircraftPage();
    }
    
    
    
    
    

    private void showAddFlightPage() {
        // Create and show the page for adding a flight
        JPanel addFlightPanel = createAddFlightPage();
        cardPanel.add(addFlightPanel, "addFlightPanel");
        cardLayout.show(cardPanel, "addFlightPanel");
    }

    private JPanel createAddFlightPage() {
        JPanel addFlightPage = new JPanel();
        addFlightPage.setLayout(new BoxLayout(addFlightPage, BoxLayout.Y_AXIS));
    
        JLabel titleLabel = new JLabel("Add New Flight");
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.PLAIN, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        addFlightPage.add(Box.createVerticalStrut(20));
        addFlightPage.add(titleLabel);
        addFlightPage.add(Box.createVerticalStrut(20));
    
        // Add Origin
        JPanel originPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        originPanel.add(new JLabel("Airport 1: "));
        JTextField airport1Field = new JTextField(15);
        originPanel.add(airport1Field);
        originPanel.add(new JLabel("City 1: "));
        JTextField city1Field = new JTextField(15);
        originPanel.add(city1Field);
        originPanel.add(new JLabel("Country 1: "));
        JTextField country1Field = new JTextField(15);
        originPanel.add(country1Field);
        addFlightPage.add(originPanel);
        addFlightPage.add(Box.createVerticalStrut(10));
    
        // Add Destination
        JPanel destinationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        destinationPanel.add(new JLabel("Airport 2: "));
        JTextField airport2Field = new JTextField(15);
        destinationPanel.add(airport2Field);
        destinationPanel.add(new JLabel("City 2: "));
        JTextField city2Field = new JTextField(15);
        destinationPanel.add(city2Field);
        destinationPanel.add(new JLabel("Country 2: "));
        JTextField country2Field = new JTextField(15);
        destinationPanel.add(country2Field);
        addFlightPage.add(destinationPanel);
        addFlightPage.add(Box.createVerticalStrut(10));
    
        // Add Flight Number
        JPanel flightNumberPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        flightNumberPanel.add(new JLabel("Flight Number: "));
        JTextField flightNumField = new JTextField(15);
        flightNumberPanel.add(flightNumField);
        addFlightPage.add(flightNumberPanel);
        addFlightPage.add(Box.createVerticalStrut(10));
    
        // Add Flight Date
        JPanel flightDatePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        flightDatePanel.add(new JLabel("Flight Date (YYYY-MM-DD): "));
        JTextField dateField = new JTextField(15);
        flightDatePanel.add(dateField);
        addFlightPage.add(flightDatePanel);
        addFlightPage.add(Box.createVerticalStrut(10));
    
        // Add Departure Time
        JPanel departureTimePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        departureTimePanel.add(new JLabel("Departure Time (HH:mm): "));
        JTextField departureTimeField = new JTextField(15);
        departureTimePanel.add(departureTimeField);
        addFlightPage.add(departureTimePanel);
        addFlightPage.add(Box.createVerticalStrut(10));
    
        // Add Arrival Time
        JPanel arrivalTimePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        arrivalTimePanel.add(new JLabel("Arrival Time (HH:mm): "));
        JTextField arrivalTimeField = new JTextField(15);
        arrivalTimePanel.add(arrivalTimeField);
        addFlightPage.add(arrivalTimePanel);
        addFlightPage.add(Box.createVerticalStrut(10));
    
        // Add Flight Time
        JPanel flightTimePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        flightTimePanel.add(new JLabel("Flight Time (HH:mm): "));
        JTextField flightTimeField = new JTextField(15);
        flightTimePanel.add(flightTimeField);
        addFlightPage.add(flightTimePanel);
        addFlightPage.add(Box.createVerticalStrut(20));
    
        JButton addButton = new JButton("Add Flight");
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addButton.addActionListener(e -> handleAddFlight(
                new Location(
                airport1Field.getText(),
                city1Field.getText(),
                country1Field.getText()),
                
                new Location(
                airport2Field.getText(),
                city2Field.getText(),
                country2Field.getText()),

                flightNumField.getText(),
                dateField.getText(),
                departureTimeField.getText(),
                arrivalTimeField.getText(),
                flightTimeField.getText(),
                new Aircraft()
            ));

        addFlightPage.add(addButton);
        addFlightPage.add(Box.createVerticalStrut(20));
    
        JButton backButton = new JButton("Back");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> showAdminPage());
        addFlightPage.add(backButton);
        addFlightPage.add(Box.createVerticalStrut(20));
    
        return addFlightPage;
    }
    

    private void showAllFlights() {
        ArrayList<Flight> allFlights = system.getFlights(); // Adjust this based on your implementation
        ArrayList<String> flightStrings = system.getFlightStrings(allFlights);
    
        JList<String> allFlightsList = new JList<>(flightStrings.toArray(new String[0]));
        allFlightsList.setFont(new Font(allFlightsList.getFont().getName(), Font.PLAIN, 16));
        allFlightsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    
        // Add ListSelectionListener to the flight list
        allFlightsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedIndex = allFlightsList.getSelectedIndex();
                    if (selectedIndex != -1) {
                        handleSelectedAdminFlight(allFlights.get(selectedIndex));
                        allFlightsList.clearSelection(); // Deselect the item after handling
                    }
                }
            }
        });
    
        JScrollPane allFlightsScrollPane = new JScrollPane(allFlightsList);
        allFlightsScrollPane.setPreferredSize(new Dimension(600, 400));
    
        TitledBorder titledBorder = BorderFactory.createTitledBorder("All Flights");
        allFlightsScrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10), titledBorder));
    
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "user"));
    
        // Add the "Add Flight" button
        JButton addFlightButton = new JButton("Add Flight");
        addFlightButton.addActionListener(e -> showAddFlightPage());
        
        JPanel allFlightsPanel = new JPanel();
        allFlightsPanel.setLayout(new BoxLayout(allFlightsPanel, BoxLayout.Y_AXIS));
        allFlightsPanel.add(allFlightsScrollPane);
        allFlightsPanel.add(Box.createVerticalStrut(20));
        allFlightsPanel.add(addFlightButton);
        allFlightsPanel.add(Box.createVerticalStrut(20));
        allFlightsPanel.add(backButton);
    
        cardPanel.add(allFlightsPanel, "allFlightsPanel");
        cardLayout.show(cardPanel, "allFlightsPanel");
    }
    

    private void handleSelectedAdminFlight(Flight selectedFlight) {
        int choice = JOptionPane.showOptionDialog(
                this,
                "Do you want to cancel the flight?",
                "Cancel Flight",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"Cancel flight", "Back"},
                "Back");
    
        if (choice == JOptionPane.YES_OPTION) {
            // User chose to cancel the flight
            boolean cancellationSuccess = system.cancelFlight(selectedFlight);
    
            if (cancellationSuccess) {
                // Cancellation successful
                JOptionPane.showMessageDialog(this, "Flight canceled successfully.");
    
                // Navigate back to the "Show All Flights" page
                showAllFlights();
            } else {
                // Cancellation failed
                JOptionPane.showMessageDialog(this, "Failed to cancel the flight.");
            }
        } else {
            // User chose to go back
            // Do nothing or add any additional logic as needed
        }
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
