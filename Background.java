import java.awt.Graphics;

import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Background { 
	private int x;
	private int y;
	private BufferedImage backgroundImg;

	public Background(int x, int y) {
		this.x = x;
		this.y = y;
		
		try {
			backgroundImg = ImageIO.read(new File("Background.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void draw(Graphics g) {
		g.drawImage(backgroundImg, x, y, null);
	}
}