import java.awt.Graphics;
import java.awt.Color;

public class Enemies { 
	private int x;
	private int y;
	private int w;
	private int h;
	private int layer;
	
	Color red = new Color(255, 0, 0);
	Color redGlow = new Color(255, 0, 0, 50);
	Color redGlow2 = new Color(255, 0, 0, 100);

	public Enemies(int x, int y, int w, int h, int layer) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.layer = layer;
	}

	public void draw(Graphics g) {
		g.setColor(red);
		g.fillRect(x, y, w, h);
		g.setColor(redGlow2);
		g.fillRect(x - 4, y - 4, w + 8, h + 8);
		g.setColor(redGlow);
		g.fillRect(x - 8, y - 8, w + 16, h + 16);
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void setLayer(int layer) {
		this.layer = layer;
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
	
	public int getLayer() {
		return layer;
	}
} 