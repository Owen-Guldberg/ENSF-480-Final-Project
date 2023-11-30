package boundary;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Login extends JPanel {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton backButton;
    private JButton registerButton;

    public Login(ActionListener loginAction, ActionListener backAction, ActionListener registerAction) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        initializeComponents(loginAction, backAction, registerAction);
    }

    // private void initializeComponents(ActionListener loginAction, ActionListener backAction, ActionListener registerAction) {
    //     JLabel titleLabel = new JLabel("Please Login Below");
    //     titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 18));
    //     titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
    //     JLabel emailLabel = new JLabel("Email:");
    //     emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    //     emailField = new JTextField(15);
    //     styleTextField(emailField);

    //     JLabel passwordLabel = new JLabel("Password:");
    //     passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    //     passwordField = new JPasswordField(15);
    //     styleTextField(passwordField);

    //     JCheckBox showPasswordCheckBox = new JCheckBox("Show Password");
    //     showPasswordCheckBox.setAlignmentX(Component.CENTER_ALIGNMENT);
    //     showPasswordCheckBox.addActionListener(e -> passwordField.setEchoChar(showPasswordCheckBox.isSelected() ? '\u0000' : '*'));

    //     loginButton = new JButton("Login");
    //     styleButton(loginButton);
    //     loginButton.addActionListener(loginAction);

    //     backButton = new JButton("Back");
    //     styleButton(backButton);
    //     backButton.addActionListener(backAction);

    //     registerButton = new JButton("Don't have an account? Register");
    //     registerButton.setForeground(new Color(0, 102, 204)); // Blue text color
    //     registerButton.setBorderPainted(false);
    //     registerButton.setContentAreaFilled(false);
    //     registerButton.setFocusPainted(false);
    //     registerButton.setOpaque(false);
    //     registerButton.addActionListener(registerAction);
    //     registerButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    //     registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);

    //     add(Box.createVerticalStrut(20));
    //     add(titleLabel);
    //     add(Box.createVerticalStrut(10));
    //     addComponent(emailLabel, emailField);
    //     add(Box.createVerticalStrut(5));
    //     addComponent(passwordLabel, passwordField);
    //     add(Box.createVerticalStrut(5));
    //     add(showPasswordCheckBox);
    //     add(Box.createVerticalStrut(10));
    //     addButtons(loginButton, backButton);
    //     add(Box.createVerticalStrut(10));
    //     add(registerButton);
    // }

    private void initializeComponents(ActionListener loginAction, ActionListener backAction, ActionListener registerAction) {
        add(Box.createVerticalStrut(20));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
    
        // Common GridBagConstraints settings
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 50, 10, 50);
        gbc.weightx = 1;
        gbc.gridx = 0;
    
        // Title label
        JLabel titleLabel = new JLabel("Please Log In Below");
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);
    
        // Email field and label
        emailField = new JTextField(15);
        JPanel emailPanel = createInputPanel("Email:", emailField);
        gbc.anchor = GridBagConstraints.WEST; // Align to the left
        add(emailPanel, gbc);
    
        // Password field and label
        passwordField = new JPasswordField(15);
        JPanel passwordPanel = createInputPanel("Password:", passwordField);
        add(passwordPanel, gbc);
    
        // Show Password checkbox
        JCheckBox showPasswordCheckBox = new JCheckBox("Show Password");
        showPasswordCheckBox.addActionListener(e -> passwordField.setEchoChar(showPasswordCheckBox.isSelected() ? '\u0000' : '*'));
        gbc.anchor = GridBagConstraints.CENTER; // Align to the center
        add(showPasswordCheckBox, gbc);
    
        // Login button
        loginButton = new JButton("Log In");
        styleButton(loginButton);
        loginButton.addActionListener(loginAction);
        add(loginButton, gbc);
    
        // Back button
        backButton = new JButton("Back");
        styleButton(backButton);
        backButton.addActionListener(backAction);
        add(backButton, gbc);
    
        // Register button
        registerButton = new JButton("Don't have an account? Register");
        styleLinkButton(registerButton);
        registerButton.addActionListener(registerAction);
        add(registerButton, gbc);
    
        // Filler component to push everything up
        gbc.weighty = 1;
        add(Box.createGlue(), gbc);
    }
    
    private JPanel createInputPanel(String labelText, JComponent field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
    
        JLabel label = new JLabel(labelText);
        label.setAlignmentX(Component.LEFT_ALIGNMENT); // Align label to the left
        panel.add(label);
    
        //field.setMaximumSize(new Dimension(900, 25));
        field.setMinimumSize(new Dimension(900, 25));
        field.setAlignmentX(Component.LEFT_ALIGNMENT); // Align field to the left
        panel.add(field);
    
        return panel;
    }

    // private void addComponent(JLabel label, JComponent field) {
    //     JPanel panel = new JPanel();
    //     panel.setBackground(Color.WHITE);
    //     panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
    //     panel.add(Box.createHorizontalGlue());
    //     panel.add(field);
    //     panel.add(Box.createHorizontalGlue());
    //     add(Box.createVerticalStrut(10));
    //     add(label);
    //     add(panel);
    // }

    private void addComponent(JLabel label, JComponent field) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
    
        // Align label to the left
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(label, gbc);
    
        // Align field below the label
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(field, gbc);
    
        add(panel);
    }

    private void addButtons(JButton... buttons) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        for (JButton button : buttons) {
            buttonPanel.add(button);
        }
        add(Box.createVerticalStrut(10));
        add(buttonPanel);
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(0, 102, 204)); // Blue background
        button.setForeground(Color.WHITE); // White text
        button.setFocusPainted(false);
        button.setFont(new Font(button.getFont().getName(), Font.BOLD, 16));
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void styleTextField(JTextField textField) {
        textField.setMaximumSize(new Dimension(200, 25));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5))
        );
    }

    private void styleLinkButton(JButton button) {
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setForeground(new Color(0, 102, 204)); // Blue color
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFont(new Font(button.getFont().getName(), Font.PLAIN, 16));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        button.setText("<html><span style='text-decoration: none;'>" + button.getText() + "</span></html>");

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setText("<html><span style='text-decoration: underline;'>" + button.getText().replaceAll("<[^>]*>", "") + "</span></html>");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setText("<html><span style='text-decoration: none;'>" + button.getText().replaceAll("<[^>]*>", "") + "</span></html>");
            }
        });
    }

    public String getEmail() {
        return emailField.getText();
    }

    public char[] getPassword() {
        return passwordField.getPassword();
    }
}
