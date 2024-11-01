package game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class MapGenerator {
    public int map[][];
    public int brickWidth;
    public int brickHeight;
    public int row; // Number of rows
    public int col; // Number of columns

    public MapGenerator(int row, int col) {
        this.row = row;
        this.col = col;
        map = new int[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                map[i][j] = 1; // 1 means brick is present
            }
        }
        brickWidth = 540 / col; // Width of each brick
        brickHeight = 150 / row; // Height of each brick
    }

    public void draw(Graphics2D g) {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (map[i][j] > 0) {
                    // Draw the brick
                    g.setColor(Color.BLUE);
                    g.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
                    
                    // Draw the border for the brick
                    g.setColor(Color.BLACK);
                    g.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
                }
            }
        }
    }

    public boolean checkCollision(int ballX, int ballY, int ballDiameter) {
        Rectangle2D ballBounds = new Rectangle2D.Double(ballX, ballY, ballDiameter, ballDiameter);
        
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                // Calculate the brick boundaries
                if (map[i][j] > 0) {
                    Rectangle2D brickBounds = new Rectangle2D.Double(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);

                    // Check for collision with the brick
                    if (ballBounds.intersects(brickBounds)) {
                        // Mark the brick as broken
                        map[i][j] = 0; // Remove the brick
                        return true; // Collision detected
                    }
                }
            }
        }
        return false; // No collision detected
    }
}
