package boundary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Register extends JPanel {
    private JTextField nameField;
    private JTextField emailField;
    private JTextField addressField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JButton backButton;

    public Register(ActionListener registerListener, ActionListener backListener) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initializeComponents(registerListener, backListener);
    }

    private void initializeComponents(ActionListener registerListener, ActionListener backListener) {
        // Name
        nameField = new JTextField(15);
        addComponent(new JLabel("Name:"), nameField);
        nameField.setMaximumSize(new Dimension(200, 25));

        // Email
        emailField = new JTextField(15);
        addComponent(new JLabel("Email:"), emailField);
        emailField.setMaximumSize(new Dimension(200, 25));

        // Address
        addressField = new JTextField(15);
        addComponent(new JLabel("Address:"), addressField);
        addressField.setMaximumSize(new Dimension(200, 25));

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
    public String getName() {
        return nameField.getText();
    }

    public String getEmail() {
        return emailField.getText();
    }

    public String getAddress() {
        return addressField.getText();
    }

    public char[] getPassword() {
        return passwordField.getPassword();
    }
}
