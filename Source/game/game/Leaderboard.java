package game;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

@SuppressWarnings("unused")
public class Leaderboard extends JFrame {
    private JTable table; // JTable to display leaderboard data

    // Constructor for the Leaderboard class
    public Leaderboard() {
        setTitle("Leaderboard");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create table model and define columns
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Username");
        model.addColumn("Score");
        model.addColumn("Timestamp");

        // Fetch data from database
        fetchLeaderboardData(model);

        // Create table and add to scroll pane
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    // Method to fetch leaderboard data from the database
    private void fetchLeaderboardData(DefaultTableModel model) {
        model.setRowCount(0); // Clear existing rows before fetching new data
    
        String sql = "SELECT id, username, score, timestamp FROM scores ORDER BY score DESC";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
    
            while (rs.next()) {
                // Using correct column indices based on your specification
                model.addRow(new Object[]{
                    rs.getInt(1),            // id is at index 1
                    rs.getString(2),         // username is at index 2
                    rs.getInt(3),            // score is at index 3
                    rs.getTimestamp(4)       // timestamp is at index 4
                });
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Print stack trace for any SQL errors
        }
    }

    // Method to save score to the database (Make sure to call this when the game ends)
    public void saveScoreToDatabase(String username, int score) {
        try (Connection conn = DBconnection.getConnection()) {
            String sql = "SELECT score FROM scores WHERE username = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    int existingScore = rs.getInt("score");
                    if (score > existingScore) {
                        String updateSql = "UPDATE scores SET score = ?, timestamp = ? WHERE username = ?";
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                            updateStmt.setInt(1, score);
                            updateStmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                            updateStmt.setString(3, username);
                            updateStmt.executeUpdate();
                        }
                    }
                } else {
                    String insertSql = "INSERT INTO scores (username, score, timestamp) VALUES (?, ?, ?)";
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                        insertStmt.setString(1, username);
                        insertStmt.setInt(2, score);
                        insertStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                        insertStmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Main method to launch the Leaderboard GUI
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Leaderboard::new);
    }
}
