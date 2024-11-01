package game;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
@SuppressWarnings("unused")

public class Login extends JFrame {
    private JTextField usernameField;
    private JButton loginButton;
    private JButton registerButton;

    public Login() {
        setTitle("Brick Breaker - Login");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Username label and text field
        JLabel usernameLabel = new JLabel("Enter Username:");
        usernameField = new JTextField(20);

        // Login button
        loginButton = new JButton("Login");
        loginButton.addActionListener(e -> openLoginFrame());

        // Register button
        registerButton = new JButton("Register");
        registerButton.addActionListener(e -> openRegisterFrame());

        // Setting up the layout
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(loginButton);
        panel.add(registerButton);

        add(panel, BorderLayout.CENTER);
    }

    // Open the registration frame
    private void openRegisterFrame() {
        JFrame registerFrame = new JFrame("Register User");
        registerFrame.setSize(300, 150);
        registerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        registerFrame.setLocationRelativeTo(null);

        JTextField registerField = new JTextField(20);
        JButton registerSubmitButton = new JButton("Register");

        registerSubmitButton.addActionListener(e -> {
            String username = registerField.getText().trim();
            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(registerFrame, "Username cannot be empty.");
                return;
            }

            // Check if user already exists before registering
            if (isUserRegistered(username)) {
                JOptionPane.showMessageDialog(registerFrame, "User is already registered. Please login.");
                return;
            }

            // Attempt to register the user
            if (registerUser(username)) {
                JOptionPane.showMessageDialog(registerFrame, "User registered successfully!");
                registerFrame.dispose(); // Close registration frame
            } else {
                JOptionPane.showMessageDialog(registerFrame, "Error: Username could not be registered.");
            }
        });

        // Set up the registration panel layout
        JPanel registerPanel = new JPanel(new FlowLayout());
        registerPanel.add(new JLabel("Enter Username:"));
        registerPanel.add(registerField);
        registerPanel.add(registerSubmitButton);

        registerFrame.add(registerPanel);
        registerFrame.setVisible(true);
    }

    // Method to handle user login
    private void openLoginFrame() {
        String username = usernameField.getText().trim();
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username cannot be empty.");
            return;
        }
    
        // Check if user exists in the database
        if (isUserRegistered(username)) {
            JOptionPane.showMessageDialog(this, "Welcome, " + username + "!");

            // Create a new JFrame for gameplay
            JFrame gameFrame = new JFrame("Brick Breaker");
            gameFrame.setBounds(10, 10, 700, 600);
            gameFrame.setResizable(false);
            gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            // Add the Gameplay panel to the frame
            Gameplay gameplay = new Gameplay(username);
            gameFrame.add(gameplay);
            
            // Make game frame visible
            gameFrame.setVisible(true); 
            dispose(); // Close the login window
        } else {
            JOptionPane.showMessageDialog(this, "Error: Username does not exist. Please register.");
        }
    }
    
    // Check if the user is registered
    private boolean isUserRegistered(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Returns true if user exists
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Error during database access
        }
    }

    // Method to register a new user
    private boolean registerUser(String username) {
        String sql = "INSERT IGNORE INTO users (username) VALUES (?)";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // User registration is successful if at least one row is affected
        } catch (SQLException e) {
            e.printStackTrace(); // Print stack trace for any SQL errors
            return false; // Registration failed
        }
    }

    // Main method to start the Login GUI
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }
}
