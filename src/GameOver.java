import java.awt.*;
import java.awt.image.ImageObserver;

import javax.swing.ImageIcon;
public class GameOver implements ImageObserver {
	
    //Creation of different fonts to be used with display
    Font font1 = new Font("Impact", Font.BOLD,35);
    Font font2 = new Font("Verdana", Font.PLAIN, 18);

    //This function serves to draw a string with the center being placed at the exact coordinates specified.
    public static void drawCenteredString(Graphics g, String text, int xx, int yy, int ww, int hh, Font ff) {
        FontMetrics metrics = g.getFontMetrics(ff);
        int x = xx + (ww - metrics.stringWidth(text)) / 2;
        int y = yy + ((hh - metrics.getHeight()) / 2) + metrics.getAscent();
        g.setFont(ff);
        g.drawString(text, x, y);
    }

    //Actually draws the Game Over Screen
    public void ded(Graphics p) {
    	Image gameOver = new ImageIcon("src/resources/images/gameover.png").getImage();
    	
    	p.drawImage(gameOver,30,30,this);
    	p.setColor(Color.red);
    	drawCenteredString(p, "Press the S to Start Over", 180, 335, 1, 1, font2);
        
    	/*
        //Sets the gameover background
            p.setColor(Color.BLACK);
            p.fillRect(0, 0, 500, 500);
        //draws the gameover text
            p.setColor(Color.WHITE);
            drawCenteredString(p, "Game Over", 250, 250, 1, 1, font1);
            drawCenteredString(p, "Press the S to Continue", 250, 300, 1, 1, font2);
    */
    }

	@Override
	public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
		// TODO Auto-generated method stub
		return false;
	}

}
