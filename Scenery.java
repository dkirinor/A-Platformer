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
	boolean died;
	
	Vector<Platforms> platforms = new Vector<Platforms>(0);
	Vector<Enemies> enemies = new Vector<Enemies>(0);
	
	double moveChange;
	
	int scene;
	
	Goal goal;
	
	Text text;
	String[] taunts = new String[5];
	
	long eventWait;
	
	Color borders;
	
	public Scenery() {
		background = new Background(0, 0);
		
		respawnX = 150;
		respawnY = 850;
		player = new Player(respawnX, respawnY, 25, 25);
		deaths = 0;
		died = false;
		
		upArrowPressed = false;
		leftArrowPressed = false;
		rightArrowPressed = false;
		physicsOn = false;
		
		jumpPower = -15;
		xv = 0;
		yv = 0;
		
		setFocusable(true);
        addKeyListener(this);
		
		enemies.add(new Enemies(0, 975, 1000, 1000, 0));
		
		scene = 10; // -1
		
		goal = new Goal(960, 50, 40, 150, 2);
		
		text = new Text(100, 100, 25);
		taunts[0] = "...";
		taunts[1] = "......";
		taunts[2] = "hehe";
		taunts[3] = "lol";
		taunts[4] = "welp";
		
		eventWait = -10000;
		
		borders = new Color(238, 238, 238);
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(1000, 1000);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		background.draw(g);
		
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
		g.fillRect(1000, 0, 1000, 1000);
		g.fillRect(0, 1000, 2000, 100);
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
		else scene = -1;
	}
	
	public void sceneShift() { // REMEMBER IT IS platforms.get(ID).changeXY((WHERE YOU WANT TO GO - platforms.get(SAME ID).getXY()) / 5);
		int playerX = player.getX();
		int playerY = player.getY();
		int playerW = player.getW();
		int playerH = player.getH();
		
		
		// // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // //
		
		if (scene == -1) {
			scene++;
			
			platforms.add(new Platforms(0, 900, 1000, 100));
			platforms.add(new Platforms(975, 200, 25, 800));
			platforms.add(new Platforms(0, 0, 25, 1000));
			platforms.add(new Platforms(0, 0, 1000, 25));
			platforms.add(new Platforms(975, 0, 25, 50));
			platforms.add(new Platforms(300, 900, 100, 700));
		
			enemies.add(new Enemies(0, 975, 1000, 1000, 0));
			
			text.setText("Use A and D to move left and right, and use W to jump.");
			
			physicsOn = true;
		}
		if (scene == 0) {
			platforms.get(5).changeY((850 - platforms.get(5).getY()) / 5);
			
			text.changePercent((1 - text.getPercent()) / 5);
			
			if (playerX >= 400) {
				scene++;
				
				platforms.add(new Platforms(650, 900, 100, 700));
				platforms.add(new Platforms(800, 900, 100, 25));
				
				text.setPercent(0);
				text.setText("Your goal is to get to the green area.");
			}
		}
		if (scene == 1) {
			platforms.get(6).changeY((850 - platforms.get(6).getY()) / 5);
			platforms.get(7).changeY((775 - platforms.get(7).getY()) / 5);
			
			text.changePercent((1 - text.getPercent()) / 5);
			// System.out.println(text.getPercent());
			
			if (playerX + playerW >= 800 && playerY <= 750) {
				scene++;
				
				platforms.add(new Platforms(475, 900, 100, 700));
				platforms.add(new Platforms(125, 900, 100, 700));
				
				text.setPercent(0);
				text.setText("Don't touch the red!");
			}
		}
		if (scene == 2) {
			platforms.get(0).changeX((1000 - platforms.get(0).getX()) / 5);
			platforms.get(5).changeY((675 - platforms.get(5).getY()) / 5);
			platforms.get(8).changeY((750 - platforms.get(8).getY()) / 5);
			platforms.get(9).changeY((600 - platforms.get(9).getY()) / 5);
			
			text.changePercent((1 - text.getPercent()) / 5);
			
			if (playerX <= 225 && playerY <= 900) {
				scene++;
				
				text.setPercent(0);
				text.setText("");
			}
		}
		if (scene == 3) {
			platforms.get(5).changeY((525 - platforms.get(5).getY()) / 5);
			platforms.get(6).changeY((375 - platforms.get(6).getY()) / 5);
			platforms.get(7).changeY((300 - platforms.get(7).getY()) / 5);
			platforms.get(8).changeY((450 - platforms.get(8).getY()) / 5);
			platforms.get(7).changeH((700 - platforms.get(7).getH()) / 5);
			
			if (playerX + playerW >= 775 && playerY + playerH <= 310) {
				scene++;
			}
			// System.out.println(playerX + playerW + " " + (playerY + playerH));
		}
		if (scene == 4) {
			platforms.get(0).changeX((0 - platforms.get(0).getX()) / 5);
			platforms.get(5).changeY((1000 - platforms.get(5).getY()) / 5);
			platforms.get(6).changeY((1000 - platforms.get(6).getY()) / 5);
			platforms.get(7).changeY((1000 - platforms.get(7).getY()) / 5);
			platforms.get(8).changeY((1000 - platforms.get(8).getY()) / 5);
			platforms.get(9).changeY((1000 - platforms.get(9).getY()) / 5);

			if (playerY + playerH >= 900) {	
				scene++;
			
				resetScene();
				
				platforms.add(new Platforms(0, 900, 1000, 100));
				platforms.add(new Platforms(975, 200, 25, 800));
				platforms.add(new Platforms(0, 0, 25, 1000));
				platforms.add(new Platforms(0, 0, 1000, 25));
				platforms.add(new Platforms(975, 0, 25, 50));
				
				enemies.add(new Enemies(0, 975, 1000, 1000, 0));
				
				text.setText("Hmph.");
				
				physicsOn = false;
				
				eventWait = System.currentTimeMillis();
			}
		}
		if (scene == 5) {
			text.changePercent((1 - text.getPercent()) / 5);
			
			if (System.currentTimeMillis() > eventWait + 2000) {
				scene++;
				
				text.setPercent(0);
				if (deaths <= 3) {
					text.setText("You thought it was that easy?");
				} else {
					text.setText("That was just the tutorial.");
					scene++;
				}
				
				eventWait = System.currentTimeMillis();
			}
		}
		if (scene == 6) {
			text.changePercent((1 - text.getPercent()) / 5);
			
			if (System.currentTimeMillis() > eventWait + 2500) {
				scene++;
				
				text.setPercent(0);
				text.setText("That was just the tutorial.");
				
				eventWait = System.currentTimeMillis();
			}
		}
		if (scene == 7) {
			text.changePercent((1 - text.getPercent()) / 5);
			
			if (System.currentTimeMillis() > eventWait + 2500) {
				scene++;
				
				text.setPercent(0);
				if (deaths == 0) text.setText("You do seem kind of good, though.");
				else if (deaths <= 3) text.setText("But I think you did OK for now.");
				else if (deaths <= 7) text.setText("And to put it nicely, I think you need more practice.");
				else if (deaths >= 8) {
					text.setText("Did you die that many times on purpouse? You died " + deaths + " times!");
				}
				
				eventWait = System.currentTimeMillis();
			}
		}
		if (scene == 8) {
			text.changePercent((1 - text.getPercent()) / 5);
			
			if (System.currentTimeMillis() > eventWait + ((deaths < 3) ? 3000 : 4500)) {
				scene++;
				
				text.setPercent(0);
				text.setText("I am going to make it slightly harder now, so good luck.");
				
				eventWait = System.currentTimeMillis();
			}
		}
		if (scene == 9) {
			text.changePercent((1 - text.getPercent()) / 5);
			
			if (System.currentTimeMillis() > eventWait + 4000) {
				scene++;
			
				
			}
		}
		if (scene == 10) {
			resetScene();
			
			respawnX = 800;
			respawnY = 850;
			
			scene++;
			
			platforms.add(new Platforms(0, 900, 500, 100));
			platforms.add(new Platforms(500, 900, 600, 100));
			platforms.add(new Platforms(975, 200, 25, 800));
			platforms.add(new Platforms(0, 0, 25, 1000));
			platforms.add(new Platforms(0, 0, 1000, 25));
			platforms.add(new Platforms(975, 0, 25, 50));
			platforms.add(new Platforms(75, 900, 75, 25));
			platforms.add(new Platforms(175, 900, 75, 25));
			
			enemies.add(new Enemies(0, 975, 1000, 1000, 0));
			
			text.setText("");
			
			// physicsOn = true;
		}
		if (scene == 11) {
			if (playerX + playerW < 625) {
				platforms.get(0).changeX((-125 - platforms.get(0).getX()) / 5);
				platforms.get(1).changeX((625 - platforms.get(1).getX()) / 5);
				player.changeY(8);
			}
			
			if (playerX + playerW >= 625) {
				scene++;
				
				physicsOn = true;
			}
		}
		if (scene == 12) {
			// System.out.println(1);
			platforms.get(6).changeY((775 - platforms.get(6).getY()) / 5);
			platforms.get(7).changeY((825 - platforms.get(7).getY()) / 5);
			
			if (playerX + playerW < 600) {
				scene++;
			}
		}
		if (scene == 13) {
			platforms.get(0).changeX((-75 - platforms.get(0).getX()) / 5);
			platforms.get(1).changeX((575 - platforms.get(1).getX()) / 5);
			
			if (playerX + playerW < 550 || playerX + playerW >= 600) {
				scene++;
			}
		}
		if (scene == 14) {
			if (playerX + playerW > 575) {
				platforms.get(0).changeX((-75 - platforms.get(0).getX()) / 5);
				platforms.get(1).changeX((725 - platforms.get(1).getX()) / 5);
			}
			
			if (playerX + playerW < 525 && playerY <= 900) {
				scene++;
			}
		}
		if (scene == 15) {
			platforms.get(0).changeX((-175 - platforms.get(0).getX()) / 5);
			platforms.get(1).changeX((475 - platforms.get(1).getX()) / 5);
			
			if (playerX <= 125 && playerY + playerH <= 775) {
				scene++;
			}
		}
		if (scene == 16) {
			platforms.get(0).changeX((-500 - platforms.get(0).getX()) / 5);
			platforms.get(1).changeX((1000 - platforms.get(1).getX()) / 5);
			platforms.get(7).changeY((700 - platforms.get(7).getY()) / 5);
			
			if (playerX >= 175 && playerY + playerH <= 700) {
				scene++;
				platforms.remove(0);
				platforms.remove(0);
				
				text.setPercent(0);
				text.setText("Hmm... I'm already running out of ideas.");
				
				eventWait = System.currentTimeMillis();
				
			}
		}
		if (scene == 17) {
			text.changePercent((1 - text.getPercent()) / 5);
			
			platforms.get(4).changeY((1100 - platforms.get(4).getY()) / 5);
			
			if (System.currentTimeMillis() > eventWait + 2500) {
				scene++;
				
				platforms.add(new Platforms(325, 1000, 75, 25));
				platforms.add(new Platforms(475, 1000, 75, 25));
				platforms.add(new Platforms(625, 1000, 75, 25));
				platforms.add(new Platforms(800, 1000, 75, 25));
				
				enemies.add(new Enemies(250, -25, 75, 25, 1));
				enemies.add(new Enemies(400, -25, 75, 25, 1));
				enemies.add(new Enemies(550, -25, 75, 25, 1));
				enemies.add(new Enemies(700, -25, 75, 25, 1));
				
				platforms.remove(4);
				
				text.setPercent(0);
				text.setText("Here, let me try something new...");
				
				eventWait = System.currentTimeMillis();
			}
		}
		if (scene == 18) {
			text.changePercent((1 - text.getPercent()) / 5);
			
			platforms.get(5).changeY((800 - platforms.get(5).getY()) / 5);
			platforms.get(6).changeY((800 - platforms.get(6).getY()) / 5);
			platforms.get(7).changeY((800 - platforms.get(7).getY()) / 5);
			platforms.get(8).changeY((600 - platforms.get(8).getY()) / 5);
			
			enemies.get(1).changeY((200 - enemies.get(1).getY()) / 5);
			enemies.get(2).changeY((200 - enemies.get(2).getY()) / 5);
			enemies.get(3).changeY((200 - enemies.get(3).getY()) / 5);
			enemies.get(4).changeY((200 - enemies.get(4).getY()) / 5);
			
			if (System.currentTimeMillis() > eventWait + 2500 || (playerX + playerW >= 325 && playerY + playerH >= 800)) {
				scene++;
				
				text.setPercent(0);
				if (playerX + playerW >= 325) {
					text.setText("Hey! Go back I'm not done yet.");
					eventWait = System.currentTimeMillis();
				} else {
					text.setText("");
					scene++;
				}
				
				moveChange = 0;
			}
		}
		if (scene == 19) {
			text.changePercent((1 - text.getPercent()) / 5);
			
			if (System.currentTimeMillis() > eventWait + 8000) {
				text.setText("...");
				
				platforms.get(4).changeY((1100 - platforms.get(4).getY()) / 5);
				platforms.get(5).changeY((1100 - platforms.get(5).getY()) / 5);
				platforms.get(6).changeY((1100 - platforms.get(6).getY()) / 5);
				platforms.get(7).changeY((1100 - platforms.get(7).getY()) / 5);
				platforms.get(8).changeY((1100 - platforms.get(8).getY()) / 5);
				
				enemies.get(1).changeY((-100 - enemies.get(1).getY()) / 5);
				enemies.get(2).changeY((-100 - enemies.get(2).getY()) / 5);
				enemies.get(3).changeY((-100 - enemies.get(3).getY()) / 5);
				enemies.get(4).changeY((-100 - enemies.get(4).getY()) / 5);
			} else if (playerX < 250 && playerY + playerH <= 700) {
				scene++;
				
				text.setPercent(0);
				text.setText("");
			}
			
		}
		if (scene == 20) {
			platforms.get(5).setY((int)(300 * Math.cos(moveChange) + 500));
			platforms.get(6).setY((int)(300 * Math.cos(moveChange * 0.8) + 500));
			platforms.get(7).setY((int)(300 * Math.cos(moveChange * 1.5) + 500));
			
			enemies.get(1).setY((int)(-300 * Math.cos(moveChange * 1.5) + 500));
			enemies.get(2).setY((int)(-300 * Math.cos(moveChange * 2) + 500));
			enemies.get(3).setY((int)(-300 * Math.cos(moveChange * 1.5) + 500));
			enemies.get(4).setY((int)(-300 * Math.cos(moveChange * 2.5) + 500));
			
			moveChange += 0.02;
			for (int j = 5; j <= 7; j++) {
				if (playerX + playerW >= platforms.get(j).getX() && 
				playerX <= platforms.get(j).getX() + platforms.get(j).getW() &&
				playerY + playerH >= platforms.get(j).getY() && 
				playerY <= platforms.get(j).getY() + platforms.get(j).getH()) {
					if (upArrowPressed) player.setY(platforms.get(j).getY() - playerH + 1);
					else player.setY(platforms.get(j).getY() - playerH);
				}
			}
			//platforms.get(7).changeW();
			//if (
			
			if (playerX + playerW >= 800 && playerY + playerH >= 600) {
				scene++;
				
				eventWait = System.currentTimeMillis();
			}
		}
		if (scene == 21) {
			text.changePercent((1 - text.getPercent()) / 5);
			
			platforms.get(4).changeY((1100 - platforms.get(4).getY()) / 5);
			platforms.get(5).changeY((1100 - platforms.get(5).getY()) / 5);
			platforms.get(6).changeY((1100 - platforms.get(6).getY()) / 5);
			platforms.get(7).changeY((1100 - platforms.get(7).getY()) / 5);
			
			enemies.get(1).changeY((1100 - enemies.get(1).getY()) / 5);
			enemies.get(2).changeY((1100 - enemies.get(2).getY()) / 5);
			enemies.get(3).changeY((1100 - enemies.get(3).getY()) / 5);
			enemies.get(4).changeY((1100 - enemies.get(4).getY()) / 5);
			
			if (System.currentTimeMillis() > eventWait + 500) {
				scene++;
				
				for (int i = 0; i < 4; i++) platforms.remove(4);
				for (int i = 0; i < 4; i++) enemies.remove(1);
				
				platforms.add(new Platforms(-700, 900, 700, 100));
				platforms.add(new Platforms(-700, 850, 150, 100));
				platforms.add(new Platforms(-725, 875, 200, 50));
				platforms.add(new Platforms(-400, 825, 150, 100));
				platforms.add(new Platforms(-400, 875, 250, 50));
				platforms.add(new Platforms(-700, 750, 50, 25));
				platforms.add(new Platforms(-600, 700, 25, 25));
				platforms.add(new Platforms(-575, 650, 25, 25));
				
				enemies.add(new Enemies(0, 1100, 1000, 1000, 1));
			}
		}
		if (scene == 22) {
			platforms.get(4).changeY((800 - platforms.get(4).getY()) / 5);
			platforms.get(5).changeX((25 - platforms.get(5).getX()) / 5);
			platforms.get(6).changeX((25 - platforms.get(6).getX()) / 5);
			platforms.get(7).changeX((25 - platforms.get(7).getX()) / 5);
			platforms.get(8).changeX((400 - platforms.get(8).getX()) / 5);
			platforms.get(9).changeX((400 - platforms.get(9).getX()) / 5);
			platforms.get(10).changeX((25 - platforms.get(10).getX()) / 5);
			platforms.get(11).changeX((125 - platforms.get(11).getX()) / 5);
			platforms.get(12).changeX((200 - platforms.get(12).getX()) / 5);
			
			enemies.get(1).changeY((975 - enemies.get(1).getY()) / 5);
		
			if (playerX <= 700) {
				scene++;
				
				enemies.remove(0);
				
				moveChange = 0;
			}
		}
		if (scene == 23) {
			platforms.get(4).changeY((200 - platforms.get(4).getY()) / 5);
			
			moveChange++;
			if (moveChange % 5 == 0) {
				for (int i = 5; i < platforms.size(); i++) {
					platforms.get(i).changeY(1);
				}
			}
		
			if (false) {
				scene++;
			}
		}
		// System.out.println(deaths);
		
		// System.out.println(platforms.size());
		
		// -------------------------------------------------------------------------------------------------------------------------------
	}
	
	public void death() {
		if (!died) {
			died = true;
			physicsOn = false;
			if (Math.random() * 100 < 20) {
				// System.out.println(taunts[(int)(Math.random() * 5)]);
				text.setPercent(1);
				text.setSize(15);
				text.setText(taunts[(int)(Math.random() * 5)]);
			}
		}
		if (player.getOpacity() > 0) player.changeOpacity(-15);
		else {
			text.setSize(25);
			player.setX(respawnX);
			player.setY(respawnY);
			player.setOpacity(255);
			resetScene();
			deaths++;
			physicsOn = true;
			died = false;
		}
	}
	
	public void animate() {
		for(;;) {
			try {
				Thread.sleep(15);
			} catch(InterruptedException ex){
				Thread.currentThread().interrupt();
			}
			
			if (physicsOn) physics();
			if (detectEnemy() || died) {
				death();
			}
			sceneShift();
			
			repaint();
		}
	}
}