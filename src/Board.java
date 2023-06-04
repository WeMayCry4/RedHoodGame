import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

    private Dimension d;
    private final Font smallFont = new Font("Helvetica", Font.BOLD, 14);

    private Image ii;
    private final Color dotColor = new Color(192, 192, 0);
    private Color mazeColor;

    private boolean inGame = false;
    private boolean dying = false;
    private static int invisibilityTimeLeft = 0;
    private static final int DURATION = 5000; // 5 seconds in milliseconds
    private boolean invOn,frzOn;
    private boolean gameOver = false;
    private final int BLOCK_SIZE = 24;
    private final int N_BLOCKS = 15;
    protected final int SCREEN_SIZE = N_BLOCKS * BLOCK_SIZE;
    private final int PAC_ANIM_DELAY = 2;
    private final int PACMAN_ANIM_COUNT = 4;
    private final int MAX_GHOSTS = 10;
    private int PACMAN_SPEED = 6;

    private int numFirstAidKits = 1;
    private int apples = 4;
    private int cookie = 3;

    private int pacAnimCount = PAC_ANIM_DELAY;
    private int pacAnimDir = 1;
    private int pacmanAnimPos = 0;
    private int N_GHOSTS = 4;
    private int level = 1; 
    private int pacsLeft, score, highscore;
    private int[] dx, dy;
    private int[] ghost_x, ghost_y, ghost_dx, ghost_dy, ghostSpeed;

    private Image ghost,map1,map2,map3,trap,health,gameOverScreen,gameWonScreen,appleImage,cookieImage;
    private Image pacman1, pacman2up, pacman2left, pacman2right, pacman2down;
    private Image pacman3up, pacman3down, pacman3left, pacman3right;
    private Image pacman4up, pacman4down, pacman4left, pacman4right;

    private int pointsRemaining;
    private int pacman_x, pacman_y, pacmand_x, pacmand_y;
    private int req_dx, req_dy, view_dx, view_dy;

    GameOver gOver = new GameOver();
    GameOver gWon = new GameOver();
    //map for level 1
    private final short levelData[] = {
            3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 6,
            1, 4, 3, 26, 22, 0, 27, 98, 30, 0, 19, 26, 22, 0, 4,
            1, 19, 28, 0, 17, 22, 0, 21, 0, 3, 4, 0, 25, 22, 4,
            1, 21, 0, 0, 17, 20, 0, 21, 0, 97, 20, 0, 0, 21, 4,
            1, 25, 90, 26, 16, 24, 26, 24, 26, 24, 16, 26, 26, 28, 4,
            1, 1, 0, 0, 21, 3, 14, 2, 3, 6, 21, 0, 0, 4, 4,
            1, 19, 30, 0, 21, 5, 3, 0, 6, 5, 21, 0, 27, 22, 4,
            1, 21, 0, 0, 21, 1, 0, 0, 0, 4, 21, 0, 0, 21, 4,
            1, 17, 18, 26, 4, 9, 8, 8, 8, 12, 17, 26, 18, 4, 4,
            1, 17, 20, 0, 17, 26, 26, 18, 26, 26, 20, 4, 1, 4, 4,
            1, 17, 16, 26, 4, 0, 0, 21, 0, 0, 17, 26, 16, 4, 4,
            1, 17, 28, 0, 25, 18, 18, 24, 18, 18, 28, 0, 25, 4, 4,
            1, 21, 0, 0, 0, 17, 4, 15, 17, 4, 0, 0, 0, 21, 4,
            1, 25, 26, 30,8, 25, 24, 18, 24, 28, 8, 27, 26, 28, 4,
            9, 8, 8, 8, 8, 8, 8, 13, 8, 8, 8, 8, 8, 8,12
};
    //map for level 2
    private final short levelData2[] = {
            3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 6,
            1, 4, 3, 26, 22, 0, 27, 98, 30, 0, 19, 26, 22, 0, 4,
            1, 19, 28, 0, 17, 22, 0, 21, 0, 3, 4, 0, 25, 22, 4,
            1, 21, 0, 0, 17, 20, 0, 21, 0, 97, 20, 0, 0, 21, 4,
            1, 25, 10, 26, 16, 24, 26, 24, 26, 24, 16, 26, 26, 28, 4,
            1, 1, 0, 0, 21, 3, 14, 2, 3, 6, 21, 0, 0, 4, 4,
            1, 19, 30, 0, 21, 5, 3, 0, 6, 5, 21, 0, 27, 22, 4,
            1, 21, 0, 0, 21, 1, 0, 0, 0, 4, 21, 0, 0, 21, 4,
            1, 17, 18, 90, 4, 9, 8, 8, 8, 12, 17, 26, 18, 4, 4,
            1, 17, 20, 0, 17, 26, 26, 18, 26, 26, 20, 4, 1, 100, 4,
            1, 17, 16, 26, 4, 0, 0, 21, 0, 0, 17, 26, 16, 4, 4,
            1, 17, 28, 0, 25, 18, 18, 24, 18, 18, 28, 0, 25, 4, 4,
            1, 21, 0, 0, 0, 17, 4, 15, 17, 4, 0, 0, 0, 21, 4,
            1, 25, 26, 30,8, 25, 24, 18, 24, 28, 8, 27, 26, 28, 4,
            9, 8, 8, 8, 8, 8, 8, 13, 8, 8, 8, 8, 8, 8,12
};
    
    //map for level 3 and above
    private final short levelData3[] = {
    		19, 26, 26, 26, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22,
            21, 0, 0, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            21, 0, 0, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            21, 0, 0, 0, 17, 16, 16, 24, 16, 16, 16, 16, 16, 16, 20,
            17, 18, 18, 18, 16, 16, 20, 0, 17, 16, 16, 16, 16, 16, 20,
            17, 16, 16, 16, 16, 16, 20, 0, 17, 16, 16, 16, 16, 24, 20,
            25, 16, 16, 16, 24, 24, 28, 0, 25, 24, 24, 16, 20, 0, 21,
            1, 17, 16, 20, 0, 0, 0, 0, 0, 0, 0, 17, 20, 0, 21,
            1, 17, 16, 16, 18, 18, 22, 0, 19, 18, 18, 16, 20, 0, 21,
            1, 17, 16, 16, 16, 16, 20, 0, 17, 16, 16, 16, 20, 0, 21,
            1, 17, 16, 16, 16, 16, 20, 0, 17, 16, 16, 16, 20, 0, 21,
            1, 17, 16, 16, 16, 16, 16, 18, 16, 16, 16, 16, 20, 0, 21,
            1, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0, 21,
            1, 25, 24, 24, 24, 24, 24, 24, 24, 24, 16, 16, 16, 18, 20,
            9, 8, 8, 8, 8, 8, 8, 8, 8, 8, 25, 24, 24, 24, 28
};
  
    private final int validSpeeds[] = {1, 2, 3, 4, 6, 8};
    private final int maxSpeed = 6;

    private int currentSpeed = 3;
    private short[] screenData;
    private Timer timer;

    public Board() {
    	
        loadImages();
        initVariables();
        initBoard();
    }

    private void initBoard() {

        addKeyListener(new TAdapter());

        setFocusable(true);

        setBackground(Color.black);
        
    }
    
    private void initVariables() {

        screenData = new short[N_BLOCKS * N_BLOCKS];
        mazeColor = new Color(5, 100, 5);
        d = new Dimension(400, 400);
        ghost_x = new int[MAX_GHOSTS];
        ghost_dx = new int[MAX_GHOSTS];
        ghost_y = new int[MAX_GHOSTS];
        ghost_dy = new int[MAX_GHOSTS];
        ghostSpeed = new int[MAX_GHOSTS];
        dx = new int[4];
        dy = new int[4];
        

        timer = new Timer(40, this);
        timer.start();
        
    }

    @Override
    public void addNotify() {
        super.addNotify();

        initGame();
    }

    private void doAnim() {

        pacAnimCount--;

        if (pacAnimCount <= 0) {
            pacAnimCount = 1000000; 
            pacmanAnimPos = pacmanAnimPos + pacAnimDir;

            if (pacmanAnimPos == (PACMAN_ANIM_COUNT - 1) || pacmanAnimPos == 0) {
                pacAnimDir = -pacAnimDir;
            } 
        } 
    } 
    
    public void gameOverScreen(Graphics g){
        gOver.ded(g);
    }
    public void gameWonScreen(Graphics g){
    	g.drawImage(gameWonScreen, 0, 0, SCREEN_SIZE, SCREEN_SIZE + 1, this);
    	Sound.StopBGMusic();
    	Sound.RunBGMusic("src/resources/sounds/gameWon.wav");
    	Timer soundTimer = new Timer(3000, new ActionListener() {
    	      public void actionPerformed(ActionEvent e) {
    	      Sound.StopBGMusic(); // Stop the sound effect after 3 seconds
    	                }
    	            });
    	timer.setDelay(7500); // Set the delay between each action event to 1 millisecond
    	timer.start(); // Start the timer

    	setBackground(Color.black);
    	Font font1 = new Font("Calibri", Font.BOLD,30);
        Font font2 = new Font("Verdana", Font.PLAIN, 18);
        g.setColor(Color.green);
    	g.setFont(font1);
    	String w = "You escaped the wolves!";
        //g.drawString(w, 25 , 25);
    	g.setColor(Color.black);
    	g.setFont(font2);
        String h = "High Score: " + highscore;  
        //g.drawString(h, SCREEN_SIZE / 2 - 50 , 50);
        String z = "Press Esc to Start Over"; 
        //g.drawString(z, SCREEN_SIZE / 2 - 100 , SCREEN_SIZE - 25 );
        
        initGame();
        
    }
    private void playGame(Graphics2D g2d) {

        if (dying) {

            death();
            gameOverScreen(g2d);
            Sound.StopBGMusic();
            Sound.RunBGMusic("src/resources/sounds/gameover.wav");
        } else {
        	
        	if (invOn) {
                	pacman1 = new ImageIcon("src/resources/images/inv.png").getImage();
                    pacman2up = new ImageIcon("src/resources/images/inv.png").getImage();
                    pacman3up = new ImageIcon("src/resources/images/inv.png").getImage();
                    pacman4up = new ImageIcon("src/resources/images/inv.png").getImage();
                    pacman2down = new ImageIcon("src/resources/images/inv.png").getImage();
                    pacman3down = new ImageIcon("src/resources/images/inv.png").getImage();
                    pacman4down = new ImageIcon("src/resources/images/inv.png").getImage();
                    pacman2left = new ImageIcon("src/resources/images/inv1.png").getImage();
                    pacman3left = new ImageIcon("src/resources/images/inv1.png").getImage();
                    pacman4left = new ImageIcon("src/resources/images/inv1.png").getImage();
                    pacman2right = new ImageIcon("src/resources/images/inv2.png").getImage();
                    pacman3right = new ImageIcon("src/resources/images/inv2.png").getImage();
                    pacman4right = new ImageIcon("src/resources/images/inv2.png").getImage();

                // Reset the composite for other drawings
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f));
                
                movePacman();
                
                // Draw Pac-Man with full opacity
                drawPacman(g2d);
                
                // Set composite for ghosts to make them opaque
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                
                moveGhosts(g2d);
                checkMaze();
                checkGhostTrapCollision();
                
                // Reset the composite for other drawings
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            } else {
            	pacman1 = new ImageIcon("src/resources/images/pacman.jpg").getImage();
                pacman2up = new ImageIcon("src/resources/images/pacman.jpg").getImage();
                pacman3up = new ImageIcon("src/resources/images/pacman.jpg").getImage();
                pacman4up = new ImageIcon("src/resources/images/pacman.jpg").getImage();
                pacman2down = new ImageIcon("src/resources/images/pacman.jpg").getImage();
                pacman3down = new ImageIcon("src/resources/images/pacman.jpg").getImage();
                pacman4down = new ImageIcon("src/resources/images/pacman.jpg").getImage();
                pacman2left = new ImageIcon("src/resources/images/pacman1.png").getImage();
                pacman3left = new ImageIcon("src/resources/images/pacman1.png").getImage();
                pacman4left = new ImageIcon("src/resources/images/pacman1.png").getImage();
                pacman2right = new ImageIcon("src/resources/images/pacman2.png").getImage();
                pacman3right = new ImageIcon("src/resources/images/pacman2.png").getImage();
                pacman4right = new ImageIcon("src/resources/images/pacman2.png").getImage();
                movePacman();
                moveGhosts(g2d);
                checkMaze();
                drawPacman(g2d);
                checkGhostTrapCollision();
                
                if(level == 3) 
                {
                	gameWonScreen(g2d);
                	
                }
            }
                int pacmanTile = screenData[pacman_x / BLOCK_SIZE + N_BLOCKS * (int) (pacman_y / BLOCK_SIZE)];
                if (pacmanTile == 19) { // Cookie tile
                    score += 20;
                    screenData[pacman_x / BLOCK_SIZE + N_BLOCKS * (int) (pacman_y / BLOCK_SIZE)] = 3; // Remove the cookie
                    cookie--;
                    pointsRemaining--;
                }

                // Check if Pacman collects cookie
                if (pacmanTile == 20) { // Apple tile
                    score += 10;
                    screenData[pacman_x / BLOCK_SIZE + N_BLOCKS * (int) (pacman_y / BLOCK_SIZE)] = 4; // Remove the apple
                    apples--;
                    pointsRemaining--;
                }
                if (pacmanTile == 98) { // First Aid Kit tile
                	pacsLeft++;
                    screenData[pacman_x / BLOCK_SIZE + N_BLOCKS * (int) (pacman_y / BLOCK_SIZE)] = 2; // Remove the first aid kit
                    numFirstAidKits--;
                }
                
                if (pacmanTile == 100) { // Trap tile
                	death();
                    screenData[pacman_x / BLOCK_SIZE + N_BLOCKS * (int) (pacman_y / BLOCK_SIZE)] = 4; // Remove the trap
                }if (pacmanTile == 90) { // Trap tile
                	death();
                    screenData[pacman_x / BLOCK_SIZE + N_BLOCKS * (int) (pacman_y / BLOCK_SIZE)] = 10; // Remove the trap
                }
        	
            if(highscore < score) 
            {
            	highscore = score;
            }
        }
    }

    private void showIntroScreen(Graphics2D g2d) {

        //g2d.setColor(new Color(0, 32, 48));
        //g2d.fillRect(50, SCREEN_SIZE / 2-30, SCREEN_SIZE - 100, 100);
        //g2d.setColor(Color.white);
        //g2d.drawRect(50, SCREEN_SIZE / 2-30, SCREEN_SIZE - 100, 100);

    	String c = " Controls: ";
        String s = "Press S to start.";
        String p = "Click SPACE BAR to pause.";
        String m = "Press M to mute or unmute.";
        String e = "For 50 points, E activates invisibility.";
        String f = "For 100 points, F freezes wolves.";
        String r = "Apples are 10 points.";
        String q = "Cookies are 20 points.";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        Font mid = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = this.getFontMetrics(small);

        g2d.setColor(Color.red);
        g2d.setFont(small);
        g2d.drawString(c, (SCREEN_SIZE - metr.stringWidth(c)) / 2, 50);
        g2d.setColor(Color.white);
        g2d.drawString(r, (SCREEN_SIZE - metr.stringWidth(r)) / 2, 100);
        g2d.drawString(q, (SCREEN_SIZE - metr.stringWidth(q)) / 2, 125);
        g2d.drawString(s, (SCREEN_SIZE - metr.stringWidth(s)) / 2, SCREEN_SIZE / 2 );
        g2d.drawString(p, (SCREEN_SIZE - metr.stringWidth(p)) / 2, SCREEN_SIZE / 2 + 25);
        g2d.drawString(m, (SCREEN_SIZE - metr.stringWidth(m)) / 2, SCREEN_SIZE / 2 + 50);
        g2d.drawString(e, (SCREEN_SIZE - metr.stringWidth(e)) / 2, SCREEN_SIZE / 2 + 100);
        g2d.drawString(f, (SCREEN_SIZE - metr.stringWidth(f)) / 2, SCREEN_SIZE / 2 + 125);
        }

    private void drawScore(Graphics2D g) {

        int i;
        String s;
        String l;
        String high;
        String inv;
        
        g.setFont(smallFont);
        g.setColor(new Color(96, 128, 255));
        FontMetrics metr = this.getFontMetrics(smallFont);
        
        s = "Score: " + score;
        l= "Lvl: "+ level;
        high= "High Score: "+ highscore;
        if(invOn) 
        {
        	inv = "Inv: ON";
        }else 
        {
        	inv = "Inv: OFF";
        }
        g.drawString(l, 0, SCREEN_SIZE + 16 );
        g.drawString(inv, metr.stringWidth(l) + 15, SCREEN_SIZE + 16 );
        
        g.drawString(s, SCREEN_SIZE - (metr.stringWidth(s) + metr.stringWidth(high) + 15), SCREEN_SIZE + 16 );
        g.drawString(high, SCREEN_SIZE - metr.stringWidth(high) , SCREEN_SIZE + 16 );
        
        for (i = 0; i < pacsLeft-1; i++) {
            g.drawImage(health, SCREEN_SIZE / 2 - 70 + i*25 , SCREEN_SIZE, this);
        } 
        // Visual for lives counter
    }

    private void checkMaze() {

        int i = 0;
        // boolean finished = true;

        /* while (i < N_BLOCKS * N_BLOCKS && finished) {

            if ((screenData[i] & 48) != 0 ) {
                finished = false;
            }
            
            i++;
        } */
        pointsRemaining = 0;
        for (i = 0; i < N_BLOCKS * N_BLOCKS; i++) {
            if (screenData[i] == 19 || screenData[i] == 20) {
                pointsRemaining++;
            }
        }
        
        if (pointsRemaining == 0) {
        	
            score += 50;
            level++;

            if (N_GHOSTS < MAX_GHOSTS) {
                N_GHOSTS+=2;
            }

            if (currentSpeed < maxSpeed) {
                currentSpeed++;
            }

            initLevel();
        }
        // when player clears the map,enemies become more and faster
    }

    private void death() {

        pacsLeft--;

        if (pacsLeft == 0) {
            inGame = false;
            Sound.StopBGMusic();
            Sound.RunBGMusic("src/resources/sounds/gameover.wav");
            gameOver = true;
            level = 1;
        }
        continueLevel();
    }

    private void checkGhostTrapCollision() {
        for (int i = 0; i < N_GHOSTS; i++) {
            int ghostTile = screenData[ghost_x[i] / BLOCK_SIZE + N_BLOCKS * (int) (ghost_y[i] / BLOCK_SIZE)];
            if (ghostTile == 100) { // Trap tile
                N_GHOSTS--;
                screenData[ghost_x[i] / BLOCK_SIZE + N_BLOCKS * (int) (ghost_y[i] / BLOCK_SIZE)] = 4; // Remove the trap
                pointsRemaining--;
            }
            if (ghostTile == 90) { // Trap tile
                N_GHOSTS--;
                screenData[ghost_x[i] / BLOCK_SIZE + N_BLOCKS * (int) (ghost_y[i] / BLOCK_SIZE)] = 26; // Remove the trap
                pointsRemaining--;
            }
        }
    }

    private void moveGhosts(Graphics2D g2d) {

        short i;
        int pos;
        int count;

        for (i = 0; i < N_GHOSTS; i++) {
            if (ghost_x[i] % BLOCK_SIZE == 0 && ghost_y[i] % BLOCK_SIZE == 0) {
                pos = ghost_x[i] / BLOCK_SIZE + N_BLOCKS * (int) (ghost_y[i] / BLOCK_SIZE);

                count = 0;

                if ((screenData[pos] & 1) == 0 && ghost_dx[i] != 1) {
                    dx[count] = -1;
                    dy[count] = 0;
                    count++;
                }

                if ((screenData[pos] & 2) == 0 && ghost_dy[i] != 1) {
                    dx[count] = 0;
                    dy[count] = -1;
                    count++;
                }

                if ((screenData[pos] & 4) == 0 && ghost_dx[i] != -1) {
                    dx[count] = 1;
                    dy[count] = 0;
                    count++;
                }

                if ((screenData[pos] & 8) == 0 && ghost_dy[i] != -1) {
                    dx[count] = 0;
                    dy[count] = 1;
                    count++;
                }

                if (count == 0) {

                    if ((screenData[pos] & 15) == 15) {
                        ghost_dx[i] = 0;
                        ghost_dy[i] = 0;
                    } else {
                        ghost_dx[i] = -ghost_dx[i];
                        ghost_dy[i] = -ghost_dy[i];
                    }

                } else {

                    count = (int) (Math.random() * count);

                    if (count > 3) {
                        count = 3;
                    }

                    ghost_dx[i] = dx[count];
                    ghost_dy[i] = dy[count];
                }

            }

            ghost_x[i] = ghost_x[i] + (ghost_dx[i] * ghostSpeed[i]);
            ghost_y[i] = ghost_y[i] + (ghost_dy[i] * ghostSpeed[i]);
            drawGhost(g2d, ghost_x[i] + 1, ghost_y[i] + 1);

            if (pacman_x > (ghost_x[i] - 12) && pacman_x < (ghost_x[i] + 12)
                    && pacman_y > (ghost_y[i] - 12) && pacman_y < (ghost_y[i] + 12)
                    && inGame) {
            	if(invOn) 
            	{
            	    dying = false;
            	    Timer timer = new Timer(5000, new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            // Code to be executed after 5 seconds
                            invOn = false;
                            
                       }
                    });
            	    timer.setRepeats(false); // Only execute the timer once
            	    
            	    // Start the timer
                    timer.start();
                    
            	    // return dying = true or invOn = false when timer is 0
            	}
            	else {
            		dying = true;
            	}
            	//add invisibility feature here that checks if cloak is up and dying = false for certain amount of time
            	
            	if(frzOn) {
            	for (i = 0; i < N_GHOSTS; i++) {
            		ghostSpeed[i] = 0;
            	}
            	Timer frzTimer = new Timer(5000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        frzOn = false; // Disable after 5 seconds
                        for (int i = 0; i < N_GHOSTS; i++) {
                        	
                        ghostSpeed[i] = currentSpeed;
                        }
                    }
                });
                frzTimer.setRepeats(false);
                frzTimer.start();
            }
            }
        }
    }
    
    private void drawGhost(Graphics2D g2d, int x, int y) {

        g2d.drawImage(ghost, x, y, this);
    }

    private void movePacman() {

        int pos;
        short ch;

        if (req_dx == -pacmand_x && req_dy == -pacmand_y) {
            pacmand_x = req_dx;
            pacmand_y = req_dy;
            view_dx = pacmand_x;
            view_dy = pacmand_y;
        }

        if (pacman_x % BLOCK_SIZE == 0 && pacman_y % BLOCK_SIZE == 0) {
            pos = pacman_x / BLOCK_SIZE + N_BLOCKS * (int) (pacman_y / BLOCK_SIZE);
            ch = screenData[pos];

            if ((ch & 16) != 0) {
                screenData[pos] = (short) (ch & 15);
                score++;
            }
            

            if (req_dx != 0 || req_dy != 0) {
                if (!((req_dx == -1 && req_dy == 0 && (ch & 1) != 0)
                        || (req_dx == 1 && req_dy == 0 && (ch & 4) != 0)
                        || (req_dx == 0 && req_dy == -1 && (ch & 2) != 0)
                        || (req_dx == 0 && req_dy == 1 && (ch & 8) != 0))) {
                    pacmand_x = req_dx;
                    pacmand_y = req_dy;
                    view_dx = pacmand_x;
                    view_dy = pacmand_y;
                }
            }

            // Check for standstill
            if ((pacmand_x == -1 && pacmand_y == 0 && (ch & 1) != 0)
                    || (pacmand_x == 1 && pacmand_y == 0 && (ch & 4) != 0)
                    || (pacmand_x == 0 && pacmand_y == -1 && (ch & 2) != 0)
                    || (pacmand_x == 0 && pacmand_y == 1 && (ch & 8) != 0)) {
                pacmand_x = 0;
                pacmand_y = 0;
            }
        }
        pacman_x = pacman_x + PACMAN_SPEED * pacmand_x;
        pacman_y = pacman_y + PACMAN_SPEED * pacmand_y;
    }

    private void drawPacman(Graphics2D g2d) {

        if (view_dx == -1) {
            drawPacnanLeft(g2d);
        } else if (view_dx == 1) {
            drawPacmanRight(g2d);
        } else if (view_dy == -1) {
            drawPacmanUp(g2d);
        } else {
            drawPacmanDown(g2d);
        }
    }

    private void drawPacmanUp(Graphics2D g2d) {

        switch (pacmanAnimPos) {
            case 1:
                g2d.drawImage(pacman2up, pacman_x + 1, pacman_y + 1, this);
                break;
            case 2:
                g2d.drawImage(pacman3up, pacman_x + 1, pacman_y + 1, this);
                break;
            case 3:
                g2d.drawImage(pacman4up, pacman_x + 1, pacman_y + 1, this);
                break;
            default:
                g2d.drawImage(pacman1, pacman_x + 1, pacman_y + 1, this);
                break;
        }
    }

    private void drawPacmanDown(Graphics2D g2d) {

        switch (pacmanAnimPos) {
            case 1:
                g2d.drawImage(pacman2down, pacman_x + 1, pacman_y + 1, this);
                break;
            case 2:
                g2d.drawImage(pacman3down, pacman_x + 1, pacman_y + 1, this);
                break;
            case 3:
                g2d.drawImage(pacman4down, pacman_x + 1, pacman_y + 1, this);
                break;
            default:
                g2d.drawImage(pacman1, pacman_x + 1, pacman_y + 1, this);
                break;
        }
    }

    private void drawPacnanLeft(Graphics2D g2d) {

        switch (pacmanAnimPos) {
            case 1:
                g2d.drawImage(pacman2left, pacman_x + 1, pacman_y + 1, this);
                break;
            case 2:
                g2d.drawImage(pacman3left, pacman_x + 1, pacman_y + 1, this);
                break;
            case 3:
                g2d.drawImage(pacman4left, pacman_x + 1, pacman_y + 1, this);
                break;
            default:
                g2d.drawImage(pacman1, pacman_x + 1, pacman_y + 1, this);
                break;
        }
    }

    private void drawPacmanRight(Graphics2D g2d) {

        switch (pacmanAnimPos) {
            case 1:
                g2d.drawImage(pacman2right, pacman_x + 1, pacman_y + 1, this);
                break;
            case 2:
                g2d.drawImage(pacman3right, pacman_x + 1, pacman_y + 1, this);
                break;
            case 3:
                g2d.drawImage(pacman4right, pacman_x + 1, pacman_y + 1, this);
                break;
            default:
                g2d.drawImage(pacman1, pacman_x + 1, pacman_y + 1, this);
                break;
        }
    }

    private void drawMaze(Graphics2D g2d) {

        short i = 0;
        int x, y;
        
        for (y = 0; y < SCREEN_SIZE; y += BLOCK_SIZE) {
            for (x = 0; x < SCREEN_SIZE; x += BLOCK_SIZE) {

                g2d.setColor(mazeColor);
                g2d.setStroke(new BasicStroke(2));

                if ((screenData[i] & 1) != 0) {
                    g2d.drawLine(x, y, x, y + BLOCK_SIZE - 1);
                }

                if ((screenData[i] & 2) != 0) {
                    g2d.drawLine(x, y, x + BLOCK_SIZE - 1, y);
                }

                if ((screenData[i] & 4) != 0) {
                    g2d.drawLine(x + BLOCK_SIZE - 1, y, x + BLOCK_SIZE - 1,
                            y + BLOCK_SIZE - 1);
                }

                if ((screenData[i] & 8) != 0) {
                    g2d.drawLine(x, y + BLOCK_SIZE - 1, x + BLOCK_SIZE - 1,
                            y + BLOCK_SIZE - 1);
                }

                if ((screenData[i] & 16) != 0) {
                    g2d.setColor(dotColor);
                    g2d.fillRect(x + 11, y + 11, 2, 2);
                }
                int tile = screenData[i];

                if (tile == 20) { // Apple
                    g2d.drawImage(appleImage, x, y, this);
                } else if (tile == 19) { // Cookie
                    g2d.drawImage(cookieImage, x, y, this);
                } else if(tile == 98) {
                	g2d.drawImage(health, x, y, this);
                } else if(tile == 100 || tile == 90) {
                	g2d.drawImage(trap, x, y, this);
                }

                i++;
            }
        }
    }

    private void initGame() {

        pacsLeft = 1;
        score = 0;
        level = 3;
        N_GHOSTS = 4;
        initLevel();
        currentSpeed = 3;
        //for (int i = 0; i < N_GHOSTS; i++) {
        	//ghostSpeed[i] = currentSpeed;
        //}
    }

    private void initLevel() {

        int i;
        if(level==1)
        {
        	for (i = 0; i < N_BLOCKS * N_BLOCKS; i++) 
        	{
            screenData[i] = levelData[i];
            }
        } else if(level == 2)
        {
        	for (i = 0; i < N_BLOCKS * N_BLOCKS; i++) 
        	{
            screenData[i] = levelData2[i];
            }
        }
        else 
        {
        	for (i = 0; i < N_BLOCKS * N_BLOCKS; i++) 
        	{
            screenData[i] = levelData3[i];
            }
        }

        continueLevel();
    }

    private void continueLevel() {

        short i;
        int dx = 1;
        int random;

        for (i = 0; i < N_GHOSTS; i++) {

            ghost_y[i] = 4 * BLOCK_SIZE;
            ghost_x[i] = 7 * BLOCK_SIZE;
            ghost_dy[i] = 0;
            ghost_dx[i] = dx;
            dx = -dx;
            random = (int) (Math.random() * (currentSpeed + 1));

            if (random > currentSpeed) {
                random = currentSpeed;
            }

            ghostSpeed[i] = validSpeeds[random];
        }

        pacman_x = 7 * BLOCK_SIZE;
        pacman_y = 14 * BLOCK_SIZE;
        pacmand_x = 0;
        pacmand_y = 0;
        req_dx = 0;
        req_dy = 0;
        view_dx = -1;
        view_dy = 0;
        dying = false;
    }

    private void loadImages() {

    	appleImage = new ImageIcon("src/resources/images/appleImage.png").getImage();
    	cookieImage = new ImageIcon("src/resources/images/cookieImage.png").getImage();
    	gameOverScreen = new ImageIcon("src/resources/images/gameover.png").getImage();
    	gameWonScreen = new ImageIcon("src/resources/images/gameWon.png").getImage();
    	map1 = new ImageIcon("src/resources/images/map1.jpg").getImage();
    	//map2 = new ImageIcon("src/resources/images/map2.jpg").getImage();
    	//map3 = new ImageIcon("src/resources/images/map3.jpg").getImage();
    	trap = new ImageIcon("src/resources/images/trap.png").getImage();
       	health = new ImageIcon("src/resources/images/health.png").getImage();
       	ghost = new ImageIcon("src/resources/images/ghost.jpg").getImage();
       	pacman1 = new ImageIcon("src/resources/images/pacman.jpg").getImage();
        pacman2up = new ImageIcon("src/resources/images/pacman.jpg").getImage();
        pacman3up = new ImageIcon("src/resources/images/pacman.jpg").getImage();
        pacman4up = new ImageIcon("src/resources/images/pacman.jpg").getImage();
        pacman2down = new ImageIcon("src/resources/images/pacman.jpg").getImage();
        pacman3down = new ImageIcon("src/resources/images/pacman.jpg").getImage();
        pacman4down = new ImageIcon("src/resources/images/pacman.jpg").getImage();
        pacman2left = new ImageIcon("src/resources/images/pacman1.png").getImage();
        pacman3left = new ImageIcon("src/resources/images/pacman1.png").getImage();
        pacman4left = new ImageIcon("src/resources/images/pacman1.png").getImage();
        pacman2right = new ImageIcon("src/resources/images/pacman2.png").getImage();
        pacman3right = new ImageIcon("src/resources/images/pacman2.png").getImage();
        pacman4right = new ImageIcon("src/resources/images/pacman2.png").getImage();
    }

    @Override
    public void paintComponent(Graphics g) {
    	
    	super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        if (inGame) {
        	if(level == 1 || level == 2 )
            {
            	g2d.drawImage(map1, 0, 0, SCREEN_SIZE, SCREEN_SIZE, this);
            } else if (level == 3 && level == 4) 
            {
            	g2d.drawImage(map2, 0, 0, SCREEN_SIZE, SCREEN_SIZE, this);
            } else 
            {
            	g2d.drawImage(map3, 0, 0, SCREEN_SIZE, SCREEN_SIZE, this);
            }
            doDrawing(g2d);
        } else if(gameOver)
        {
        	gameOverScreen(g2d);
        } else {
            showIntroScreen(g2d);
        }
        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }

    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        drawMaze(g2d);
        drawScore(g2d);
        doAnim();

        if (inGame) {
            playGame(g2d);
        } else {
            showIntroScreen(g2d);
        }

        g2d.drawImage(ii, 5, 5, this);
        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }

    class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();
            
            if (inGame) {
                if (key == KeyEvent.VK_LEFT || key == 'a' || key == 'A') {
                    req_dx = -1;
                    req_dy = 0;
                } else if (key == KeyEvent.VK_RIGHT || key == 'd' || key == 'D') {
                    req_dx = 1;
                    req_dy = 0;
                } else if (key == KeyEvent.VK_UP || key == 'w' || key == 'W') {
                    req_dx = 0;
                    req_dy = -1;
                } else if (key == KeyEvent.VK_DOWN || key == 's' || key == 'S' ) {
                    req_dx = 0;
                    req_dy = 1;
                } else if (key == KeyEvent.VK_ESCAPE && timer.isRunning()) {
                	level = 1;
                    inGame = false;
                    Sound.StopBGMusic();
                } else if (key == KeyEvent.VK_SPACE) {
                    if (timer.isRunning()) {
                        timer.stop();
                        Sound.StopBGMusic();
                    } else {
                        timer.start();
                        Sound.RunBGMusic("src/resources/sounds/bgmusic.wav");
                    }
                } else if (key == 'M' || key == 'm') {
                    if (Sound.IsBGMusicPlaying()) {
                        Sound.StopBGMusic();
                    } else {
                    	Sound.RunBGMusic("src/resources/sounds/bgmusic.wav");
                    }
                } else if(key == 'q' || key == 'Q') 
                {
                	if(score >= 200)
                	{
                		score-=200;
                		PACMAN_SPEED *=2;
                    	Timer spdTimer = new Timer(5000, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                            	PACMAN_SPEED /=2; // Disable invincibility after 5 seconds
                            }
                        });
                        spdTimer.setRepeats(false);
                        spdTimer.start();
                    }
                } 
                else if(key == 'e' || key == 'E' && !invOn) {
                	if(score >= 50)
                	{
                		score-=50;
                    	invOn = true;
                    	Timer invTimer = new Timer(5000, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                invOn = false; // Disable invincibility after 5 seconds
                            }
                        });
                        invTimer.setRepeats(false);
                        invTimer.start();
                    }
                } else if(key == 'f' || key == 'F') {
                	if(score >= 100)
                	{
                		score-=100;
                	for (int i = 0; i < N_GHOSTS; i++) {
                		ghostSpeed[i] = 0;
                	}
                	frzOn = true;
                	Timer frzTimer = new Timer(5000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            frzOn = false; // Disable after 5 seconds
                            for (int i = 0; i < N_GHOSTS; i++) {
                            	
                                ghostSpeed[i] = currentSpeed;
                                }
                        }
                    });
                    frzTimer.setRepeats(false);
                    frzTimer.start();
                }
               } else if(key == 'r' || key == 'R') {
               	if(score >= 150)
               	{
               		score-=150;
               		N_GHOSTS--;
               	}
               }
            } else {
                if (key == 's' || key == 'S') {
                    inGame = true;
                    initGame();
                    Sound.RunBGMusic("src/resources/sounds/bgmusic.wav");
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

            int key = e.getKeyCode();

            if (key == Event.LEFT || key == Event.RIGHT
                    || key == Event.UP || key == Event.DOWN) {
                req_dx = 0;
                req_dy = 0;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
