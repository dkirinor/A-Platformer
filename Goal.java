import java.awt.Graphics;
import java.awt.Color;

public class Goal {
	int x;
	int y;
    int w;
    int h;
    double det;
	Color goal;

    public Goal(int _x, int _y, int _w, int _h, double _det) {
        x = _x;
        y = _y;
		w = _w;
        h = _h;
        det = _det;
    }

    public void draw(Graphics g, int red1, int green1, int blue1, int alpha1, int red2, int green2, int blue2, int alpha2) {
		for (int i = 0; i < w / det; i++) {
            double ratio = (double)i / (double)(w / det);
            int red = (int)(red1 * (1 - ratio) + red2 * ratio);
            int green = (int)(green1 * (1 - ratio) + green2 * ratio);
            int blue = (int)(blue1 * (1 - ratio) + blue2 * ratio);
			int alpha = (int)(alpha1 * (1 - ratio) + alpha2 * ratio);

            goal = new Color(red, green, blue, alpha);
            g.setColor(goal);
            g.fillRect(x + (int)(i * det), y, (int)det, h);

            // System.out.println(red + " " + green + " " + blue + " " + i);
        }
    }
}

// Version 0.0.06