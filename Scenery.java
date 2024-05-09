import java.util.Vector;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;	

public class Scenery extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
	Background background;
	
	Player player;	
	boolean upArrowPressed;
	boolean leftArrowPressed;
	boolean rightArrowPressed;
	int xv, yv;
	int jumpPower;
	int respawnX, respawnY;
	int deaths;
	boolean physicsOn;
	
	Vector<Platforms> platforms = new Vector<Platforms>(0);
	Vector<Enemies> enemies = new Vector<Enemies>(0);
	
	int scene;
	int sceneTrans;
	
	Goal goal;
	
	Text text;
	
	long eventWait;
	
	Color borders;
	
	public Scenery() {
		background = new Background(1000, 1000, 5);
		
		respawnX = 150; // 150
		respawnY = 850;
		player = new Player(respawnX, respawnY, 25, 25);
		deaths = 0;
		
		upArrowPressed = false;
		leftArrowPressed = false;
		rightArrowPressed = false;
		physicsOn = false;
		
		jumpPower = -15;
		xv = 0;
		yv = 0;
		
		setFocusable(true);
        addKeyListener(this);
		
		enemies.add(new Enemies(0, 975, 1000, 25, 0));
		
		scene = 0;
		sceneTrans = 0;
		
		goal = new Goal(960, 50, 40, 150, 2);
		
		text = new Text(100, 100, 25);
		
		eventWait = -10000;
		
		borders = new Color(238, 238, 238);
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(1000, 1000);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		background.draw(g, 245, 245, 255, 220, 220, 220);
		
		player.draw(g);
		
		for (int i = 0; i < enemies.size(); i++) {
			if (enemies.get(i).getLayer() == 0) enemies.get(i).draw(g);
		}
		
		for (int i = 0; i < platforms.size(); i++) {
			platforms.get(i).draw(g);
		}
		
		for (int i = 0; i < enemies.size(); i++) {
			if (enemies.get(i).getLayer() == 1) enemies.get(i).draw(g);
		}
		
		goal.draw(g, 255, 255, 255, 0, 155, 255, 155, 255);
		
		text.draw(g);
		
		g.setColor(borders);
		g.fillRect(1000, 0, 500, 1000);
		g.fillRect(0, 1000, 1500, 500);
	}
	
	public void mouseClicked(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseDragged(MouseEvent e) {}
	
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == 38 || e.getKeyCode() == 87) {
		    upArrowPressed = true;
		}
		if (e.getKeyCode() == 37 || e.getKeyCode() == 65) {
		    leftArrowPressed = true;
		}
		if (e.getKeyCode() == 39 || e.getKeyCode() == 68) {
		    rightArrowPressed = true;
		}	
    }
	
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == 38 || e.getKeyCode() == 87) {
		    upArrowPressed = false;
		}
		if (e.getKeyCode() == 37 || e.getKeyCode() == 65) {
		    leftArrowPressed = false;
		}
		if (e.getKeyCode() == 39 || e.getKeyCode() == 68) {
		    rightArrowPressed = false;
		}	
    }
	
	public void keyTyped(KeyEvent e) {} 
	
	public boolean detectCollision() {
		boolean touchingGround = false;
		int playerX = player.getX();
		int playerY = player.getY();
		int playerW = player.getW();
		int playerH = player.getH();
		
		for (int i = 0; i < platforms.size(); i++) {
			if (touchingGround) break;
			touchingGround = playerX + playerW >= platforms.get(i).getX() && 
			playerX <= platforms.get(i).getX() + platforms.get(i).getW() &&
			playerY + playerH >= platforms.get(i).getY() && 
			playerY <= platforms.get(i).getY() + platforms.get(i).getH();
			// System.out.print(touchingGround + " ");
		}
		// System.out.println();
		return touchingGround;
	}
	
	public boolean detectEnemy() {
		boolean touchingEnemy = false;
		int playerX = player.getX();
		int playerY = player.getY();
		int playerW = player.getW();
		int playerH = player.getH();
		
		for (int i = 0; i < enemies.size(); i++) {
			if (touchingEnemy) break;
			touchingEnemy = playerX + playerW >= enemies.get(i).getX() && 
			playerX <= enemies.get(i).getX() + enemies.get(i).getW() &&
			playerY + playerH >= enemies.get(i).getY() && 
			playerY <= enemies.get(i).getY() + enemies.get(i).getH();
		}
		return touchingEnemy;
	}
	
	public void physics() {
		player.changeY(-1);
		boolean touchingGround = detectCollision();
		
		if (rightArrowPressed) {
			if (xv < 5) xv++;
		} else if (xv > 0) {
			xv--;
		}
		if (leftArrowPressed) {
			if (xv > -5) xv--;
		} else if (xv < 0){
			xv++;
		}

		if (upArrowPressed && touchingGround) {
			yv = jumpPower;
		} else if (!touchingGround) {
			if (yv < 10) yv++;
		} else if (touchingGround) {
			yv = 0;
			while (detectCollision()) {
				player.changeY(-1);
			}
		}
		
		player.changeX(xv);
		if (detectCollision()) {
			player.changeX(xv * -1);
			xv = 0;
		}
		
		player.changeY(yv - 1);
		if (detectCollision() && yv <= 0) {
			yv = 0;
			while (detectCollision()) {
				player.changeY(1);
			}
		}
		player.changeY(3);
	}
	
	public void resetScene() {
		int platformSize = platforms.size();
		for (int i = 0; i < platformSize; i++) platforms.remove(0);
		
		int enemySize = enemies.size();
		for (int i = 0; i < enemySize; i++) enemies.remove(0);
		
		text.setText("");
		text.setPercent(0);
		
		if (scene >= 10) scene = 10;
		else if (scene >= 5 && scene < 10) scene = 5;
		else scene = 0;
		sceneTrans = 0;
	}
	
	public void sceneShift() { // REMEMBER IT IS platforms.get(ID).changeXY((WHERE YOU WANT TO GO - platforms.get(SAME ID).getXY()) / 5);
		int playerX = player.getX();
		int playerY = player.getY();
		int playerW = player.getW();
		int playerH = player.getH();
		
		if (scene == 0 && sceneTrans == 0) {
			sceneTrans = 1;
			platforms.add(new Platforms(0, 900, 1000, 100));
			platforms.add(new Platforms(975, 200, 25, 800));
			platforms.add(new Platforms(0, 0, 25, 1000));
			platforms.add(new Platforms(0, 0, 1000, 25));
			platforms.add(new Platforms(975, 0, 25, 50));
			platforms.add(new Platforms(300, 900, 100, 700));
		
			enemies.add(new Enemies(0, 975, 1000, 25, 0));
			
			text.setText("Use A and D to move left and right, and use W to jump.");
			
			physicsOn = true;
		}
		if (scene == 1 && playerX >= 400 && sceneTrans == 0) {
			sceneTrans = 1;
			platforms.add(new Platforms(650, 900, 100, 700));
			platforms.add(new Platforms(800, 900, 100, 25));
			
			text.setPercent(0);
			text.setText("Your goal is to get to the green area.");
		}
		if (scene == 2 && playerX + playerW >= 800 && playerY <= 750 && sceneTrans == 0) {
			sceneTrans = 1;
			platforms.add(new Platforms(475, 900, 100, 700));
			platforms.add(new Platforms(125, 900, 100, 700));
			
			text.setPercent(0);
			text.setText("Don't touch the red!");
		}
		if (scene == 3 && playerX <= 225 && playerY <= 900 && sceneTrans == 0) {
			sceneTrans = 1;
			
			text.setPercent(0);
			text.setText("");
		}
		if (scene == 4 && playerX + playerW >= 775 && playerY + playerH <= 310 && sceneTrans == 0) {
			sceneTrans = 1;
		}
		if (scene == 5 && playerY + playerH >= 900 && sceneTrans == 0) {
			sceneTrans = 1;
			
			physicsOn = false;
			
			text.setText("Hmph.");
		}
		if (scene == 6 && System.currentTimeMillis() > eventWait + 2000 && sceneTrans == 0) {
			sceneTrans = 1;
			
			text.setPercent(0);
			text.setText("You thought it was that easy?");
		}
		if (scene == 7 && System.currentTimeMillis() > eventWait + 2500 && sceneTrans == 0) {
			sceneTrans = 1;
			
			text.setPercent(0);
			text.setText("That was just the tutorial.");
		}
		if (scene == 8 && System.currentTimeMillis() > eventWait + 2500 && sceneTrans == 0) {
			sceneTrans = 1;
			
			text.setPercent(0);
			if (deaths == 0) text.setText("You do seem kind of good, though.");
			else if (deaths <= 3) text.setText("But I think you did OK for now.");
			else if (deaths <= 7) text.setText("And to put it nicely, I think you need more practice.");
			else if (deaths >= 8) {
				text.setSize(23);
				text.setText("Did you die that many times on purpouse? You died " + deaths + " times!");
			}
		}
		if (scene == 9 && System.currentTimeMillis() > eventWait + ((deaths < 3) ? 3000 : 4500) && sceneTrans == 0) {
			sceneTrans = 1;
			
			text.setSize(25);
			text.setPercent(0);
			text.setText("Good luck.");
		}
		if (scene == 10 && System.currentTimeMillis() > eventWait + 2000 && sceneTrans == 0) {
			resetScene();
			
			platforms.add(new Platforms(0, 900, 1000, 100));
			platforms.add(new Platforms(975, 200, 25, 800));
			platforms.add(new Platforms(0, 0, 25, 1000));
			platforms.add(new Platforms(0, 0, 1000, 25));
			platforms.add(new Platforms(975, 0, 25, 50));
			platforms.add(new Platforms(200, 900, 100, 25));
		
			enemies.add(new Enemies(0, 975, 1000, 25, 0));
			
			physicsOn = true;
			
			text.setPercent(0);
			text.setText("");
			
			sceneTrans = 1;
		}
		
		// // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // //
		
		if (sceneTrans == 1 && scene == 0) {
			platforms.get(5).changeY((845 - platforms.get(5).getY()) / 5);
			
			text.changePercent((1 - text.getPercent()) / 5);
			
			if (platforms.get(5).getY() < 850) {
				scene++;
				sceneTrans = 0;
			}
		}
		if (sceneTrans == 1 && scene == 1) {
			platforms.get(6).changeY((845 - platforms.get(6).getY()) / 5);
			platforms.get(7).changeY((775 - platforms.get(7).getY()) / 5);
			
			text.changePercent((1 - text.getPercent()) / 5);
			System.out.println(text.getPercent());
			
			if (platforms.get(6).getY() < 850) {
				scene++;
				sceneTrans = 0;
			}
		}
		if (sceneTrans == 1 && scene == 2) {
			platforms.get(0).changeX((1005 - platforms.get(0).getX()) / 5);
			platforms.get(5).changeY((675 - platforms.get(5).getY()) / 5);
			platforms.get(8).changeY((750 - platforms.get(8).getY()) / 5);
			platforms.get(9).changeY((600 - platforms.get(9).getY()) / 5);
			
			text.changePercent((1 - text.getPercent()) / 5);
			
			if (platforms.get(0).getX() > 1000) {
				scene++;
				sceneTrans = 0;
			}
		}
		if (sceneTrans == 1 && scene == 3) {
			platforms.get(5).changeY((525 - platforms.get(5).getY()) / 5);
			platforms.get(6).changeY((375 - platforms.get(6).getY()) / 5);
			platforms.get(7).changeY((300 - platforms.get(7).getY()) / 5);
			platforms.get(8).changeY((450 - platforms.get(8).getY()) / 5);
			platforms.get(7).changeH((705 - platforms.get(7).getH()) / 5);
			
			if (platforms.get(7).getH() > 700) {
				scene++;
				sceneTrans = 0;
			}
			// System.out.println(playerX + playerW + " " + (playerY + playerH));
		}
		if (sceneTrans == 1 && scene == 4) {
			platforms.get(0).changeX((0 - platforms.get(0).getX()) / 5);
			platforms.get(5).changeY((1000 - platforms.get(5).getY()) / 5);
			platforms.get(6).changeY((1000 - platforms.get(6).getY()) / 5);
			platforms.get(7).changeY((1005 - platforms.get(7).getY()) / 5);
			platforms.get(8).changeY((1000 - platforms.get(8).getY()) / 5);
			platforms.get(9).changeY((1000 - platforms.get(9).getY()) / 5);

			if (platforms.get(7).getY() > 1000) {	
				scene++;
				sceneTrans = 0;
			
				resetScene();
				
				platforms.add(new Platforms(0, 900, 1000, 100));
				platforms.add(new Platforms(975, 200, 25, 800));
				platforms.add(new Platforms(0, 0, 25, 1000));
				platforms.add(new Platforms(0, 0, 1000, 25));
				platforms.add(new Platforms(975, 0, 25, 50));
				enemies.add(new Enemies(0, 975, 1000, 25, 0));
			}
		}
		if (sceneTrans == 1 && scene == 5) {
			text.changePercent((1 - text.getPercent()) / 5);
			
			System.out.println("ok tutorial over");
			
			if (text.getPercent() > 0.9) {
				scene++;
				sceneTrans = 0;
				
				eventWait = System.currentTimeMillis();
			}
		}
		if (sceneTrans == 1 && scene == 6) {
			text.changePercent((1 - text.getPercent()) / 5);
			
			System.out.println("more stuff");
			
			if (text.getPercent() > 0.95) {
				scene++;
				sceneTrans = 0;
				
				eventWait = System.currentTimeMillis();
			}
		}
		if (sceneTrans == 1 && scene == 7) {
			text.changePercent((1 - text.getPercent()) / 5);
			
			System.out.println("even more stuff");
			
			if (text.getPercent() > 0.95) {
				scene++;
				sceneTrans = 0;
				
				eventWait = System.currentTimeMillis();
			}
		}
		if (sceneTrans == 1 && scene == 8) {
			text.changePercent((1 - text.getPercent()) / 5);
			
			System.out.println("stuffstuffstuff");
			
			if (text.getPercent() > 0.95) {
				scene++;
				sceneTrans = 0;
				
				eventWait = System.currentTimeMillis();
			}
		}
		if (sceneTrans == 1 && scene == 9) {
			text.changePercent((1 - text.getPercent()) / 5);
			
			System.out.println("stuffstuffstuffokimdonenowstuffstuffstuff");
			
			if (text.getPercent() > 0.9) {
				scene++;
				sceneTrans = 0;
				
				eventWait = System.currentTimeMillis();
			}
		}
		if (sceneTrans == 1 && scene == 10) {	
			platforms.get(5).changeY((825 - platforms.get(5).getY()) / 5);
			
			if (false) {
				scene++;
				sceneTrans = 0;
				
				eventWait = System.currentTimeMillis();
			}
		}
		System.out.println(deaths);
		
		// System.out.println(platforms.size());
	}
	
	public void animate() {
		for(;;) {
			try {
				Thread.sleep(15);
			} catch(InterruptedException ex){
				Thread.currentThread().interrupt();
			}
			
			if (physicsOn) physics();
			if (detectEnemy()) {
				player.setX(respawnX);
				player.setY(respawnY);
				resetScene();
				deaths++;
			}
			sceneShift();
			
			repaint();
		}
	}
}