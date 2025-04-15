import java.awt.*;

public class Player extends Rectangle {
	
	// construct the player with the given x, y, width and height
	Player(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	// make the player jump
	public void jump(int jumpHeight) {
		y += jumpHeight;
	}
	// make the player fall
	public void fall(int gravity) {
		y += gravity;
	}
	// move the player left
	public void moveLeft(int speed) {
		x -= speed;
	}
	// move the player right
	public void moveRight(int speed) {
		x += speed;
	}
	// draw the player onto the screen
	public void draw(Graphics g) {
		g.setColor(Color.orange);
		g.fillRect(x, y, width, height);
	}
	
}
