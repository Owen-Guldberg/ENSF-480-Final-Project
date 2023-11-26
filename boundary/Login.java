package boundary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Login extends JPanel {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton backButton;

    public Login(ActionListener loginAction, ActionListener backAction) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initializeComponents(loginAction, backAction);
    }

    private void initializeComponents(ActionListener loginAction, ActionListener backAction) {
        JLabel usernameLabel = new JLabel("Email:");
        emailField = new JTextField(15);
        emailField.setMaximumSize(new Dimension(200, 25));

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);
        passwordField.setMaximumSize(new Dimension(200, 25));

        JCheckBox showPasswordCheckBox = new JCheckBox("Show Password");
        showPasswordCheckBox.addActionListener(e -> passwordField.setEchoChar(showPasswordCheckBox.isSelected() ? '\u0000' : '*'));

        loginButton = new JButton("Login");
        loginButton.addActionListener(loginAction);

        backButton = new JButton("Back");
        backButton.addActionListener(backAction);

        addComponent(usernameLabel, emailField);
        addComponent(passwordLabel, passwordField);
        add(showPasswordCheckBox);
        addButtons(loginButton, backButton);
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

    public String getEmail() {
        return emailField.getText();
    }

    public char[] getPassword() {
        return passwordField.getPassword();
    }
}
