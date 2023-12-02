package boundary;

import controller.*;
import flightInfo.*;
import role.CrewMember;
import role.RegisteredCustomer;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GUI extends JFrame implements ActionListener {

    private JPanel cardPanel;
    private CardLayout cardLayout;
    private MainPanel mainPanel;
    private Login loginPanel;
    private Register registerPanel;
    private JPanel userPanel;
    private UserPagePanel userPagePanel;
    private JPanel flightInfoPanel;  // New panel for flight information
    private JButton backToMainButton;
    private JButton actionButton; // Used for both login and register actions
    private JButton loginBackButton; // Separate button for the back action on the login page
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton viewAllFlightsButton; // View flighst as admin
    private JScrollPane allFlightsScrollPane; // View flights as admin
    private JList<String> flightList;
    private JList<String> allFlightsList;
    private String currentEmail; // Track username of currently logged in user
    private String selectedOriginName;
    private String selectedDestinationName;
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
        userPage.setBackground(Color.WHITE);

        JLabel welcomeLabel = new JLabel("Welcome, " + system.getNameByEmail(username) + "!");
        welcomeLabel.setFont(new Font(welcomeLabel.getFont().getName(), Font.BOLD, 18));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        userPage.add(Box.createVerticalStrut(20));
        userPage.add(welcomeLabel);
        userPage.add(Box.createVerticalStrut(20));

        JLabel enterLabel = new JLabel("Please select flights to show passengers.");
        enterLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        userPage.add(enterLabel);
        userPage.add(Box.createVerticalStrut(10));

        JPanel locationMenusPanel = new JPanel();
        locationMenusPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); 
        locationMenusPanel.setBackground(Color.WHITE);
        JScrollPane CrewMemberScrollPane = createCrewMemberMenu("Crew Member Flights",username);
        locationMenusPanel.add(CrewMemberScrollPane);
        userPage.add(locationMenusPanel);
        userPage.add(Box.createVerticalStrut(40));

        JButton viewPassengersButton = new JButton("View Passengers");
        viewPassengersButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewPassengersButton.addActionListener(e -> handleViewPassengers(username));
        styleButton(viewPassengersButton);
        userPage.add(viewPassengersButton);

        userPage.add(Box.createVerticalStrut(100));

        // Logout or Return to Home Page Button
        actionButton = username.isEmpty() ? new JButton("Return to Home Page") : new JButton("Logout");
        actionButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        actionButton.addActionListener(e -> showMainScreen());
        styleButton(actionButton);
        userPage.add(actionButton);
        userPage.add(Box.createVerticalStrut(80));

        return userPage;    
    }

    private void handleViewPassengers(String username) {
        String selectedFlight = flightList.getSelectedValue();
        System.out.println(selectedFlight);
        if (selectedFlight != null) {
            // Use regular expression to extract the flight number
            String regex = "Flight Number:</u> (SB-\\d+)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(selectedFlight);

            if (matcher.find()) {
                String flightNumber = matcher.group(1);
                System.out.println("Selected Flight: " + flightNumber);
                userPanel = createPassengerPage(username, flightNumber);
                cardPanel.add(userPanel, "user");

        // Show user page
        cardLayout.show(cardPanel, "user");
                
            } else {
                JOptionPane.showMessageDialog(this, "Flight number not found in the selected flight information.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a flight first.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private JPanel createPassengerPage(String username, String flightNumber) {
        JPanel userPage = new JPanel();
        userPage.setLayout(new BoxLayout(userPage, BoxLayout.Y_AXIS));
        userPage.setBackground(Color.WHITE);
    
        JLabel welcomeLabel = new JLabel("Flight : " + flightNumber + " Passengers");
        welcomeLabel.setFont(new Font(welcomeLabel.getFont().getName(), Font.BOLD, 18));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        userPage.add(Box.createVerticalStrut(20));
        userPage.add(welcomeLabel);
        userPage.add(Box.createVerticalStrut(20));
    
        userPage.add(Box.createVerticalStrut(10));
        JPanel locationMenusPanel = new JPanel();
        locationMenusPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        locationMenusPanel.setBackground(Color.WHITE);
    
        JScrollPane CrewMemberScrollPane = createFlightPassengerMenu("Passengers", flightNumber);
        // set the scroll pane size
        if (CrewMemberScrollPane != null) {
            CrewMemberScrollPane.setPreferredSize(new Dimension(600, 500));
        
        }
    
        // Check if there are passengers to display
        if (CrewMemberScrollPane != null && CrewMemberScrollPane.getComponentCount() > 0) {
            locationMenusPanel.add(CrewMemberScrollPane);
            userPage.add(locationMenusPanel);
            userPage.add(Box.createVerticalStrut(10));
        } else {
            // Handle case when there are no passengers
            JLabel noPassengersLabel = new JLabel("No passengers for this flight.");
            noPassengersLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            userPage.add(noPassengersLabel);
            userPage.add(Box.createVerticalStrut(10));
        }
    
        // Logout or Return to Home Page Button
        userPage.add(Box.createVerticalStrut(30));
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> showCrewMemberPage(username));
        styleButton(backButton);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        userPage.add(backButton);
        userPage.add(Box.createVerticalStrut(60));
    
        actionButton = username.isEmpty() ? new JButton("Return to Home Page") : new JButton("Logout");
        actionButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        actionButton.addActionListener(e -> showMainScreen());
        styleButton(actionButton);
        userPage.add(actionButton);
        userPage.add(Box.createVerticalStrut(80));
        return userPage;
    }
    
    private void showMyFlights(String username) {
        PaymentController paymentController = new PaymentController(username, null, "");
        MyFlights myFlightsPanel = new MyFlights(paymentController, username, -1, system, cardPanel, cardLayout);
        cardPanel.add(myFlightsPanel, "myFlights");
        cardLayout.show(cardPanel, "myFlights");
    }
    private JScrollPane createFlightPassengerMenu(String title, String flightnumber) {
        ArrayList<RegisteredCustomer> customers = system.getFlightsPassengers(flightnumber);
        if (customers == null) {
            // Handle the case where customers is null, for example:
            return null; // or return an empty JScrollPane, or handle it in a way that fits your requirements
        }
            System.out.println(customers.size());

        ArrayList<String> customerStrings = system.getPassengerStrings(customers);
        JList<String> flightList = new JList<>(customerStrings.toArray(new String[0]));
    
        flightList.setFont(new Font(flightList.getFont().getName(), Font.PLAIN, 16));
        flightList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane flightScrollPane = new JScrollPane(flightList);
        flightScrollPane.setPreferredSize(new Dimension(400, 400));
    
        TitledBorder titledBorder = BorderFactory.createTitledBorder(title);
        flightScrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10), titledBorder));
    
        return flightScrollPane;
    }
    
    private JScrollPane createCrewMemberMenu(String title, String username) {
        ArrayList<Flight> flights = system.getFlightsCrewmembers(username);
        ArrayList<String> flightStrings = system.getFlightStrings(flights);
        flightList = new JList<>(flightStrings.toArray(new String[0]));

        flightList.setFont(new Font(flightList.getFont().getName(), Font.PLAIN, 16));
        flightList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        flightList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                Color evenColor = new Color(230, 240, 255);
                Color oddColor = Color.WHITE;

                if (isSelected) {
                    c.setBackground(new Color(0, 102, 204));
                } else {
                    c.setBackground(index % 2 == 0 ? evenColor : oddColor);
                }

                return c;
            }
        });
        // //add bottom border to each entry
        // flightList.addListSelectionListener(new ListSelectionListener() {
        //     @Override
        //     public void valueChanged(ListSelectionEvent e) {
        //         flightList.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        //     }
        // });

        JScrollPane flightScrollPane = new JScrollPane(flightList);
        flightScrollPane.setPreferredSize(new Dimension(800, 500));

        TitledBorder titledBorder = BorderFactory.createTitledBorder(title);
        flightScrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10), titledBorder));

        return flightScrollPane;
    }

    private Location handleSelectedOrigin(String origin) {
        return system.getLocationByName(origin);
    }
    
    private Location handleSelectedDestination(String destination) {
        return system.getLocationByName(destination);
    }

    private JPanel createFlightsPanel() {
        JPanel flightsPanel = new JPanel();
        flightsPanel.setLayout(new BoxLayout(flightsPanel, BoxLayout.Y_AXIS));
        flightsPanel.setBackground(Color.WHITE);
        
        flightsPanel.add(Box.createVerticalStrut(20));
        JLabel flightsLabel = new JLabel("Browse Flights", SwingConstants.CENTER);
        flightsLabel.setFont(new Font(flightsLabel.getFont().getName(), Font.BOLD, 18));
        flightsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        //flightsPanel.add(flightsLabel);
        flightsPanel.add(flightsLabel, BorderLayout.NORTH);

        flightsPanel.add(Box.createVerticalStrut(20));

        JScrollPane flightsScrollPane = createFlightMenu("Flights");
        flightsScrollPane.setMaximumSize(new Dimension(900, 600)); // Set preferred size
        flightsPanel.add(flightsScrollPane, BorderLayout.CENTER); // Add the scroll pane to the center
    
        flightsPanel.add(Box.createVerticalStrut(20));
        JButton backButton = new JButton("Back");
        styleButton(backButton);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "user"));
        flightsPanel.add(backButton);
        flightsPanel.add(Box.createVerticalStrut(20));

        return flightsPanel;
    }
    
    private JScrollPane createFlightMenu(String menuTitle) {
        ArrayList<String> flightStrings = flightController.browseFlightNums(handleSelectedOrigin(selectedOriginName), handleSelectedDestination(selectedDestinationName));
        JPanel flightsPanel = new JPanel();
        flightsPanel.setLayout(new BoxLayout(flightsPanel, BoxLayout.Y_AXIS));
    
        for (String flightNum : flightStrings) {
            JButton flightButton = new JButton(flightNum);
            flightButton.setMaximumSize(new Dimension(900, 30)); // Set maximum size for uniformity
            flightButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            styleButton(flightButton);
    
            flightButton.addActionListener(e -> handleSelectedFlight(flightNum));
    
            flightsPanel.add(flightButton);
            flightsPanel.add(Box.createVerticalStrut(5)); // Spacing between buttons
        }
        //flightsPanel.setMinimumSize(new Dimension(900, flightsPanel.getPreferredSize().height));
    
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
        panel.setBackground(Color.WHITE);

        String flightInfo = system.getFlightByNum(flightNum).toString();
        JLabel infoLabel = new JLabel("Flight Information", SwingConstants.CENTER);
        infoLabel.setFont(new Font(infoLabel.getFont().getName(), Font.BOLD, 18));
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        infoPanel.setBorder(BorderFactory.createEmptyBorder());
        infoPanel.setBackground(Color.WHITE);

        JLabel infoLabel2 = new JLabel(flightInfo);
        infoLabel2.setFont(new Font(infoLabel.getFont().getName(), Font.PLAIN, 16));

        ImageIcon imageIcon = new ImageIcon(getClass().getResource("airline.png"));
        JLabel label = new JLabel(imageIcon);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalStrut(20));
        panel.add(infoLabel);
        panel.add(Box.createVerticalStrut(30));
        infoPanel.add(infoLabel2);
        infoPanel.setMaximumSize(new Dimension(800, 80));
        panel.add(infoPanel);
        panel.add(label);
        panel.add(Box.createVerticalStrut(30));

        JButton backButton = new JButton("Back");
        styleButton(backButton);
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "flightsPanel"));

        JButton viewSeatsButton = new JButton("View Seats");
        styleButton(viewSeatsButton);
        viewSeatsButton.addActionListener(e -> showSeatChart(flightNum));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        // buttonPanel.add(viewSeatsButton);
        buttonPanel.add(backButton);
        buttonPanel.add(viewSeatsButton);
        panel.add(buttonPanel);
        add(Box.createVerticalStrut(10));

        return panel;
    }

    private void showSeatChart(String flightNum) {
        Aircraft aircraft = system.getFlightByNum(flightNum).getAircraft();
        ArrayList<Seat> seats = aircraftController.seatByAircraft(aircraft);

        for (Seat seat : seats) {
            for (Ticket ticket : system.getTickets()) {
                if (ticket.getSeatNum() == seat.getSeatNum() && ticket.getFlightNumber().equals(flightNum)) {
                    seat.setAvailable(false);
                }
            }
        }
        SeatChart seatChart = new SeatChart(currentEmail, seats, aircraftController, flightNum, system, cardPanel, cardLayout, e -> showMainScreen());

        cardPanel.add(seatChart, "seatChart");
        cardLayout.show(cardPanel, "seatChart");
    }

    private void continueAsGuest() {
        // Show the user page for the guest
        currentEmail = "";
        showUserPage(currentEmail);
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
        currentEmail = "";
        selectedOriginName = null;
        selectedDestinationName = null;
    }

    private void handleLogin() {
        String email = loginPanel.getEmail();
        char[] password = loginPanel.getPassword();
    
        if (email.isEmpty() || password.length == 0) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        if (authController.loginCrewMember(email, new String(password))) {
            currentEmail = email;
            showCrewMemberPage(email);
        } else if (authController.loginUser(email, new String(password))) {
            // Login successful for a regular user
            currentEmail = email;
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

        ArrayList<String> locationStrings = system.getLocationStrings();

        // Create UserPagePanel and add it to the cardPanel
        userPagePanel = new UserPagePanel(
            username.isEmpty() ? "" : system.getNameByEmail(username),
            e -> showMyFlights(username),
            e -> {
                selectedOriginName = userPagePanel.getSelectedOrigin();
                selectedDestinationName = userPagePanel.getSelectedDestination();
                if (selectedOriginName != null && selectedDestinationName != null) {
                    JPanel flightsPanel = createFlightsPanel();
                    cardPanel.add(flightsPanel, "flightsPanel");
                    cardLayout.show(cardPanel, "flightsPanel");
                } else {
                    JOptionPane.showMessageDialog(this, "Please select both an origin and a destination.");
                }
            },
            e -> showMainScreen(),
            locationStrings
        );
        cardPanel.add(userPagePanel, "user");

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
        adminPage.setBackground(Color.WHITE);
    
        JLabel welcomeLabel = new JLabel("Welcome, Admin!");
        welcomeLabel.setFont(new Font(welcomeLabel.getFont().getName(), Font.BOLD, 18));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        adminPage.add(Box.createVerticalStrut(20));
        adminPage.add(welcomeLabel);
        adminPage.add(Box.createVerticalStrut(20));

        ImageIcon imageIcon = new ImageIcon(getClass().getResource("admin_icon.png"));
        imageIcon.setImage(imageIcon.getImage().getScaledInstance(300, 300, Image.SCALE_DEFAULT));
        JLabel image = new JLabel(imageIcon);
        image.setAlignmentX(Component.CENTER_ALIGNMENT);
        adminPage.add(image);
        adminPage.add(Box.createVerticalStrut(20));
    
        viewAllFlightsButton = new JButton("Manage Flights");
        viewAllFlightsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewAllFlightsButton.addActionListener(e -> showAllFlights());
        styleButton(viewAllFlightsButton);
        adminPage.add(viewAllFlightsButton);
        adminPage.add(Box.createVerticalStrut(30));
    
        JButton manageAircraftButton = new JButton("Manage Aircraft");
        manageAircraftButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        manageAircraftButton.addActionListener(e -> showManageAircraftPage());
        styleButton(manageAircraftButton);
        adminPage.add(manageAircraftButton);
        adminPage.add(Box.createVerticalStrut(60));
    
        JButton backButton = new JButton("Back");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> showMainScreen());
        styleButton(backButton);
        adminPage.add(backButton);
        adminPage.add(Box.createVerticalStrut(80));
    
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
        manageAircraftPage.setBackground(Color.WHITE);
    
        JLabel titleLabel = new JLabel("Manage Aircraft");
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 18));
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

        // Add MouseListener to the aircraft list
        aircraftList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Detect double-click
                    int selectedIndex = aircraftList.getSelectedIndex();
                    if (selectedIndex != -1) {
                        showAircraftOptionsPopup(allAircrafts.get(selectedIndex));
                    }
                }
            }
        });

        
    
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
        styleButton(addAircraftButton);
        manageAircraftPage.add(addAircraftButton);
        manageAircraftPage.add(Box.createVerticalStrut(20));
    
        JButton backButton = new JButton("Back");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> showAdminPage());
        styleButton(backButton);
        manageAircraftPage.add(backButton);
        manageAircraftPage.add(Box.createVerticalStrut(20));
    
        return manageAircraftPage;
    }

    private void showAircraftOptionsPopup(Aircraft selectedAircraft) {
        String[] options = {"Modify Aircraft", "Delete Aircraft"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Choose an action for Aircraft ID " + selectedAircraft.getId(),
                "Aircraft Options",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );
    
        if (choice == 0) {
            // Modify Aircraft
            showModifyAircraftPage(selectedAircraft);
        } else if (choice == 1) {
            // Delete Aircraft
            handleDeleteAircraft(selectedAircraft);
        }
    }

    private void handleDeleteAircraft(Aircraft aircraft) {
        // Implement this method to delete the selected aircraft from the system
        boolean deleteSuccess = system.deleteAircraft(aircraft);
    
        if (deleteSuccess) {
            // Aircraft deleted successfully
            JOptionPane.showMessageDialog(this, "Aircraft deleted successfully.");
            // Refresh the manage aircraft page to reflect the changes
            showManageAircraftPage();
        } else {
            // Aircraft deletion failed
            JOptionPane.showMessageDialog(this, "Failed to delete the aircraft.");
        }
    }

    private void showModifyAircraftPage(Aircraft aircraft) {
        // Implement this method to create and show the modify aircraft page
        // You can use a similar approach as in createAddAircraftPage method
        // to create a panel with the current aircraft information and allow modifications.
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
        addAircraftPage.setBackground(Color.WHITE);
    
        JLabel titleLabel = new JLabel("Add New Aircraft");
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 18));
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
        addFlightPage.setBackground(Color.WHITE);
    
        JLabel titleLabel = new JLabel("Add New Flight");
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        addFlightPage.add(Box.createVerticalStrut(20));
        addFlightPage.add(titleLabel);
        addFlightPage.add(Box.createVerticalStrut(20));
    
        // Add Origin
        JPanel originPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        originPanel.setBackground(Color.WHITE);
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
        destinationPanel.setBackground(Color.WHITE);
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
        flightNumberPanel.setBackground(Color.WHITE);
        flightNumberPanel.add(new JLabel("Flight Number: "));
        JTextField flightNumField = new JTextField(15);
        flightNumberPanel.add(flightNumField);
        addFlightPage.add(flightNumberPanel);
        addFlightPage.add(Box.createVerticalStrut(10));
    
        // Add Flight Date
        JPanel flightDatePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        flightDatePanel.setBackground(Color.WHITE);
        flightDatePanel.add(new JLabel("Flight Date (YYYY-MM-DD): "));
        JTextField dateField = new JTextField(15);
        flightDatePanel.add(dateField);
        addFlightPage.add(flightDatePanel);
        addFlightPage.add(Box.createVerticalStrut(10));
    
        // Add Departure Time
        JPanel departureTimePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        departureTimePanel.setBackground(Color.WHITE);
        departureTimePanel.add(new JLabel("Departure Time (HH:mm): "));
        JTextField departureTimeField = new JTextField(15);
        departureTimePanel.add(departureTimeField);
        addFlightPage.add(departureTimePanel);
        addFlightPage.add(Box.createVerticalStrut(10));
    
        // Add Arrival Time
        JPanel arrivalTimePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        arrivalTimePanel.setBackground(Color.WHITE);
        arrivalTimePanel.add(new JLabel("Arrival Time (HH:mm): "));
        JTextField arrivalTimeField = new JTextField(15);
        arrivalTimePanel.add(arrivalTimeField);
        addFlightPage.add(arrivalTimePanel);
        addFlightPage.add(Box.createVerticalStrut(10));
    
        // Add Flight Time
        JPanel flightTimePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        flightTimePanel.setBackground(Color.WHITE);
        flightTimePanel.add(new JLabel("Flight Time (HH:mm): "));
        JTextField flightTimeField = new JTextField(15);
        flightTimePanel.add(flightTimeField);
        addFlightPage.add(flightTimePanel);
        addFlightPage.add(Box.createVerticalStrut(20));
    
        JButton addButton = new JButton("Add Flight");
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        styleButton(addButton);
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
        styleButton(backButton);
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

        allFlightsList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                Color evenColor = new Color(230, 240, 255);
                Color oddColor = Color.WHITE;

                if (isSelected) {
                    c.setBackground(new Color(0, 102, 204));
                } else {
                    c.setBackground(index % 2 == 0 ? evenColor : oddColor);
                }

                return c;
            }
        });
    
        // Add ListSelectionListener to the flight list
        allFlightsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Double-click
                    int selectedIndex = allFlightsList.getSelectedIndex();
                    if (selectedIndex != -1) {
                        showFlightOptionsDialog(allFlights.get(selectedIndex));
                        allFlightsList.clearSelection(); // Deselect the item after handling
                    }
                }
            }
        });

        JLabel titleLabel = new JLabel("All Flights");
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    
        JScrollPane allFlightsScrollPane = new JScrollPane(allFlightsList);

        allFlightsScrollPane.setPreferredSize(new Dimension(600, 400));
    
        TitledBorder titledBorder = BorderFactory.createTitledBorder("All Flights");
        allFlightsScrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10), titledBorder));
    
        JButton backButton = new JButton("Back");
        styleButton(backButton);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "user"));
    
        // Add the "Add Flight" button
        JButton addFlightButton = new JButton("Add Flight");
        styleButton(addFlightButton);
        addFlightButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addFlightButton.addActionListener(e -> showAddFlightPage());
    
        JPanel allFlightsPanel = new JPanel();
        allFlightsPanel.setBackground(Color.WHITE);
        allFlightsPanel.setLayout(new BoxLayout(allFlightsPanel, BoxLayout.Y_AXIS));
        allFlightsPanel.add(Box.createVerticalStrut(20));
        allFlightsPanel.add(titleLabel);
        allFlightsPanel.add(Box.createVerticalStrut(20));
        allFlightsPanel.add(allFlightsScrollPane);
        allFlightsPanel.add(Box.createVerticalStrut(20));
        allFlightsPanel.add(addFlightButton);
        allFlightsPanel.add(Box.createVerticalStrut(40));
        allFlightsPanel.add(backButton);
        allFlightsPanel.add(Box.createVerticalStrut(80));
    
        cardPanel.add(allFlightsPanel, "allFlightsPanel");
        cardLayout.show(cardPanel, "allFlightsPanel");
    }
    
    
    private void showFlightOptionsDialog(Flight selectedFlight) {
        String[] options = {"Cancel Flight", "Modify Flight", "Back"};
    
        int choice = JOptionPane.showOptionDialog(
                this,
                "What do you want to do with the flight?",
                "Flight Options",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[2]);
    
        switch (choice) {
            case JOptionPane.YES_OPTION:
                // User chose to cancel the flight
                handleCancelAdminFlight(selectedFlight);
                break;
    
            case JOptionPane.NO_OPTION:
                // User chose to modify the flight
                handleModifyAdminFlight(selectedFlight);
                break;
    
            default:
                // User chose to go back or closed the dialog
                // Do nothing or add any additional logic as needed
                break;
        }
    }
    
    private void handleCancelAdminFlight(Flight selectedFlight) {
        boolean cancellationSuccess = system.cancelFlight(selectedFlight);
    
        if (cancellationSuccess) {
            // Cancellation successful
            JOptionPane.showMessageDialog(this, "Flight canceled successfully.");
    
            // Recreate the list of flights and update the UI
            showAllFlights();
        } else {
            // Cancellation failed
            JOptionPane.showMessageDialog(this, "Failed to cancel the flight.");
        }
    }

    private void handleModifyAdminFlight(Flight selectedFlight) {
        // Create a new panel for flight modification
        JPanel modifyPanel = new JPanel();
        modifyPanel.setLayout(new GridLayout(5, 2, 10, 10));
    
        // Labels and text fields for modification options
        JLabel departureLabel = new JLabel("Departure Time:");
        JTextField departureField = new JTextField(selectedFlight.getDepartureTime());
    
        JLabel arrivalLabel = new JLabel("Arrival Time:");
        JTextField arrivalField = new JTextField(selectedFlight.getArrivalTime());
    
        JLabel dateLabel = new JLabel("Date (yyyy-MM-dd):");
        JTextField dateField = new JTextField(selectedFlight.getFlightDate());
    
        JLabel flightTimeLabel = new JLabel("Flight Time (hh:mm):");
        JTextField flightTimeField = new JTextField(selectedFlight.getFlightTime());
    
        // Add labels and text fields to the panel
        modifyPanel.add(departureLabel);
        modifyPanel.add(departureField);
        modifyPanel.add(arrivalLabel);
        modifyPanel.add(arrivalField);
        modifyPanel.add(dateLabel);
        modifyPanel.add(dateField);
        modifyPanel.add(flightTimeLabel);
        modifyPanel.add(flightTimeField);
    
        // Create a button for flight modification
        JButton modifyButton = new JButton("Modify Flight");
        styleButton(modifyButton); // Assuming you have a styleButton method
    
        // Add action listener to the modify button
        modifyButton.addActionListener(e -> {
            // Get the modified values from text fields
            String newDepartureTime = departureField.getText();
            String newArrivalTime = arrivalField.getText();
            String newDate = dateField.getText();
            String newFlightTime = flightTimeField.getText();
    
            // Perform flight modification (update system, database, etc.)
            Flight modifiedFlight = new Flight(
                    selectedFlight.getOrigin(),
                    selectedFlight.getDestination(),
                    selectedFlight.getFlightNum(),
                    newDate,
                    newDepartureTime,
                    newArrivalTime,
                    newFlightTime,
                    selectedFlight.getAircraft(),
                    selectedFlight.getCrewMembers(),
                    selectedFlight.getPassengers());
    
            boolean modificationSuccess = system.modifyFlight(modifiedFlight);
    
            if (modificationSuccess) {
                // Modification successful
                JOptionPane.showMessageDialog(this, "Flight modified successfully.");
    
                // Close the modification panel
                ((Window) SwingUtilities.getRoot(modifyPanel)).dispose();
    
                // Update the list of flights and refresh the UI
                showAllFlights();
            } else {
                // Modification failed
                JOptionPane.showMessageDialog(this, "Failed to modify the flight. Please check the input.");
            }
        });
    
        // Add the modify button to the panel
        modifyPanel.add(modifyButton);
    
        // Show the modification panel in a dialog
        JOptionPane.showOptionDialog(
                this,
                modifyPanel,
                "Modify Flight",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new Object[]{},
                null
        );
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
