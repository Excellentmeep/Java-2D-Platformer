import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable {
	
	// initialize variables and objects
	static final int GAME_WIDTH = 1200;
	static final int GAME_HEIGHT = 800;
	static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
	double maxFallSpeed = 15.0;
	double maxJumpSpeed = 15.0;
	double fallSpeed = 0.0;
	double jumpSpeed = 7.5;
	int playerSize = 50;
	int speed = 5;
	int jumpHeight = 7;
	int gravity = 0;
	int jumped = 0;
	int counter = 0;
	int level = 1;
	int plat1TurnLeft;
	int plat2TurnLeft;
	int plat3TurnLeft;
	int plat4TurnLeft;
	int plat1TurnRight;
	int plat2TurnRight;
	int plat3TurnRight;
	int plat4TurnRight;
	boolean running = true;
	boolean grounded = true;
	boolean movingUp = false;
	boolean movingRight = false;
	boolean movingLeft = false;
	boolean sprinting = false;
	boolean platform1_movingToTheRight = true;
	boolean platform2_movingToTheRight = false;
	boolean platform3_movingToTheRight = true;
	boolean platform4_movingToTheRight = false;
	Thread gameThread;
	Image image;
	Graphics graphics;
	Player player;
	Block goal;
	Block floor;
	Block key1;
	Block key2;
	Block ladder1;
	Block ladder2;
	Block ladder3;
	Block ladder4;
	Block ladder5;
	Block platform1;
	Block platform2;
	Block platform3;
	Block platform4;
	Block platform5;
	Block platform6;
	Block platform7;
	Block platform8;
	Block platform9;
	Block platform10;
	Block platform11;
	Block platform12;
	Block spike1;
	Block spike2;
	Block spike3;
	Block spike4;
	Block spike5;
	Block spike6;
	Block spike7;
	Block spike8;
	Block spike9;
	Block spike10;
	Block spike11;
	Block wall1;
	Block wall2;
	Block wall3;
	Block wall4;
	Block wall5;
	Block wall6;
	Block movingPlatform1;
	Block movingPlatform2;
	Block movingPlatform3;
	Block movingPlatform4;
	
	// generate the first level and start the thread holding the game loop
	GamePanel() {
		newLevel();
		this.setFocusable(true);
		this.addKeyListener(new AL());
		this.setPreferredSize(SCREEN_SIZE);
		
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	@Override
	public void run() {
		// game loop
		if(running) {
			long lastTime = System.nanoTime();
			double amountOfTicks = 60.0;
			double ns = 1000000000 / amountOfTicks;
			double delta = 0;
			while(true) {
				long now = System.nanoTime();
				delta += (now - lastTime)/ns;
				lastTime = now;
				if(delta >= 1) {
					if(fallSpeed < maxFallSpeed) {
						fallSpeed += 0.4;
						gravity = (int) Math.round(fallSpeed);
					}
					if(jumped > 0) {
						if(movingUp) {
							if(jumpSpeed <= maxJumpSpeed) {
								jumpSpeed += 0.7;
								jumpHeight = (int) Math.round(jumpSpeed);
								counter ++;
								if((counter > 5 && level >= 1 && level <= 4) || (counter > 3 && level == 5)) {
									movingUp = false;
								}
							}
						}
						if((counter > 5 && level >= 1 && level <= 4) || (counter > 3 && level == 5)) {
							if(jumpSpeed >= 0) {
								jumpSpeed -= 0.5;
								jumpHeight = (int) Math.round(jumpSpeed);
							}
							if(jumpSpeed < 1) {
								counter = 0;
								jumpSpeed = 7.5;
								jumpHeight = 7;
								jumped = 0;
								fallSpeed = 4.0;
								gravity = 4;
							}
						}
					}
					move();
					checkCollisions();
					repaint();
					delta--;
				}
			}
		}
	}
			
	// put the player in the starting position
	public void newPlayer() {
		if((level == 1) || (level == 2)) {
			player = new Player(50, 650, playerSize, playerSize);
		}
		if(level == 3) {
			player = new Player(1100, 650, playerSize, playerSize);
		}
		if(level == 4) {
			player = new Player(575, 550, playerSize, playerSize);
		}
		if(level == 5) {
			player = new Player(63, 725, playerSize, playerSize);
		}
	}
	// put the floor in the correct position
	public void newFloor() {
		if(level == 1) {
			floor = new Block(0, 700, GAME_WIDTH, 100);
		}
		if(level == 2) {
			floor = new Block(25, 700, 100, 100);
		}
		if(level == 3) {
			floor = new Block(1050, 700, 150, 100);
		}
		if(level == 4) {
			floor = new Block(2000, 2000, 0, 0);
		}
		if(level == 5) {
			floor = new Block(0, 750, 150, 100);
		}
	}
	// put the platforms in the correct position
	public void newPlatforms() {
		if(level >= 1 && level <= 4) {
			platform6 = new Block(2000, 2000, 0, 0);
			platform7 = new Block(2000, 2000, 0, 0);
			platform8 = new Block(2000, 2000, 0, 0);
			platform9 = new Block(2000, 2000, 0, 0);
			platform10 = new Block(2000, 2000, 0, 0);
			platform11 = new Block(2000, 2000, 0, 0);
			platform12 = new Block(2000, 2000, 0, 0);
		}
		if(level == 1) {
			platform1 = new Block(950, 550, 100, 50);
			platform2 = new Block(600, 400, 100, 50);
			platform3 = new Block(950, 250, 100, 50);
			platform4 = new Block(350, 400, 100, 50);
			platform5 = new Block(50, 250, 100, 50);
		}
		if(level == 2) {
			platform1 = new Block(350, 550, 100, 50);
			platform2 = new Block(50, 400, 100, 50);
			platform3 = new Block(350, 250, 100, 50);
			platform4 = new Block(650, 500, 150, 50);
			platform5 = new Block(1025, 425, 100, 50);
		}
		if(level == 3) {
			platform1 = new Block(500, 600, 100, 50);
			platform2 = new Block(200, 700, 100, 50);
			platform3 = new Block(50, 550, 100, 50);
			platform4 = new Block(50, 400, 100, 50);
			platform5 = new Block(200, 200, 0, 0);
		}
		if(level == 4) {
			platform1 = new Block(2000, 2000, 0, 0);
			platform2 = new Block(2000, 2000, 0, 0);
			platform3 = new Block(2000, 2000, 0, 0);
			platform4 = new Block(2000, 2000, 0, 0);
			platform5 = new Block(2000, 2000, 0, 0);
		}
		if(level == 5) {
			platform1 = new Block(100, 625, 50, 25);
			platform2 = new Block(0, 500, 50, 25);
			platform3 = new Block(100, 375, 50, 25);
			platform4 = new Block(300, 700, 50, 25);
			platform5 = new Block(400, 625, 50, 25);
			platform6 = new Block(420, 520, 50, 25);
			platform7 = new Block(370, 440, 50, 25);
			platform8 = new Block(2000, 2000, 0, 0);
			platform9 = new Block(2000, 2000, 0, 0);
			platform10 = new Block(2000, 2000, 0, 0);
			platform11 = new Block(2000, 2000, 0, 0);
			platform12 = new Block(2000, 2000, 0, 0);
		}
	}
	// put the walls in the correct position
	public void newWalls() {
		if(level == 1) {
			wall1 = new Block(550, 100, 400, 50);
			wall2 = new Block(490, 100, 60, 400);
			wall3 = new Block(2000, 2000, 0, 0);
			wall4 = new Block(2000, 2000, 0, 0);
			wall5 = new Block(2000, 2000, 0, 0);
			wall6 = new Block(2000, 2000, 0, 0);
		}
		if(level == 2) {
			wall1 = new Block(500, 150, 60, 650);
			wall2 = new Block(900, 0, 60, 300);
			wall3 = new Block(2000, 2000, 0, 0);
			wall4 = new Block(2000, 2000, 0, 0);
			wall5 = new Block(2000, 2000, 0, 0);
			wall6 = new Block(2000, 2000, 0, 0);
		}
		if(level == 3) {
			wall1 = new Block(0, 100, 1100, 60);
			wall2 = new Block(110, 0, 60, 100);
			wall3 = new Block(990, 300, 60, 500);
			wall4 = new Block(200, 160, 60, 400);
			wall5 = new Block(2000, 2000, 0, 0);
			wall6 = new Block(2000, 2000, 0, 0);
		}
		if(level == 4) {
			wall1 = new Block(550, 650, 100, 30);
			wall2 = new Block(550, 690, 100, 30);
			wall3 = new Block(490, 650, 60, 150);
			wall4 = new Block(650, 650, 60, 150);
			wall5 = new Block(2000, 2000, 0, 0);
			wall6 = new Block(2000, 2000, 0, 0);
		}
		if(level == 5) {
			wall1 = new Block(150, 150, 60, 650);
			wall2 = new Block(310, 0, 60, 650);
			wall3 = new Block(470, 150, 60, 650);
			wall4 = new Block(630, 0, 60, 650);
			wall5 = new Block(790, 150, 60, 650);
			wall6 = new Block(950, 0, 60, 650);
		}
	}
	// put the goal in the correct position
	public void newGoal() {
		if(level == 1) {
			goal = new Block(200, 50, 50, 50);
		}
		if(level == 2) {
			goal = new Block(1055, 50, 50, 50);
		}
		if(level == 3) {
			goal = new Block(25, 25, 50, 50);
		}
		if(level == 4) {
			goal = new Block(575, 730, 50, 50);
		}
		if(level == 5) {
			goal = new Block(2000, 2000, 0, 0);
		}
	}
	// put the ladders in the correct position
	public void newLadders() {
		if(level >= 1 && level <= 4) {
			ladder2 = new Block(2000, 2000, 0, 0);
			ladder3 = new Block(2000, 2000, 0, 0);
			ladder4 = new Block(2000, 2000, 0, 0);
			ladder5 = new Block(2000, 2000, 0, 0);
		}
		if(level == 1) {
			ladder1 = new Block(2000, 2000, 50, 50);
		}
		if(level == 2) {
			ladder1 = new Block(960, 150, 10, 100);
		}
		if(level == 3) {
			ladder1 = new Block(1050, 400, 10, 100);
		}
		if(level == 4) {
			ladder1 = new Block(2000, 2000, 0, 0);
		}
		if(level == 5) {
			ladder1 = new Block(140, 175, 10, 100);
			ladder2 = new Block(460, 175, 10, 200);
			ladder3 = new Block(620, 400, 10, 175);
			ladder4 = new Block(2000, 2000, 0, 0);
			ladder5 = new Block(2000, 2000, 0, 0);
		}
	}
	// put the spikes in the correct position
	public void newSpike() {
		if(level >= 1 && level <= 4) {
			spike6 = new Block(2000, 2000, 0, 0);
			spike7 = new Block(2000, 2000, 0, 0);
			spike8 = new Block(2000, 2000, 0, 0);
			spike9 = new Block(2000, 2000, 0, 0);
			spike10 = new Block(2000, 2000, 0, 0);
			spike11 = new Block(2000, 2000, 0, 0);
		}
		if(level == 1) {
			spike1 = new Block(2000, 2000, 50, 50);
			spike2 = new Block(2000, 2000, 50, 50);
			spike3 = new Block(2000, 2000, 50, 50);
			spike4 = new Block(2000, 2000, 50, 50);
			spike5 = new Block(2000, 2000, 50, 50);
		}
		if(level == 2) {
			spike1 = new Block(400, 525, 50, 25);
			spike2 = new Block(50, 375, 50, 25);
			spike3 = new Block(400, 225, 50, 25);
			spike4 = new Block(500, 120, 60, 30);
			spike5 = new Block(885, 400, 50, 50);
		}
		if(level == 3) {
			spike1 = new Block(800, 300, 50, 50);
			spike2 = new Block(550, 575, 50, 25);
			spike3 = new Block(375, 600, 50, 50);
			spike4 = new Block(2000, 2000, 50, 50);
			spike5 = new Block(2000, 2000, 50, 50);
		}
		if(level == 4) {
			spike1 = new Block(2000, 2000, 50, 50);
			spike2 = new Block(2000, 2000, 50, 50);
			spike3 = new Block(2000, 2000, 50, 50);
			spike4 = new Block(2000, 2000, 50, 50);
			spike5 = new Block(2000, 2000, 50, 50);
		}
		if(level == 5) {
			spike1 = new Block(150, 125, 60, 25);
			spike2 = new Block(225, 150, 30, 30);
			spike3 = new Block(265, 400, 30, 30);
			spike4 = new Block(425, 550, 30, 30);
			spike5 = new Block(460, 215, 10, 25);
			spike6 = new Block(595, 215, 25, 150);
			spike7 = new Block(2000, 2000, 0, 0);
			spike8 = new Block(2000, 2000, 0, 0);
			spike9 = new Block(2000, 2000, 0, 0);
			spike10 = new Block(2000, 2000, 0, 0);
			spike11 = new Block(2000, 2000, 0, 0);
		}
	}
	// put the keys in the correct position
	public void newKey() {
		if((level == 1) || (level == 2)) { 
			key1 = new Block(2000, 2000, 0, 0);
			key2 = new Block(2000, 2000, 0, 0);
		}
		if(level == 3) {
			key1 = new Block(85, 225, 25, 25);
			key2 = new Block(2000, 2000, 0, 0);
		}
		if(level == 4) {
			key1 = new Block(200, 100, 25, 25);
			key2 = new Block(975, 100, 25, 25);
		}
		if(level == 5) {
			key1 = new Block(1100, 75, 12, 12);
			key2 = new Block(600, 400, 12, 12);
		}
	}
	// put the moving platforms in the correct position
	public void newMovingPlatforms() {
		if((level == 1) || (level == 2) || (level == 3)) {
			movingPlatform1 = new Block(2000, 2000, 0, 0);
			movingPlatform2 = new Block(2000, 2000, 0, 0);
			movingPlatform3 = new Block(2000, 2000, 0, 0);
			movingPlatform4 = new Block(2000, 2000, 0, 0);
		}
		if(level == 4) {
			movingPlatform1 = new Block(50, 500, 100, 50);
			movingPlatform2 = new Block(1050, 350, 100, 50);
			movingPlatform3 = new Block(50, 200, 100, 50);
			movingPlatform4 = new Block(1050, 200, 100, 50);
			plat1TurnLeft = 1150;
			plat2TurnLeft = 1150;
			plat3TurnLeft = 400;
			plat4TurnLeft = 1150;
			plat1TurnRight = 50;
			plat2TurnRight = 50;
			plat3TurnRight = 50;
			plat4TurnRight = 800;
		}
		if(level == 5) {
			movingPlatform1 = new Block(2000, 2000, 0, 0);
			movingPlatform2 = new Block(2000, 2000, 0, 0);
			movingPlatform3 = new Block(2000, 2000, 0, 0);
			movingPlatform4 = new Block(2000, 2000, 0, 0);
			plat1TurnLeft = 2000;
		}
	}
	// create a new level
	public void newLevel() {
		if((level >= 1) && (level <= 4)) {
			playerSize = 50;
			speed = 5;
		}
		else if(level == 5) {
			playerSize = 25;
			speed = 3;
		}
		newKey();
		newWalls();
		newLadders();
		newGoal();
		newFloor();
		newMovingPlatforms();
		newPlatforms();
		newSpike();
		newPlayer();
	}
	public void checkCollisions() {
		// check if player has gone outside the top or bottom border
		if(player.y < -50) {
			newLevel();
		}
		if(player.y > GAME_HEIGHT) {
			newLevel();
		}
		// check if player has touched the left or right border and stop them if they do
		if(player.x + playerSize > GAME_WIDTH) {
			player.x = GAME_WIDTH - playerSize;
		}
		if(player.x < 0) {
			player.x = 0;
		}
		// check for collision with floors, ladders and platforms
		if(player.intersects(floor)
			|| player.intersects(ladder1)
			|| player.intersects(ladder2)
			|| player.intersects(ladder3)
			|| player.intersects(ladder4)
			|| player.intersects(ladder5)
			|| player.intersects(platform1)
			|| player.intersects(platform2) 
			|| player.intersects(platform3) 
			|| player.intersects(platform4) 
			|| player.intersects(platform5) 
			|| player.intersects(platform6) 
			|| player.intersects(platform7) 
			|| player.intersects(platform8)
			|| player.intersects(platform9)
			|| player.intersects(platform10)
			|| player.intersects(platform11)
			|| player.intersects(platform12)) {
			
			player.y -= 1;
			fallSpeed = 0;
			gravity = 0;
			grounded = true;
		}
		else if(jumped < 1) {
			grounded = false;
		}
		// check for the player standing on movingPlatform1 and move the player with it
		if((player.x > movingPlatform1.x && player.x < movingPlatform1.x + movingPlatform1.width) && (player.y + playerSize > movingPlatform1.y && player.y + playerSize < movingPlatform1.y + 25) || (player.x + playerSize > movingPlatform1.x && player.x + playerSize < movingPlatform1.x + movingPlatform1.width) && (player.y + playerSize > movingPlatform1.y && player.y + playerSize < movingPlatform1.y + 25)) {
			if(platform1_movingToTheRight) {
				player.x += 4;
			}
			else {
				player.x -= 4;
			}
			player.y = movingPlatform1.y - playerSize + 1;
			fallSpeed = 0;
			gravity = 0;
			grounded = true;
		}
		else if(jumped < 1) {
			grounded = false;
		}
		// check for the player standing on movingPlatform2 and move the player with it
		if((player.x > movingPlatform2.x && player.x < movingPlatform2.x + movingPlatform2.width) && (player.y + playerSize > movingPlatform2.y && player.y + playerSize < movingPlatform2.y + 25) || (player.x + playerSize > movingPlatform2.x && player.x + playerSize < movingPlatform2.x + movingPlatform2.width) && (player.y + playerSize > movingPlatform2.y && player.y + playerSize < movingPlatform2.y + 25)) {
			if(platform2_movingToTheRight) {
				player.x += 6;
			}
			else {
				player.x -= 6;
			}
			player.y = movingPlatform2.y - playerSize + 1;
			fallSpeed = 0;
			gravity = 0;
			grounded = true;
		}
		else if(jumped < 1) {
			grounded = false;
		}
		// check for the player standing on movingPlatform3 and move the player with it
		if((player.x > movingPlatform3.x && player.x < movingPlatform3.x + movingPlatform3.width) && (player.y + playerSize > movingPlatform3.y && player.y + playerSize < movingPlatform3.y + 25) || (player.x + playerSize > movingPlatform3.x && player.x + playerSize < movingPlatform3.x + movingPlatform3.width) && (player.y + playerSize > movingPlatform3.y && player.y + playerSize < movingPlatform3.y + 25)) {
			if(platform3_movingToTheRight) {
				player.x += 5;
			}
			else {
				player.x -= 5;
			}
			player.y = movingPlatform3.y - playerSize + 1;
			fallSpeed = 0;
			gravity = 0;
			grounded = true;
		}
		else if(jumped < 1) {
			grounded = false;
		}
		// check for the player standing on movingPlatform4 and move the player with it
		if((player.x > movingPlatform4.x && player.x < movingPlatform4.x + movingPlatform4.width) && (player.y + playerSize > movingPlatform4.y && player.y + playerSize < movingPlatform4.y + 25) || (player.x + playerSize > movingPlatform4.x && player.x + playerSize < movingPlatform4.x + movingPlatform4.width) && (player.y + playerSize > movingPlatform4.y && player.y + playerSize < movingPlatform4.y + 25)) {
			if(platform4_movingToTheRight) {
				player.x += 5;
			}
			else {
				player.x -= 5;
			}
			player.y = movingPlatform4.y - playerSize + 1;
			fallSpeed = 0;
			gravity = 0;
			grounded = true;
		}
		else if(jumped < 1) {
			grounded = false;
		}
		// check for the player touching the top, bottom, right and left sides of wall1
		if((player.x > wall1.x && player.x < wall1.x + wall1.width) && (player.y + playerSize > wall1.y && player.y + playerSize < wall1.y + 15) || (player.x + playerSize > wall1.x) && (player.x + playerSize < wall1.x + wall1.width) && (player.y + playerSize > wall1.y) && (player.y + playerSize < wall1.y + 15)) {
			player.y -= 1;
			fallSpeed = 0;
			gravity = 0;
			grounded = true;
		}
		else if(jumped < 1) {
			grounded = false;
		}
		if((player.x > wall1.x && player.x < wall1.x + wall1.width) && (player.y > wall1.y + wall1.height - 25 && player.y < wall1.y + wall1.height) || (player.x + playerSize > wall1.x) && (player.x + playerSize < wall1.x + wall1.width) && (player.y > wall1.y + wall1.height - 25) && (player.y < wall1.y + wall1.height)) {
			player.y = wall1.y + wall1.height;
		}
		if((player.x > wall1.x + wall1.width - 25 && player.x < wall1.x + wall1.width) && (player.y > wall1.y + 15 && player.y < wall1.y + wall1.height - 15) || (player.x > wall1.x + wall1.width - 25 && player.x < wall1.x + wall1.width) && (player.y + playerSize > wall1.y + 15 && player.y + playerSize < wall1.y + wall1.height - 15)) {
			player.x = wall1.x + wall1.width;
		}
		if((player.x + playerSize > wall1.x && player.x + playerSize < wall1.x + playerSize) && (player.y > wall1.y + 15 && player.y < wall1.y + wall1.height - 15) || (player.x + playerSize > wall1.x && player.x + playerSize < wall1.x + playerSize) && (player.y + playerSize > wall1.y + 15 && player.y + playerSize < wall1.y + wall1.height - 15)) {
			player.x = wall1.x - playerSize;
		}
		// check for the player touching the top, bottom, right and left sides of wall2
		if((player.x > wall2.x && player.x < wall2.x + wall2.width) && (player.y + playerSize > wall2.y) && (player.y + playerSize < wall2.y + 15) || (player.x + playerSize > wall2.x) && (player.x + playerSize < wall2.x + wall2.width) && (player.y + playerSize > wall2.y) && (player.y + playerSize < wall2.y + 15)) {
			player.y -= 1;
			fallSpeed = 0;
			gravity = 0;
			grounded = true;
		}
		else if(jumped < 1) {
			grounded = false;
		}
		if((player.x > wall2.x && player.x < wall2.x + wall2.width) && (player.y > wall2.y + wall2.height - 25 && player.y < wall2.y + wall2.height) || (player.x + playerSize > wall2.x) && (player.x + playerSize < wall2.x + wall2.width) && (player.y > wall2.y + wall2.height - 25) && (player.y < wall2.y + wall2.height)) {
			player.y = wall2.y + wall2.height;
		}
		if((player.x > wall2.x + wall2.width - 25 && player.x < wall2.x + wall2.width) && (player.y > wall2.y + 15 && player.y < wall2.y + wall2.height - 15) || (player.x > wall2.x + wall2.width - 25 && player.x < wall2.x + wall2.width) && (player.y + playerSize > wall2.y + 15 && player.y + playerSize < wall2.y + wall2.height - 15)) {
			player.x = wall2.x + wall2.width;
		}
		if((player.x + playerSize > wall2.x && player.x + playerSize < wall2.x + 25) && (player.y > wall2.y + 15 && player.y < wall2.y + wall2.height - 15) || (player.x + playerSize > wall2.x && player.x + playerSize < wall2.x + 25) && (player.y + playerSize > wall2.y + 15 && player.y + playerSize < wall2.y + wall2.height - 15)) {
			player.x = wall2.x - playerSize;
		}
		// check for the player touching the top, bottom, right and left sides of wall3
		if((player.x > wall3.x && player.x < wall3.x + wall3.width) && (player.y + playerSize > wall3.y) && (player.y + playerSize < wall3.y + 15) || (player.x + playerSize > wall3.x) && (player.x + playerSize < wall3.x + wall3.width) && (player.y + playerSize > wall3.y) && (player.y + playerSize < wall3.y + 15)) {
			player.y -= 1;
			fallSpeed = 0;
			gravity = 0;
			grounded = true;
		}
		else if(jumped < 1) {
			grounded = false;
		}
		if((player.x > wall3.x && player.x < wall3.x + wall3.width) && (player.y > wall3.y + wall3.height - 25 && player.y < wall3.y + wall3.height) || (player.x + playerSize > wall3.x && player.x + playerSize < wall3.x + wall3.width) && (player.y > wall3.y + wall3.height - 25) && (player.y < wall3.y + wall3.height)) {
			player.y = wall3.y + wall3.height;
		}
		if((player.x > wall3.x + wall3.width - 25 && player.x < wall3.x + wall3.width) && (player.y > wall3.y + 15 && player.y < wall3.y + wall3.height - 15) || (player.x > wall3.x + wall3.width - 25 && player.x < wall3.x + wall3.width) && (player.y + playerSize > wall3.y + 15 && player.y + playerSize < wall3.y + wall3.height - 15)) {
			player.x = wall3.x + wall3.width;
		}
		if((player.x + playerSize > wall3.x && player.x + playerSize < wall3.x + 25) && (player.y > wall3.y + 15 && player.y < wall3.y + wall3.height - 15) || (player.x + playerSize > wall3.x && player.x + playerSize < wall3.x + 25) && (player.y + playerSize > wall3.y + 15 && player.y + playerSize < wall3.y + wall3.height - 15)) {
			player.x = wall3.x - playerSize;
		}
		// check for the player touching the top, bottom, right and left sides of wall4
		if((player.x > wall4.x && player.x < wall4.x + wall4.width) && (player.y + playerSize > wall4.y && player.y + playerSize < wall4.y + 15) || (player.x + playerSize > wall4.x && player.x + playerSize < wall4.x + wall4.width) && (player.y + playerSize > wall4.y && player.y + playerSize < wall4.y + 15)) {
			player.y -= 1;
			fallSpeed = 0;
			gravity = 0;
			grounded = true;
		}
		else if(jumped < 1) {
			grounded = false;
		}
		if((player.x > wall4.x && player.x < wall4.x + wall4.width) && (player.y > wall4.y + wall4.height - 25 && player.y < wall4.y + wall4.height) || (player.x + playerSize > wall4.x) && (player.x + playerSize < wall4.x + wall4.width) && (player.y > wall4.y + wall4.height - 25) && (player.y < wall4.y + wall4.height)) {
			player.y = wall4.y + wall4.height;
		}
		if((player.x > wall4.x + wall4.width - 25 && player.x < wall4.x + wall4.width) && (player.y > wall4.y + 15 && player.y < wall4.y + wall4.height - 15) || (player.x > wall4.x + wall4.width - 25 && player.x < wall4.x + wall4.width) && (player.y + playerSize > wall4.y + 15 && player.y + playerSize < wall4.y + wall4.height - 15)) {
			player.x = wall4.x + wall4.width;
		}
		if((player.x + playerSize > wall4.x && player.x + playerSize < wall4.x + 25) && (player.y > wall4.y + 15 && player.y < wall4.y + wall4.height - 15) || (player.x + playerSize > wall4.x && player.x + playerSize < wall4.x + 25) && (player.y + playerSize > wall4.y + 15 && player.y + playerSize < wall4.y + wall4.height - 15)) {
			player.x = wall4.x - playerSize;
		}
		// check for the player touching the top, bottom right and left sides of wall5
		if((player.x > wall5.x && player.x < wall5.x + wall5.width) && (player.y + playerSize > wall5.y) && (player.y + playerSize < wall5.y + 15) || (player.x + playerSize > wall5.x) && (player.x + playerSize < wall5.x + wall5.width) && (player.y + playerSize > wall5.y) && (player.y + playerSize < wall5.y + 15)) {
			player.y -= 1;
			fallSpeed = 0;
			gravity = 0;
			grounded = true;
		}
		else if(jumped < 1) {
			grounded = false;
		}
		if((player.x > wall5.x && player.x < wall5.x + wall5.width) && (player.y > wall5.y + wall5.height - 25 && player.y < wall5.y + wall5.height) || (player.x + playerSize > wall5.x) && (player.x + playerSize < wall5.x + wall5.width) && (player.y > wall5.y + wall5.height - 25) && (player.y < wall5.y + wall5.height)) {
			player.y = wall5.y + wall5.height;
		}
		if((player.x > wall5.x + wall5.width - 25 && player.x < wall5.x + wall5.width) && (player.y > wall5.y + 15 && player.y < wall5.y + wall5.height - 15) || (player.x > wall5.x + wall5.width - 25 && player.x < wall5.x + wall5.width) && (player.y + playerSize > wall5.y + 15 && player.y + playerSize < wall5.y + wall5.height - 15)) {
			player.x = wall5.x + wall5.width;
		}
		if((player.x + playerSize > wall5.x && player.x + playerSize < wall5.x + 25) && (player.y > wall5.y + 15 && player.y < wall5.y + wall5.height - 15) || (player.x + playerSize > wall5.x && player.x + playerSize < wall5.x + 25) && (player.y + playerSize > wall5.y + 15 && player.y + playerSize < wall5.y + wall5.height - 15)) {
			player.x = wall5.x - playerSize;
		}
		// check for the player touching the top, bottom right and left sides of wall6
		if((player.x > wall6.x && player.x < wall6.x + wall6.width) && (player.y + playerSize > wall6.y) && (player.y + playerSize < wall6.y + 15) || (player.x + playerSize > wall6.x) && (player.x + playerSize < wall6.x + wall6.width) && (player.y + playerSize > wall6.y) && (player.y + playerSize < wall6.y + 15)) {
			player.y -= 1;
			fallSpeed = 0;
			gravity = 0;
			grounded = true;
		}
		else if(jumped < 1) {
			grounded = false;
		}
		if((player.x > wall6.x && player.x < wall6.x + wall6.width) && (player.y > wall6.y + wall6.height - 25 && player.y < wall6.y + wall6.height) || (player.x + playerSize > wall6.x) && (player.x + playerSize < wall6.x + wall6.width) && (player.y > wall6.y + wall6.height - 25) && (player.y < wall6.y + wall6.height)) {
			player.y = wall6.y + wall6.height;
		}
		if((player.x > wall6.x + wall6.width - 25 && player.x < wall6.x + wall6.width) && (player.y > wall6.y + 15 && player.y < wall6.y + wall6.height - 15) || (player.x > wall6.x + wall6.width - 25 && player.x < wall6.x + wall6.width) && (player.y + playerSize > wall6.y + 15 && player.y + playerSize < wall6.y + wall6.height - 15)) {
			player.x = wall6.x + wall6.width;
		}
		if((player.x + playerSize > wall6.x && player.x + playerSize < wall6.x + 25) && (player.y > wall6.y + 15 && player.y < wall6.y + wall6.height - 15) || (player.x + playerSize > wall6.x && player.x + playerSize < wall6.x + 25) && (player.y + playerSize > wall6.y + 15 && player.y + playerSize < wall6.y + wall6.height - 15)) {
			player.x = wall6.x - playerSize;
		}
		// check for the player reaching the goal
		if(player.intersects(goal)) {
			if(level == 5) {
				running = false;
			}
			else {
				level ++;
				newLevel();
			}
		}
		// check for the player grabbing a key
		if(player.intersects(key1)) {
			if(level == 3) {
				key1 = new Block(2000, 2000, 0, 0);
				floor = new Block(2000, 2000, 0, 0);
				ladder1 = new Block(1190, 150, 10, 100);
				wall2 = new Block(2000, 2000, 0, 0);
				wall3 = new Block(990, 400, 60, 400);
				wall4 = new Block(200, 300, 60, 500);
				spike1 = new Block(800, 475, 50, 25);
				spike3 = new Block(375, 325, 50, 50);
				spike4 = new Block(1050, 675, 150, 125);
				platform2 = new Block(750, 500, 100, 50);
				platform3 = new Block(2000, 2000, 0, 0);
			}
			if(level == 4) {
				key1 = new Block(2000, 2000, 0, 0);
				wall1 = new Block(2000, 2000, 0, 0);
			}
			if(level == 5) {
				key1 = new Block(2000, 2000, 0, 0);
				key2 = new Block(2000, 2000, 0, 0);
				goal = new Block(63, 670, 25, 25);
				floor = new Block(2000, 2000, 0, 0);
				ladder1 = new Block(850, 500, 10, 200);
				ladder2 = new Block(940, 300, 10, 250);
				ladder3 = new Block(850, 175, 10, 175);
				ladder4 = new Block(210, 175, 10, 100);
				ladder5 = new Block(2000, 2000, 0, 0);
				movingPlatform1 = new Block(210, 725, 60, 25);
				platform1 = new Block(1085, 125, 115, 25);
				platform2 = new Block(950, 750, 100, 25);
				platform3 = new Block(630, 725, 60, 25);
				platform4 = new Block(530, 600, 50, 25);
				platform5 = new Block(580, 475, 50, 25);
				platform6 = new Block(530, 350, 50, 25);
				platform7 = new Block(580, 225, 50, 25);
				platform8 = new Block(420, 350, 50, 25);
				platform9 = new Block(260, 600, 50, 25);
				platform10 = new Block(210, 475, 50, 25);
				platform11 = new Block(260, 350, 50, 25);
				platform12 = new Block(40, 700, 70, 25);
				spike1 = new Block(0, 750, 150, 100);
				spike2 = new Block(1010, 300, 85, 30);
				spike3 = new Block(1100, 500, 100, 30);
				spike4 = new Block(850, 375, 50, 100);
				spike5 = new Block(900, 225, 50, 50);
				spike6 = new Block(790, 125, 60, 25);
				spike7 = new Block(750, 160, 30, 30);
				spike8 = new Block(700, 400, 30, 30);
				spike9 = new Block(150, 125, 60, 25);
				spike10 = new Block(75, 300, 75, 30);
				spike11 = new Block(0, 500, 75, 30);
				plat1TurnRight = 210;
				plat1TurnLeft = 470;
			}
		}
		if(player.intersects(key2)) {
			if(level == 4) {
				key2 = new Block(2000, 2000, 0, 0);
				wall2 = new Block(2000, 2000, 0, 0);
			}
			if(level == 5) {
				key2 = new Block(2000, 2000, 0, 0);
				ladder4 = new Block(780, 300, 10, 220);
				ladder5 = new Block(1010, 200, 10, 400);
				spike7 = new Block(780, 350, 10, 25);
				spike8 = new Block(790, 125, 60, 25);
				spike9 = new Block(860, 200, 30, 30);
				spike10 = new Block(910, 400, 30, 30);
				spike11 = new Block(860, 600, 30, 30);
				platform8 = new Block(610, 730, 50, 25);
				platform9 = new Block(710, 630, 50, 25);
				platform10 = new Block(710, 200, 50, 25);
				platform11 = new Block(860, 750, 50, 25);
				platform12 = new Block(1010, 700, 50, 25);
			}
		}
		// check for the player hitting a spike
		if(player.intersects(spike1) 
			|| player.intersects(spike2)
			|| player.intersects(spike3)
			|| player.intersects(spike4)
			|| player.intersects(spike5)
			|| player.intersects(spike6)
			|| player.intersects(spike7)
			|| player.intersects(spike8)
			|| player.intersects(spike9)
			|| player.intersects(spike10)
			|| player.intersects(spike11)) {
			
			newLevel();
		}
	}
	// move the player and the moving platforms
	public void move() {
		if(movingRight) {
			player.moveRight(speed);
		}
		if(movingLeft == true && movingRight == false) {
			player.moveLeft(speed);
		}
		if(sprinting) {
			if(level >= 1 && level <= 4) {
				speed = 8;
			}
			else if(level == 5) {
				speed = 5;
			}
		}
		if(grounded == false) {
			player.fall(gravity);
		}
		if(jumped > 0) {
			if(grounded) {
				player.jump(-jumpHeight);
			}
		}
		if(platform1_movingToTheRight) {
			movingPlatform1.x += 4;
			if(movingPlatform1.x + movingPlatform1.width >= plat1TurnLeft) {
				platform1_movingToTheRight = false;
			}
		}
		else {
			movingPlatform1.x -= 4;
			if(movingPlatform1.x <= plat1TurnRight) {
				platform1_movingToTheRight = true;
			}
		}
		if(platform2_movingToTheRight) {
			movingPlatform2.x += 6;
			if(movingPlatform2.x + movingPlatform2.width >= plat2TurnLeft) {
				platform2_movingToTheRight = false;
			}
		} 
		else {
			movingPlatform2.x -= 6;
			if(movingPlatform2.x <= plat2TurnRight) {
				platform2_movingToTheRight = true;
			}
		}
		if(platform3_movingToTheRight) {
			movingPlatform3.x += 5;
			if(movingPlatform3.x + movingPlatform3.width >= plat3TurnLeft) {
				platform3_movingToTheRight = false;
			}
		} 
		else {
			movingPlatform3.x -= 5;
			if(movingPlatform3.x <= plat3TurnRight) {
				platform3_movingToTheRight = true;
			}
		}
		if(platform4_movingToTheRight) {
			movingPlatform4.x += 5;
			if(movingPlatform4.x + movingPlatform4.width >= plat4TurnLeft) {
				platform4_movingToTheRight = false;
			}
		} 
		else {
			movingPlatform4.x -= 5;
			if(movingPlatform4.x <= plat4TurnRight) {
				platform4_movingToTheRight = true;
			}
		}
			
	}
	public void paint(Graphics g) {
		image = createImage(getWidth(), getHeight());
		graphics = image.getGraphics();
		draw(graphics);
		g.drawImage(image, 0, 0, this);
	}
	// draw all of the components for the game and activate the end screen
	public void draw(Graphics g) {
		if(running) {
			goal.draw(g, Color.green);
			floor.draw(g, Color.green);
			ladder1.draw(g, new Color(122, 63, 28));
			ladder2.draw(g, new Color(122, 63, 28));
			ladder3.draw(g, new Color(122, 63, 28));
			ladder4.draw(g, new Color(122, 63, 28));
			ladder5.draw(g, new Color(122, 63, 28));
			wall1.draw(g, Color.gray);
			wall2.draw(g, Color.gray);
			wall3.draw(g, Color.gray);
			wall4.draw(g, Color.gray);
			wall5.draw(g, Color.gray);
			wall6.draw(g, Color.gray);
			spike1.draw(g, Color.red);
			spike2.draw(g, Color.red);
			spike3.draw(g, Color.red);
			spike4.draw(g, Color.red);
			spike5.draw(g, Color.red);
			spike6.draw(g, Color.red);
			spike7.draw(g, Color.red);
			spike8.draw(g, Color.red);
			spike9.draw(g, Color.red);
			spike10.draw(g, Color.red);
			spike11.draw(g, Color.red);
			platform1.draw(g, Color.white);
			platform2.draw(g, Color.white);
			platform3.draw(g, Color.white);
			platform4.draw(g, Color.white);
			platform5.draw(g, Color.white);
			platform6.draw(g, Color.white);
			platform7.draw(g, Color.white);
			platform8.draw(g, Color.white);
			platform9.draw(g, Color.white);
			platform10.draw(g, Color.white);
			platform11.draw(g, Color.white);
			platform12.draw(g, Color.white);
			movingPlatform1.draw(g, Color.white);
			movingPlatform2.draw(g, Color.white);
			movingPlatform3.draw(g, Color.white);
			movingPlatform4.draw(g, Color.white);
			key1.draw(g, Color.black);
			key2.draw(g, Color.black);
			player.draw(g);
		}
		else {
			gameEnd(g);
		}
	}
		
	public void gameEnd(Graphics g) {
		// draw end screen
		platform12.draw(g, Color.white);
		player.draw(g);
		g.setColor(Color.white);
		g.setFont(new Font("Ink Free",Font.BOLD,75));
		g.drawString("THANKS FOR PLAYING :)", GAME_WIDTH/2-475, GAME_HEIGHT/2-200);
		movingLeft = false;
		movingRight = false;
		jumped = 0;
		sprinting = false;
	}
	
	public class AL extends KeyAdapter {
		// check if key has been pressed
		public void keyPressed(KeyEvent e) {
			if(running) {
				switch(e.getKeyCode()) {
				case KeyEvent.VK_LEFT: movingLeft = true;
				break;
				case KeyEvent.VK_RIGHT: movingRight = true;
				break;
				case KeyEvent.VK_A: movingLeft = true;
				break;
				case KeyEvent.VK_D: movingRight = true;
				break;
				case KeyEvent.VK_UP: jumped += 1; movingUp = true;
				break;
				case KeyEvent.VK_W: jumped += 1; movingUp = true;
				break;
				case KeyEvent.VK_SPACE: jumped += 1; movingUp = true;
				break;
				case KeyEvent.VK_SHIFT: sprinting = true;
				break;
				}
			}
		}
		// check if a key has been released
		public void keyReleased(KeyEvent e) {
			if(running) {
				switch(e.getKeyCode()) {
				case KeyEvent.VK_LEFT: movingLeft = false;
				break;
				case KeyEvent.VK_RIGHT: movingRight = false;
				break;
				case KeyEvent.VK_A: movingLeft = false;
				break;
				case KeyEvent.VK_D: movingRight = false;
				break;
				case KeyEvent.VK_SHIFT: sprinting = false; speed = 5;
				break;
				}
			}
		}
	}
}
