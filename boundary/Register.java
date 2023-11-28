package boundary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Register extends JPanel {
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;

    //private JTextField addressField;
    private JTextField houseNumField;
    private JTextField streetNameField;
    private JTextField cityField;
    private JTextField countryField;
    private JTextField postalCodeField;

    private JPasswordField passwordField;
    private JButton registerButton;
    private JButton backButton;

    public Register(ActionListener registerListener, ActionListener backListener) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initializeComponents(registerListener, backListener);
    }

    private void initializeComponents(ActionListener registerListener, ActionListener backListener) {
        JLabel titleLabel = new JLabel("Welcome! Create Your Account Below");
        add(titleLabel);
        // Name
        firstNameField = new JTextField(15);
        addComponent(new JLabel("First Name:"), firstNameField);
        firstNameField.setMaximumSize(new Dimension(200, 25));

        lastNameField = new JTextField(15);
        addComponent(new JLabel("Last Name:"), lastNameField);
        lastNameField.setMaximumSize(new Dimension(200, 25));

        // Email
        emailField = new JTextField(15);
        addComponent(new JLabel("Email:"), emailField);
        emailField.setMaximumSize(new Dimension(200, 25));

        // Address
        houseNumField = new JTextField(15);
        addComponent(new JLabel("House Number:"), houseNumField);
        houseNumField.setMaximumSize(new Dimension(200, 25));

        streetNameField = new JTextField(15);
        addComponent(new JLabel("Street Name:"), streetNameField);
        streetNameField.setMaximumSize(new Dimension(200, 25));

        cityField = new JTextField(15);
        addComponent(new JLabel("City:"), cityField);
        cityField.setMaximumSize(new Dimension(200, 25));

        countryField = new JTextField(15);
        addComponent(new JLabel("Country:"), countryField);
        countryField.setMaximumSize(new Dimension(200, 25));

        postalCodeField = new JTextField(15);
        addComponent(new JLabel("Postal Code:"), postalCodeField);
        postalCodeField.setMaximumSize(new Dimension(200, 25));

        // Password
        passwordField = new JPasswordField(15);
        addComponent(new JLabel("Password:"), passwordField);
        passwordField.setMaximumSize(new Dimension(200, 25));

        // Show/Hide password checkbox
        JCheckBox showPasswordCheckBox = new JCheckBox("Show Password");
        showPasswordCheckBox.addActionListener(e -> 
            passwordField.setEchoChar(showPasswordCheckBox.isSelected() ? '\u0000' : '*'));
        add(showPasswordCheckBox);

        // Buttons
        registerButton = new JButton("Register");
        registerButton.addActionListener(registerListener);
        backButton = new JButton("Back");
        backButton.addActionListener(backListener);
        addButtons(registerButton, backButton);
    }

    private void addComponent(JLabel label, JComponent field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(Box.createHorizontalGlue());
        panel.add(field);
        panel.add(Box.createHorizontalGlue());
        add(Box.createVerticalStrut(10));
        add(label);
        add(panel);
    }

    private void addButtons(JButton... buttons) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        for (JButton button : buttons) {
            buttonPanel.add(button);
        }
        add(Box.createVerticalStrut(10));
        add(buttonPanel);
    }

    // Getters
    public String getFirstName() {
        return firstNameField.getText();
    }

    public String getLastName() {
        return lastNameField.getText();
    }

    public String getEmail() {
        return emailField.getText();
    }

    public String getHouseNum() {
        return houseNumField.getText();
    }
    
    public String getStreetName() {
        return streetNameField.getText();
    }
    
    public String getCity() {
        return cityField.getText();
    }
    
    public String getCountry() {
        return countryField.getText();
    }
    
    public String getPostalCode() {
        return postalCodeField.getText();
    }

    public char[] getPassword() {
        return passwordField.getPassword();
    }
}
