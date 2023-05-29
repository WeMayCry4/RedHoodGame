public class GameEngine {
    
    //System.out.println("Why is Leon not real?");
    
    public int score;
    public boolean gameOver;
    public String gameDif;
    public int wolfAmount;
    public boolean hunterActive;
    public boolean gameState;
    
    public void initializeGame() 
    {
        //initializes variables
        score = 0;
        gameOver = false;
        gameDif = "Easy";
        wolfAmount = 3;
        hunterActive = false;
        gameState = false;
    }
    public static void main(String[] args) 
    {
        GameEngine game = new GameEngine();
        game.initializeGame();
        game.runGameLoop();
    }
    public void checkGameOver() 
    {
        //check if the game is over
        if (gameOver) 
        {
            handleGameOver();
        }
    }
    public void handleGameOver() 
    {
        //the game over state
        System.out.println("Game Over! Final Score: " + score);
        
    }
    public int getScore() 
    {
        return score;
    }
    public void setGameDifficulty(String dif) 
    {
        gameDif = dif;
        // Additional code to adjust game parameters based on difficulty
    }

    public void setWolfAmount() 
    {
        if(gameDif.equals("Easy"))
        {
            wolfAmount = 3;
        }
        else if(gameDif.equals("Medium"))
        {
            wolfAmount = 5;
        }
        else if(gameDif.equals("Hard"))
        {
            wolfAmount = 7;
        }
        
    }

    public void activateHunter() 
    {
        if(gameDif.equals("Hard"))
        {
            hunterActive = true;
        }
        else 
        {
            hunterActive = false;
        }
    }
    public void pauseGame() {
    
    }
    private void runGameLoop() 
    {
        while (!gameOver && !gameState) 
        {
            
            // Game loop logic goes here
            
            //Check if game over condition is met
            checkGameOver();
        }
        if(gameState)
        {
            //Paused Game code
        }
    }


}
