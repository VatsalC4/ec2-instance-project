package game;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;

public class Gameplay extends JPanel implements ActionListener {
    private boolean play = false; // Game state
    private int score; // Player score
    private int delay = 8; // Timer delay
    private int playerX = 310; // Player paddle position
    private int ballposX; // Ball position X
    private int ballposY; // Ball position Y
    private int ballXdir = -1; // Ball direction X
    private int ballYdir = -2; // Ball direction Y
    private MapGenerator map; // Game map
    private String username; // Player username

    // Sound file paths
    private static final String BRICK_HIT_SOUND = "C:\\Users\\vatsa\\OneDrive\\Desktop\\Java-Mini-Project\\Source\\game\\game\\sounds\\brick-hitting-sound.wav";
    private static final String BACKGROUND_MUSIC = "C:\\Users\\vatsa\\OneDrive\\Desktop\\Java-Mini-Project\\Source\\game\\game\\sounds\\background-music.wav";
    private Clip backgroundMusicClip; // Clip for background music

    // Constructor for Gameplay class
    public Gameplay(String username) {
        this.username = username;
        map = new MapGenerator(4, 12); // Initialize the map
        setFocusable(true);
        addKeyBindings(); // Add key bindings for gameplay
        play = true; // Start the game immediately
        ballposX = (692 - 20) / 2; // Center the ball
        ballposY = 350; // Set initial Y position
        playBackgroundMusic(); // Start background music

        Timer timer = new Timer(delay, this); // Timer setup
        timer.start();
    }

    // Method to add key bindings for player controls
    private void addKeyBindings() {
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");
        getActionMap().put("moveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (playerX > 0) {
                    playerX -= 20; // Move paddle left
                }
            }
        });

        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");
        getActionMap().put("moveRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (playerX < getWidth() - 100) { // Subtracting paddle width
                    playerX += 20; // Move paddle right
                }
            }
        });

        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "restartGame");
        getActionMap().put("restartGame", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!play) {
                    restartGame(); // Restart the game
                }
            }
        });

        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("L"), "showLeaderboard");
        getActionMap().put("showLeaderboard", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!play) {
                    new Leaderboard(); // Show the leaderboard
                }
            }
        });
    }

    // Method to paint the gameplay components
    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(1, 1, 692, 592);

        map.draw((Graphics2D) g); // Draw map

        g.setColor(Color.GREEN); // Draw paddle
        g.fillRect(playerX, 550, 100, 8);

        g.setColor(Color.RED); // Draw ball
        g.fillOval(ballposX, ballposY, 20, 20);

        g.setColor(Color.WHITE); // Draw score
        g.setFont(new Font("serif", Font.BOLD, 20));
        g.drawString("Score: " + score, 590, 30);

        // Show game over message
        if (!play) {
            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 30));
            String gameOverMessage = "Game Over";
            String scoreMessage = "Score: " + score;
            String restartMessage = "Press (Enter) to Restart";
            String leaderboardMessage = "Press (L) for Leaderboard";

            FontMetrics metrics = g.getFontMetrics(g.getFont());
            int gameOverX = (692 - metrics.stringWidth(gameOverMessage)) / 2;
            int scoreX = (692 - metrics.stringWidth(scoreMessage)) / 2;
            int restartX = (692 - metrics.stringWidth(restartMessage)) / 2;
            int leaderboardX = (692 - metrics.stringWidth(leaderboardMessage)) / 2;

            g.drawString(gameOverMessage, gameOverX, 300);
            g.drawString(scoreMessage, scoreX, 350);
            g.drawString(restartMessage, restartX, 400);
            g.drawString(leaderboardMessage, leaderboardX, 450);
        }
        g.dispose();
    }

    // Method to play sound effect
    private void playSound(String soundFilePath) {
        try {
            File soundFile = new File(soundFilePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            
            // Set maximum volume
            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(volumeControl.getMaximum());
            
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // Method to play background music
    private void playBackgroundMusic() {
        System.out.println("Playing background music...");
        new Thread(() -> {
            try {
                File musicFile = new File(BACKGROUND_MUSIC);
                if (!musicFile.exists()) {
                    System.out.println("Background music file not found: " + BACKGROUND_MUSIC);
                    return;
                }
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
                backgroundMusicClip = AudioSystem.getClip();
                backgroundMusicClip.open(audioStream);
                backgroundMusicClip.loop(Clip.LOOP_CONTINUOUSLY);

                // Set maximum volume
                FloatControl volumeControl = (FloatControl) backgroundMusicClip.getControl(FloatControl.Type.MASTER_GAIN);
                volumeControl.setValue(volumeControl.getMaximum());

                backgroundMusicClip.start();
            } catch (Exception e) {
                System.out.println("Error playing background music: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    private void stopBackgroundMusic() {
        if (backgroundMusicClip != null) {
            backgroundMusicClip.stop();
            backgroundMusicClip.close(); // Close the clip to release resources
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (play) {
            if (ballposX <= 0 || ballposX >= 670) {
                ballXdir = -ballXdir;
                playSound(BRICK_HIT_SOUND);
            }
            if (ballposY <= 0) {
                ballYdir = -ballYdir;
                playSound(BRICK_HIT_SOUND);
            }

            if (map.checkCollision(ballposX, ballposY, 20)) {
                ballYdir = -ballYdir;
                score += 10;
                playSound(BRICK_HIT_SOUND);
            }

            if (ballposY + 20 >= 550 && ballposY + 20 <= 558 && ballposX + 10 >= playerX && ballposX + 10 <= playerX + 100) {
                ballYdir = -ballYdir;
                score += 5;
                playSound(BRICK_HIT_SOUND);
            }

            if (ballposY > 600) {
                endGame();
            }

            ballposX += ballXdir;
            ballposY += ballYdir;
        }
        repaint();
    }

    private void restartGame() {
        stopBackgroundMusic();
        playBackgroundMusic();
        ballposX = (692 - 20) / 2;
        ballposY = 350;
        ballXdir = -1;
        ballYdir = -2;
        playerX = 310;
        score = 0;
        play = true;
        map = new MapGenerator(4, 12);
        repaint();
    }

    private void endGame() {
        play = false;
        stopBackgroundMusic(); // Stop music immediately when the game ends
        repaint();
        saveScoreToDatabase(username, score);
    }

    // Method to save score to the database (Make sure to call this when the game ends)
    public void saveScoreToDatabase(String username, int score) {
        try (Connection conn = DBconnection.getConnection()) {
            String sql = "SELECT score FROM scores WHERE username = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    // Update the score regardless of whether it's higher or lower
                    String updateSql = "UPDATE scores SET score = ?, timestamp = ? WHERE username = ?";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                        updateStmt.setInt(1, score); // Set the new score
                        updateStmt.setTimestamp(2, new Timestamp(System.currentTimeMillis())); // Update timestamp
                        updateStmt.setString(3, username);
                        updateStmt.executeUpdate();
                    }
                } else {
                    // Insert a new record if the user does not exist
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

}
