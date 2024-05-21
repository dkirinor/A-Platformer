import java.util.Vector;

import javax.swing.JPanel;

import java.io.File;

import javax.sound.sampled.AudioInputStream; 
import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException; 
import javax.sound.sampled.UnsupportedAudioFileException; 

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
	int xOffset;
	
	Vector<Platforms> platforms = new Vector<Platforms>(0);
	Vector<Enemies> enemies = new Vector<Enemies>(0);
	
	double moveChange;
	
	int scene;
	int maxScene;
	
	Goal goal;
	
	Text text;
	String[] taunts = new String[5];
	
	long eventWait;
	
	Color borders;
	
	public Scenery() {
		background = new Background(0, 0);
		
		respawnX = 150;
		respawnY = 750;
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
		xOffset = 0;
		
		setFocusable(true);
        addKeyListener(this);
		
		// enemies.add(new Enemies(0, 975, 1000, 1000, 0));
		
		scene = -1; // -1 SCENESCENESCENESCENESCENESCENESCENESCENESCENESCENESCENESCENESCENESCENESCENESCENESCENESCENESCENESCENESCENESCENESCENESCENESCENE
		maxScene = -1; // -1
		
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
		g.fillRect(0, 1000, 2000, 1000);
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
	
	void playSound(String soundFile) {
		try {
			File f = new File(soundFile);
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());  
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
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
			playSound("Jump.wav");
			player.resetParent();
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
		
		if (scene >= 18) scene = 18;
		else if (scene >= 10) scene = 10;
		else if (scene >= 5 && scene < 10) scene = 5;
		else scene = -1;
		
		player.resetParent();
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
			
			if (playerX >= 300) {
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
			
			if (playerX + playerW >= 800 && playerY <= platforms.get(7).getY()) {
				scene++;
				
				platforms.add(new Platforms(475, 900, 100, 700));
				platforms.add(new Platforms(125, 900, 100, 700));
				
				text.setPercent(0);
				text.setText("Don't touch the red!");
			}
		}
		if (scene == 2) {
			platforms.get(0).changeX((575 - platforms.get(0).getX()) / 5);
			platforms.get(5).changeY((675 - platforms.get(5).getY()) / 5);
			platforms.get(8).changeY((750 - platforms.get(8).getY()) / 5);
			platforms.get(9).changeY((600 - platforms.get(9).getY()) / 5);
			
			text.changePercent((1 - text.getPercent()) / 5);
			
			if (playerX <= 225 && playerY + playerH <= platforms.get(9).getY()) {
				scene++;
				
				text.setPercent(0);
				text.setText("");
			}
		}
		if (scene == 3) {
			platforms.get(0).changeX((1000 - platforms.get(0).getX()) / 5);
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
			
			if (maxScene < scene) {
				text.setPercent(1);
				text.setText("I am going to make it slightly harder now, so good luck. >:D");
				eventWait = System.currentTimeMillis();
			}
			
			// physicsOn = true;
		}
		if (scene == 11) {
			if (playerX + playerW < 625) {
				platforms.get(0).changeX((-125 - platforms.get(0).getX()) / 5);
				platforms.get(1).changeX((625 - platforms.get(1).getX()) / 5);
				player.changeY(8);
			}
			
			if (System.currentTimeMillis() >= eventWait + 100) {
				scene++;
				
				physicsOn = true;
			}
		}
		if (scene == 12) {
			text.changePercent((0 - text.getPercent()) / 5);
			
			System.out.println(platforms.get(6).getY());
			platforms.get(6).changeY((775 - platforms.get(6).getY()) / 5);
			platforms.get(7).changeY((825 - platforms.get(7).getY()) / 5);
			
			if (playerX + playerW < 575) {
				scene++;
				
				text.setText("");
			}
		}
		if (scene == 13) {
			platforms.get(0).changeX((-62 - platforms.get(0).getX()) / 5);
			platforms.get(1).changeX((562 - platforms.get(1).getX()) / 5);
			
			if (playerX < 475 && playerY + playerH <= 900) {
				scene++;
			}
		}
		if (scene == 14) {
			platforms.get(0).changeX((-162 - platforms.get(0).getX()) / 5);
			platforms.get(1).changeX((462 - platforms.get(1).getX()) / 5);
			
			if (playerX <= 125 && playerY + playerH <= platforms.get(6).getY()) {
				scene++;
				
				platforms.add(new Platforms(325, 1000, 75, 25));
				platforms.add(new Platforms(475, 1000, 75, 25));
				platforms.add(new Platforms(625, 1000, 75, 25));
				platforms.add(new Platforms(800, 1000, 75, 25));
				
				enemies.add(new Enemies(250, -25, 75, 25, 1));
				enemies.add(new Enemies(400, -25, 75, 25, 1));
				enemies.add(new Enemies(550, -25, 75, 25, 1));
				enemies.add(new Enemies(700, -25, 75, 25, 1));
			}
		}
		if (scene == 15) {
			platforms.get(0).changeX((-500 - platforms.get(0).getX()) / 5);
			platforms.get(1).changeX((1000 - platforms.get(1).getX()) / 5);
			platforms.get(7).changeY((700 - platforms.get(7).getY()) / 5);
			platforms.get(8).changeY((800 - platforms.get(8).getY()) / 5);
			platforms.get(9).changeY((800 - platforms.get(9).getY()) / 5);
			platforms.get(10).changeY((800 - platforms.get(10).getY()) / 5);
			platforms.get(11).changeY((600 - platforms.get(11).getY()) / 5);
			
			enemies.get(1).changeY((200 - enemies.get(1).getY()) / 5);
			enemies.get(2).changeY((200 - enemies.get(2).getY()) / 5);
			enemies.get(3).changeY((200 - enemies.get(3).getY()) / 5);
			enemies.get(4).changeY((200 - enemies.get(4).getY()) / 5);
			
			if (playerX >= 175 && playerY + playerH <= platforms.get(7).getY()) {
				scene++;
				platforms.remove(0);
				platforms.remove(0);
				
				moveChange = 0;
				
			}
		}
		if (scene == 16) {
			platforms.get(4).changeY((1100 - platforms.get(4).getY()) / 5);
			platforms.get(6).setY((int)(300 * Math.cos(moveChange) + 500));
			platforms.get(7).setY((int)(300 * Math.cos(moveChange * 0.8) + 500));
			platforms.get(8).setY((int)(300 * Math.cos(moveChange * 1.5) + 500));
			
			enemies.get(1).setY((int)(-300 * Math.cos(moveChange * 1.5) + 500));
			enemies.get(2).setY((int)(-300 * Math.cos(moveChange * 2) + 500));
			enemies.get(3).setY((int)(-300 * Math.cos(moveChange * 1.5) + 500));
			enemies.get(4).setY((int)(-300 * Math.cos(moveChange * 2.5) + 500));
			
			moveChange += 0.02;
			for (int j = 6; j <= 8; j++) {
				if (playerX + playerW >= platforms.get(j).getX() && 
				playerX <= platforms.get(j).getX() + platforms.get(j).getW() &&
				playerY + playerH >= platforms.get(j).getY() && 
				playerY <= platforms.get(j).getY() + platforms.get(j).getH()) {
					if (upArrowPressed) player.setY(platforms.get(j).getY() - playerH + 1);
					else {
						yv = 0;
						player.setY(platforms.get(j).getY() - playerH);
					}
				}
			}
			//platforms.get(7).changeW();
			//if (
			
			if (playerX + playerW >= 800 && playerY + playerH >= 600) {
				platforms.remove(4);
				
				scene++;
				
				eventWait = System.currentTimeMillis();
			}
		}
		if (scene == 17) {
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
			}
		}
		if (scene == 18) {
			resetScene();
			
			scene++;
			
			physicsOn = true;
			
			respawnX = 800;
			respawnY = 750;
			
			platforms.add(new Platforms(975, 200, 25, 800)); // 0
			platforms.add(new Platforms(0, 0, 25, 1000));
			platforms.add(new Platforms(0, 0, 1000, 25));
			platforms.add(new Platforms(975, 0, 25, 50));
			platforms.add(new Platforms(800, 600, 75, 25));	
			platforms.add(new Platforms(25, 900, 700, 100)); // 5 (Maze Stuff)
			platforms.add(new Platforms(700, -800, 25, 1800));
			platforms.add(new Platforms(25, 850, 150, 100));
			platforms.add(new Platforms(25, 875, 200, 50));
			platforms.add(new Platforms(400, 825, 150, 100));
			platforms.add(new Platforms(400, 875, 250, 50)); // 10
			platforms.add(new Platforms(25, 750, 75, 25));
			platforms.add(new Platforms(150, 700, 50, 25));
			platforms.add(new Platforms(275, 650, 25, 25));
			platforms.add(new Platforms(300, 675, 25, 25));
			platforms.add(new Platforms(375, 625, 50, 25));
			platforms.add(new Platforms(475, 600, 50, 25));
			platforms.add(new Platforms(525, 575, 25, 25));
			platforms.add(new Platforms(275, 500, 25, 25));
			platforms.add(new Platforms(300, 525, 25, 25));
			platforms.add(new Platforms(25, 525, 75, 25)); // 20
			platforms.add(new Platforms(125, 475, 25, 25));
			platforms.add(new Platforms(25, 400, 50, 25));
			platforms.add(new Platforms(100, 375, 25, 25));
			platforms.add(new Platforms(125, 275, 25, 25));
			platforms.add(new Platforms(200, 250, 100, 25));
			platforms.add(new Platforms(275, 225, 100, 25));
			platforms.add(new Platforms(450, 200, 20, 200));
			platforms.add(new Platforms(475, 150, 20, 200));
			platforms.add(new Platforms(550, -100, 20, 400));
			platforms.add(new Platforms(550, 450, 20, 200)); // 30
			platforms.add(new Platforms(600, 375, 20, 200));
			platforms.add(new Platforms(675, 400, 20, 200));
			platforms.add(new Platforms(575, 350, 20, 20));
			platforms.add(new Platforms(675, 275, 20, 20));
			platforms.add(new Platforms(575, 200, 20, 20));
			platforms.add(new Platforms(625, 125, 20, 20));
			platforms.add(new Platforms(675, 50, 20, 20));
			platforms.add(new Platforms(575, -50, 20, 20));
			platforms.add(new Platforms(275, 0, 100, 25));
			platforms.add(new Platforms(350, 25, 100, 25)); // 40
			platforms.add(new Platforms(200, -75, 100, 25));
			platforms.add(new Platforms(25, -150, 150, 25));
			platforms.add(new Platforms(25, -250, 75, 25)); // 43 Moving Stuff Start
			platforms.add(new Platforms(25, -325, 75, 25));
			platforms.add(new Platforms(25, -400, 75, 25));
			platforms.add(new Platforms(25, -475, 75, 25));
			platforms.add(new Platforms(25, -550, 75, 25));
			platforms.add(new Platforms(25, -625, 75, 25)); // 48 Moving Stuff End
			platforms.add(new Platforms(25, -250, 25, 25));
			platforms.add(new Platforms(25, -325, 25, 25));
			platforms.add(new Platforms(25, -400, 25, 25));
			platforms.add(new Platforms(25, -475, 25, 25));
			platforms.add(new Platforms(25, -550, 25, 25));
			platforms.add(new Platforms(25, -625, 25, 25));
			platforms.add(new Platforms(675, -250, 25, 25));
			platforms.add(new Platforms(675, -325, 25, 25));
			platforms.add(new Platforms(675, -400, 25, 25));
			platforms.add(new Platforms(675, -475, 25, 25));
			platforms.add(new Platforms(675, -550, 25, 25));
			platforms.add(new Platforms(675, -625, 25, 25));
			platforms.add(new Platforms(325, -700, 75, 25));
			
			enemies.add(new Enemies(0, 975, 1000, 1000, 1));
			
			eventWait = System.currentTimeMillis();
			
			text.setPercent(0);
			if (maxScene >= scene) {
				scene += 5;
				platforms.get(4).changeY(200);
			}
			else {
				for (int i = 5; i < platforms.size(); i++) {
					platforms.get(i).changeX(-725);
				}
				
				text.setText("Uhh...");
			}
		}
		if (scene == 19) {
			text.changePercent((1 - text.getPercent()) / 5);
			
			platforms.get(4).changeY((800 - platforms.get(4).getY()) / 10);
			
			if (System.currentTimeMillis() > eventWait + 2000) {
				scene++;
				
				eventWait = System.currentTimeMillis();
				
				text.setPercent(0);
				text.setText("I'm running out of ideas.");
			}
		}
		if (scene == 20) {
			text.changePercent((1 - text.getPercent()) / 5);
			
			if (System.currentTimeMillis() > eventWait + 2500) {
				scene++;
				
				eventWait = System.currentTimeMillis();
				
				text.setPercent(0);
				text.setText("Here, let me get this thing from the storage room...");
			}
		}
		if (scene == 21) {
			text.changePercent((1 - text.getPercent()) / 5);
			
			if (System.currentTimeMillis() > eventWait + 3500) {
				scene++;
				
				eventWait = System.currentTimeMillis();
				
				text.setPercent(0);
				text.setText("Ungh...");
			}
		}
		if (scene == 22) {
			text.changePercent((1 - text.getPercent()) / 5);
			if (Math.random() * 100 < 10) {
				text.setText(text.getText() + "..");
				for (int i = 5; i < platforms.size(); i++) {
					platforms.get(i).changeX(25);
				}
			}
			
			if (platforms.get(5).getX() >= 25) {
				scene++;
				
				text.setPercent(0);
				text.setText("Heh... Sorry, it is a lot of game objects I'm moving at once.");
				
				eventWait = System.currentTimeMillis();
			}
		}
		if (scene == 23) {
			text.changePercent((1 - text.getPercent()) / 5);
			
			if (System.currentTimeMillis() > eventWait + 3500) {
				scene++;
				
				text.setPercent(0);
				text.setText("Anyways, good luck.");
			}
		}
		if (scene == 24) {
			platforms.get(6).changeY((-1000 - platforms.get(6).getY()) / 5);

			
			text.changePercent((1 - text.getPercent()) / 5);
			
			if (playerX < 725) {
				scene++;
				
				text.setPercent(0);
				text.setText("");
				
				moveChange = 0;
			}
		}
		if (scene == 25) {
			platforms.get(4).changeY((200 - platforms.get(4).getY()) / 5);
			
			moveChange++;
			if (moveChange % ((25 / ((int)(moveChange / 100) + 1)) + 1) == 0) {
				for (int i = 5; i < platforms.size(); i++) {
					platforms.get(i).changeY(1);
				}
			}
			
			platforms.get(43).changeX((int)(275 * Math.cos(moveChange / 100)) + 325 - platforms.get(43).getX());
			platforms.get(44).changeX((int)(275 * Math.cos(moveChange / 75 + 1)) + 325 - platforms.get(44).getX());
			platforms.get(45).changeX((int)(275 * Math.cos(moveChange / 100 + 2)) + 325 - platforms.get(45).getX());
			platforms.get(46).changeX((int)(275 * Math.cos(moveChange / 75 + 3)) + 325 - platforms.get(46).getX());
			platforms.get(47).changeX((int)(275 * Math.cos(moveChange / 50 + 4)) + 325 - platforms.get(47).getX());
			platforms.get(48).changeX((int)(275 * Math.cos(moveChange / 25 + 5)) + 325 - platforms.get(48).getX());
			
			for (int i = 43; i <= 48; i++) {
				if (playerX + playerW >= platforms.get(i).getX() && 
				playerX <= platforms.get(i).getX() + platforms.get(i).getW() &&
				playerY + playerH >= platforms.get(i).getY() && 
				playerY <= platforms.get(i).getY() + platforms.get(i).getH()) {
					//xOffset = platforms.get(i).getX() - playerX;
					platforms.get(i).setChild(player);
					player.setParent(platforms.get(i));
					//player.setX(platforms.get(i).getX() + xOffset);
				}
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
			if (maxScene < scene) maxScene = scene;
			System.out.println(maxScene);
			if (Math.random() * 100 < 20) {
				// System.out.println(taunts[(int)(Math.random() * 5)]);
				text.setPercent(1);
				text.setSize(15);
				text.setText(taunts[(int)(Math.random() * 5)]);
			}
			playSound("Death.wav");
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