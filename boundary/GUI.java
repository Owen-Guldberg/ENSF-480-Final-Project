package boundary;

import controller.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.sql.*;
import java.text.*;
import java.io.*;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;

public class GUI extends JFrame implements ActionListener {

    private JLabel welcome;
    private JLabel instructions;
    private JButton registerButton;
    private JButton loginButton;
    private JButton guestLink;

    public GUI() {
        setTitle("Skyward Bound Flight Reservation System");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Container pane = getContentPane();
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

        welcome = new JLabel("Welcome to Skyward Bound!");
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);
        instructions = new JLabel("Please register or login to continue.");
        instructions.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Panel for buttons and guest link
        JPanel buttonsAndLinkPanel = new JPanel();
        buttonsAndLinkPanel.setLayout(new BoxLayout(buttonsAndLinkPanel, BoxLayout.Y_AXIS));

        // Buttons Panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());

        // Register Button
        registerButton = new JButton("Register");
        registerButton.addActionListener(this);
        buttonsPanel.add(registerButton);

        // Login Button
        loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        buttonsPanel.add(loginButton);
        buttonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Continue as Guest Link
        guestLink = new JButton("Continue as Guest");
        guestLink.setBorderPainted(false);
        guestLink.setContentAreaFilled(false);
        guestLink.setFocusPainted(false);
        guestLink.setOpaque(false);
        guestLink.setForeground(Color.BLUE);
        guestLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Add underline and action listener
        guestLink.setFont(new Font(guestLink.getFont().getName(), Font.PLAIN, guestLink.getFont().getSize()));
        guestLink.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                continueAsGuest();
            }
        });
        guestLink.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add buttons and link to nested panel
        buttonsAndLinkPanel.add(buttonsPanel);
        buttonsAndLinkPanel.add(guestLink);

        // Add components to main pane
        pane.add(welcome);
        pane.add(instructions);
        pane.add(buttonsAndLinkPanel);
        
        setVisible(true);
    }
    
    private void continueAsGuest() {
        // Logic to continue as a guest
        JOptionPane.showMessageDialog(this, "Continuing as guest...");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerButton) {
            // Logic for registering
            JOptionPane.showMessageDialog(this, "Register functionality not implemented yet.");
        } else if (e.getSource() == loginButton) {
            // Logic for logging in
            JOptionPane.showMessageDialog(this, "Login functionality not implemented yet.");
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