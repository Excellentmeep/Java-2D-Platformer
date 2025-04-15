import java.awt.*;

public class Block extends Rectangle {
	
	// construct the component for the game with the given x, y, width and height
	Block(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	
	// draw the component for the game onto the screen
	public void draw(Graphics g, Color c) {
		g.setColor(c);
		g.fillRect(x, y, width, height);
	}
	
}
