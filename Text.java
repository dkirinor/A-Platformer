import java.util.Vector;

import java.awt.Graphics;
import java.awt.Color;

import java.awt.Font;

public class Text { 
	private int x;
	private int y;
	private int size;
	private double percent;
	private String text;

	public Text(int x, int y, int size) {
		this.x = x;
		this.y = y;
		this.size = size;
		text = new String("");
		
		percent = 0;
	}

	public void draw(Graphics g) {
		Font font = new Font("Arial", Font.PLAIN, size);
		g.setFont(font);
		Color gray = new Color(80, 80, 120);	
		g.setColor(gray);
		int length = (int)(Math.clamp(text.length() * percent * 1.1, 0, text.length()));
		g.drawString(text.substring(0, length), x, y);
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
	
	public void setPercent(double percent) {
		this.percent = percent;
	}
	
	public void changePercent(double speed) {
        percent += speed;
    }
	
	public double getPercent() {
		return percent;
	}
}