package game;
import java.awt.Color;
import javax.swing.JFrame;

@SuppressWarnings("unused")
public class Main {
	public static void main(String[] args) {
		JFrame obj=new JFrame();
		Gameplay gamePlay = new Gameplay(null);
		
		obj.setBounds(10, 10, 700, 600);
		obj.setTitle("Brick Breaker Game");		
		obj.setResizable(false);
		obj.setVisible(true);
		obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		obj.add(gamePlay);
        obj.setVisible(true);
		
	}

}
