import java.awt.Graphics;
import java.awt.Color;


public class Player {
	private int x;
	private int y;
	private int w;
	private int h;
	
	private int opacity;
	
	private Platforms parent;
	
	Color black;

	public Player(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		
		this.parent = null;
		opacity = 255;
	}

	public void draw(Graphics g) {
		black = new Color(50, 50, 50, opacity);
		g.setColor(black);
		g.fillRect(x, y, w, h);
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void setOpacity(int opacity) {
		this.opacity = opacity;
	}
	
	public void changeX(int speed) {
		x += speed;		
	}

	public void changeY(int speed) {
		y += speed;
	}
	
	public void changeOpacity(int speed) {
		opacity += speed;
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
	
	public int getOpacity() {
		return opacity;
	}
	
	public void setParent(Platforms platform) {
		this.parent = platform;
	}
	
	public Platforms getParent() {
		return parent;
	}
	
	public void resetParent() {
		if (parent != null) parent.resetChild();
		parent = null;
	}
} 