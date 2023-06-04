import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameMenu extends JFrame {
    private JButton startButton;
    private JButton quitButton;

    public GameMenu() {
        createView();

        setTitle("Red Riding Hood");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void createView() {
        JPanel panel = new JPanel();
        getContentPane().add(panel);

        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);

        startButton = new JButton("Start Game");
        panel.add(startButton, gbc);

        quitButton = new JButton("Quit");
        panel.add(quitButton, gbc);

        addActionListeners();
    }

    private void addActionListeners() {
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RedHood().setVisible(true); // Start game
                GameMenu.this.dispose(); // Close menu
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Quit application
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GameMenu().setVisible(true);
            }
        });
    }
}
