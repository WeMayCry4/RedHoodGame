import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class RedHood extends JFrame {
	private GameOver gameOver;
	
    public RedHood() {
        initUI();
    }

    private void initUI() {

        add(new Board());
        
        setTitle("Red Riding Hood");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(375, 420);
        setLocationRelativeTo(null);
    }
        
    public static void main(String[] args) {

    	EventQueue.invokeLater(() -> {
            var ex = new RedHood();
            ex.setVisible(true);
        });
    }
}
