import java.awt.Graphics;
import java.awt.Color;

public class Platforms { 
	private int x;
	private int y;
	private int w;
	private int h;

	public Platforms(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	public void draw(Graphics g) {
		Color black = new Color(0, 0, 0);	
		g.setColor(black);
		g.fillRect(x, y, w, h);
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void changeX(int speed) {
		x += speed;		
	}

	public void changeY(int speed) {
		y += speed;
	}

	public void changeW(int speed) {
		w += speed;
	}
	
	public void changeH(int speed) {
		h += speed;		
	}

	public int getX() {						
		return x;							
	}

	public int getY() {
		return y;
	}
	
	public int getW() {						
		return w;							
	}

	public int getH() {
		return h;
	}
} 