package boundary;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class MainPanel extends JPanel {
    private JButton registerButton;
    private JButton loginButton;
    private JButton guestLink;

    public MainPanel(ActionListener registerListener, ActionListener loginListener, ActionListener guestListener) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initializeComponents(registerListener, loginListener, guestListener);
    }

    private void initializeComponents(ActionListener registerListener, ActionListener loginListener, ActionListener guestListener) {
        JLabel welcome = new JLabel("Welcome to Skyward Bound!");
        welcome.setFont(new Font(welcome.getFont().getName(), Font.PLAIN, 16));
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);

        ImageIcon imageIcon = new ImageIcon(getClass().getResource("airline.png")); // Load the image
        JLabel label = new JLabel(imageIcon); // Set the icon to JLabel
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel instructions = new JLabel("Please register or login to continue.");
        instructions.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        registerButton = new JButton("Register");
        registerButton.addActionListener(registerListener);
        loginButton = new JButton("Login");
        loginButton.addActionListener(loginListener);

        // Continue as Guest Link
        guestLink = new JButton("Continue as Guest");
        guestLink.setBorderPainted(false);
        guestLink.setContentAreaFilled(false);
        guestLink.setFocusPainted(false);
        guestLink.setOpaque(false);
        guestLink.setForeground(Color.BLUE);
        guestLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        guestLink.setFont(new Font(guestLink.getFont().getName(), Font.PLAIN, guestLink.getFont().getSize()));
        guestLink.addActionListener(guestListener);
        guestLink.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonsPanel.add(registerButton);
        buttonsPanel.add(loginButton);

        add(welcome);
        add(label);
        add(instructions);
        add(buttonsPanel);
        add(guestLink);
    }

    public JButton getRegisterButton() {
        return registerButton;
    }

    public JButton getLoginButton() {
        return loginButton;
    }

    public JButton getGuestLink() {
        return guestLink;
    }
}
