package mazeGame;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class MazeGame extends GameBase
{
    private final int NORMAL_SPEED = 4;
    private final int HACK_SPEED = 20;

    private final int COIN_AMOUNT = 10;
    private final int COIN_SIZE = 28;
    private final int COIN_TIME_BONUS = 5;

    Player s;
    MazeGenerator maze;

    ImageLayer layer;
    Random rand = new Random();

    ArrayList<Coin> coins = new ArrayList<Coin>();
    Coin coinCounterIcon;

    int coinsCollected = 0;

    boolean hWasDown = false;
    boolean cWasDown = false;
    boolean rWasDown = false;

    boolean noClipOn = false;

    Rect finish;
    boolean gameWon = false;
    boolean triedLockedExit = false;

    long startTime;
    int timeLimit = 90;
    boolean gameOver = false;

    public void initialize()
    {
        resetGame();
    }

    public void inGameLoop()
    {
        checkReset();

        if (gameWon || gameOver)
        {
            return;
        }

        updateTimer();
        toggleHacks();

        int speed = NORMAL_SPEED;

        if (Debug.hacksOn)
        {
            speed = HACK_SPEED;
        }

        if (pressing[_W])
        {
            s.moveUP(speed);
        }

        if (pressing[_S])
        {
            s.moveDN(speed);
        }

        if (pressing[_A])
        {
            s.moveLT(speed);
        }

        if (pressing[_D])
        {
            s.moveRT(speed);
        }

        if (!noClipOn)
        {
            checkWallCollision();
            checkFinishLine();
            keepPlayerInsideMap();
        }
        else
        {
            checkFinishLine();
        }

        checkCoinCollision();
        updateCamera();
    }

    private void resetGame()
    {
        Camera.setLocation(0, 0, 0);

        maze = new MazeGenerator(25, 25, 80, 20, 40, 40);

        String[] wallTextures = {
            "Assets/Textures/WallTextures/wall.png",
            "Assets/Textures/WallTextures/wall2.jpg",
            "Assets/Textures/WallTextures/wall3.jpg"
        };

        String chosenWall = wallTextures[rand.nextInt(wallTextures.length)];
        layer = new ImageLayer(chosenWall, 0, 0, 1, 16, 16);

        String[] characters = {
            "Dragon",
            "Knight"
        };

        String chosenCharacter = characters[rand.nextInt(characters.length)];
        s = new Player(chosenCharacter, 60, 60, Player.LT);

        finish = new Rect(
            maze.getExitX(),
            maze.getExitY(),
            maze.getWallThickness(),
            maze.getExitHeight()
        );

        coinsCollected = 0;
        coins.clear();
        placeRandomCoins(COIN_AMOUNT);

        coinCounterIcon = new Coin(0, 0);

        gameWon = false;
        gameOver = false;
        triedLockedExit = false;

        noClipOn = false;
        Debug.hacksOn = false;

        hWasDown = false;
        cWasDown = false;
        rWasDown = false;

        timeLimit = 90;
        startTime = System.currentTimeMillis();
    }

    private void checkReset()
    {
        if ((gameWon || gameOver) && pressing[_R] && !rWasDown)
        {
            resetGame();
        }

        rWasDown = pressing[_R];
    }

    private void updateTimer()
    {
        long elapsed = (System.currentTimeMillis() - startTime) / 1000;

        if (elapsed >= timeLimit)
        {
            gameOver = true;
        }
    }

    private void toggleHacks()
    {
        if (pressing[_H] && !hWasDown)
        {
            Debug.hacksOn = !Debug.hacksOn;

            if (!Debug.hacksOn)
            {
                noClipOn = false;
            }
        }

        hWasDown = pressing[_H];

        if (Debug.hacksOn && pressing[_C] && !cWasDown)
        {
            noClipOn = !noClipOn;
        }

        cWasDown = pressing[_C];
    }

    private void checkWallCollision()
    {
        for (Rect wall : maze.getWalls())
        {
            if (wall.overlaps(s))
            {
                wall.pushes(s);
            }
        }
    }

    private void checkCoinCollision()
    {
        for (Coin coin : coins)
        {
            boolean wasCollected = coin.isCollected();

            coin.collect(s);

            if (!wasCollected && coin.isCollected())
            {
                coinsCollected++;
                timeLimit += COIN_TIME_BONUS;
            }
        }
    }

    private void checkFinishLine()
    {
        triedLockedExit = false;

        if (finish.overlaps(s))
        {
            if (coinsCollected >= COIN_AMOUNT)
            {
                gameWon = true;
            }
            else
            {
                triedLockedExit = true;

                if (!noClipOn)
                {
                    finish.pushes(s);
                }
            }
        }
    }

    private void keepPlayerInsideMap()
    {
        int minX = maze.getStartX();
        int minY = maze.getStartY();

        int maxX = maze.getStartX() + maze.getPixelWidth() - s.w;
        int maxY = maze.getStartY() + maze.getPixelHeight() - s.h;

        if (s.x < minX)
        {
            s.x = minX;
        }

        if (s.y < minY)
        {
            s.y = minY;
        }

        if (s.x > maxX)
        {
            s.x = maxX;
        }

        if (s.y > maxY)
        {
            s.y = maxY;
        }
    }

    private void updateCamera()
    {
        int camX = (int)(s.x + s.w / 2 - screenW / 2);
        int camY = (int)(s.y + s.h / 2 - screenH / 2);

        int minCamX = maze.getStartX();
        int minCamY = maze.getStartY();

        int maxCamX = maze.getStartX() + maze.getPixelWidth() - screenW;
        int maxCamY = maze.getStartY() + maze.getPixelHeight() - screenH;

        if (maxCamX < minCamX)
        {
            maxCamX = minCamX;
        }

        if (maxCamY < minCamY)
        {
            maxCamY = minCamY;
        }

        if (camX < minCamX)
        {
            camX = minCamX;
        }

        if (camY < minCamY)
        {
            camY = minCamY;
        }

        if (camX > maxCamX)
        {
            camX = maxCamX;
        }

        if (camY > maxCamY)
        {
            camY = maxCamY;
        }

        Camera.setLocation(camX, camY, 0);
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Eve's Part

    private void placeRandomCoins(int amount)
    {
        coins.clear();

        int cellSize = maze.getCellSize();
        int startX = maze.getStartX();
        int startY = maze.getStartY();

        int cols = maze.getPixelWidth() / cellSize;
        int rows = maze.getPixelHeight() / cellSize;

        int attempts = 0;
        int maxAttempts = amount * 1000;

        while (coins.size() < amount && attempts < maxAttempts)
        {
            attempts++;

            int col = rand.nextInt(cols);
            int row = rand.nextInt(rows);

            int x = startX + col * cellSize + cellSize / 2 - COIN_SIZE / 2;
            int y = startY + row * cellSize + cellSize / 2 - COIN_SIZE / 2;

            Coin newCoin = new Coin(x, y);

            boolean badSpot = false;

            if (newCoin.overlaps(s))
            {
                badSpot = true;
            }

            if (finish != null && newCoin.overlaps(finish))
            {
                badSpot = true;
            }

            for (Rect wall : maze.getWalls())
            {
                if (wall.overlaps(newCoin))
                {
                    badSpot = true;
                    break;
                }
            }

            for (Coin coin : coins)
            {
                if (coin.overlaps(newCoin))
                {
                    badSpot = true;
                    break;
                }
            }

            if (!badSpot)
            {
                coins.add(newCoin);
            }
        }
    }

    @Override
    public void paint(Graphics g)
    {
        g.setColor(Color.decode("#121212"));
        g.fillRect(0, 0, screenW, screenH);

        drawMaze(g);
        drawFinishLine(g);
        drawCoins(g);

        s.draw(g);

        drawUI(g);

        if (Debug.hacksOn)
        {
            Debug.draw(g, s, screenW, screenH, maze.getWalls().size(), noClipOn);
        }
    }

    private void drawMaze(Graphics g)
    {
        for (Rect wall : maze.getWalls())
        {
            int x = (int)(wall.x - Camera.x);
            int y = (int)(wall.y - Camera.y);

            if (x + wall.w < 0 || x > screenW || y + wall.h < 0 || y > screenH)
            {
                continue;
            }

            layer.drawTiledWall(g, x, y, wall.w, wall.h, 20);

            g.setColor(Color.decode("#5a5248"));
            g.drawRect(x, y, wall.w, wall.h);
        }
    }

    private void drawFinishLine(Graphics g)
    {
        int fx = (int)(finish.x - Camera.x);
        int fy = (int)(finish.y - Camera.y);

        int squareSize = 10;

        for (int y = 0; y < finish.h; y += squareSize)
        {
            for (int x = 0; x < finish.w; x += squareSize)
            {
                boolean white = ((x / squareSize) + (y / squareSize)) % 2 == 0;

                if (coinsCollected >= COIN_AMOUNT)
                {
                    if (white)
                    {
                        g.setColor(Color.WHITE);
                    }
                    else
                    {
                        g.setColor(Color.BLACK);
                    }
                }
                else
                {
                    if (white)
                    {
                        g.setColor(Color.RED);
                    }
                    else
                    {
                        g.setColor(Color.DARK_GRAY);
                    }
                }

                g.fillRect(
                    fx + x,
                    fy + y,
                    Math.min(squareSize, finish.w - x),
                    Math.min(squareSize, finish.h - y)
                );
            }
        }

        if (coinsCollected >= COIN_AMOUNT)
        {
            g.setColor(Color.GREEN);
        }
        else
        {
            g.setColor(Color.RED);
        }

        g.drawRect(fx, fy, finish.w, finish.h);
    }

    private void drawCoins(Graphics g)
    {
        for (Coin coin : coins)
        {
            coin.draw(g);
        }
    }
    
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Gar

    private void drawUI(Graphics g)
    {
        drawTimer(g);
        drawCoinCounter(g);

        if (!gameWon && !gameOver && coinsCollected < COIN_AMOUNT)
        {
            g.setFont(new Font("Arial", Font.BOLD, 18));
            g.setColor(Color.WHITE);
            g.drawString("Collect all coins to unlock the exit", 20, screenH - 30);
        }

        if (triedLockedExit)
        {
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.setColor(Color.RED);
            g.drawString("EXIT LOCKED!", screenW / 2 - 85, screenH / 2 + 40);
        }

        if (gameOver)
        {
            g.setFont(new Font("Arial", Font.BOLD, 32));
            g.setColor(Color.RED);
            g.drawString("YOU LOSE!", screenW / 2 - 80, screenH / 2);

            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.setColor(Color.WHITE);
            g.drawString("Press R to restart", screenW / 2 - 90, screenH / 2 + 40);
        }

        if (gameWon)
        {
            g.setFont(new Font("Arial", Font.BOLD, 32));
            g.setColor(Color.GREEN);
            g.drawString("YOU WIN!", screenW / 2 - 70, screenH / 2);

            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.setColor(Color.WHITE);
            g.drawString("Press R to restart", screenW / 2 - 90, screenH / 2 + 40);
        }
    }

    private void drawTimer(Graphics g)
    {
        long elapsed = (System.currentTimeMillis() - startTime) / 1000;
        long remaining = Math.max(0, timeLimit - elapsed);

        String timerText = "Time: " + remaining;

        Font oldFont = g.getFont();
        Color oldColor = g.getColor();

        Font timerFont = new Font("Arial", Font.BOLD, 30);
        g.setFont(timerFont);

        FontMetrics fm = g.getFontMetrics();
        int textW = fm.stringWidth(timerText);

        int textX = screenW / 2 - textW / 2;
        int textY = 45;

        g.setColor(Color.WHITE);
        g.drawString(timerText, textX, textY);

        g.setFont(oldFont);
        g.setColor(oldColor);
    }

    private void drawCoinCounter(Graphics g)
    {
        String coinText = coinsCollected + "/" + COIN_AMOUNT;

        Font oldFont = g.getFont();
        Color oldColor = g.getColor();

        Font coinFont = new Font("Arial", Font.BOLD, 28);
        g.setFont(coinFont);

        FontMetrics fm = g.getFontMetrics();
        int textW = fm.stringWidth(coinText);

        int textX = screenW - textW - 25;
        int textY = 45;

        int iconX = textX - 45;
        int iconY = 17;

        coinCounterIcon.x = Camera.x + iconX;
        coinCounterIcon.y = Camera.y + iconY;

        coinCounterIcon.draw(g);

        if (coinsCollected >= COIN_AMOUNT)
        {
            g.setColor(Color.GREEN);
        }
        else
        {
            g.setColor(Color.YELLOW);
        }

        g.drawString(coinText, textX, textY);

        g.setFont(oldFont);
        g.setColor(oldColor);
    }
}