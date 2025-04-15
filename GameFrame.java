import java.awt.*;
import javax.swing.*;

public class GameFrame extends JFrame {
	
	// setup the frame and instantiate the panel
	GameFrame() {
		
		this.add(new GamePanel());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Cube Jumping Adventure");
		this.setResizable(false);
		this.setBackground(new Color(0, 200, 255));
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		
	}
	
}
