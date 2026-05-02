package mazeGame;

import java.awt.*;
import java.util.Random;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class MazeGame extends GameBase
{
    private final int NORMAL_SPEED = 4;
    private final int HACK_SPEED = 20;

    Player s;
    MazeGenerator maze = new MazeGenerator(25, 25, 80, 20, 40, 40);

    ImageLayer layer;
    Random rand = new Random();

    ArrayList<Coin> coins = new ArrayList<Coin>();
    int score = 0;
    int coinsToCollect = 10;

    Image coinUI;

    boolean hWasDown = false;
    boolean cWasDown = false;
    boolean noClipOn = false;

    public void initialize()
    {
        Camera.setLocation(0, 0, 0);

        String[] wallTextures = {
            "images/WallTextures/wall.png",
            "images/WallTextures/wall2.jpg",
            "images/WallTextures/wall3.jpg"
        };

        String chosenWall = wallTextures[rand.nextInt(wallTextures.length)];
        layer = new ImageLayer(chosenWall, 0, 0, 1, 16, 16);

        String[] characters = {
            "Dragon",
            "Knight"
        };

        String chosenCharacter = characters[rand.nextInt(characters.length)];
        s = new Player(chosenCharacter, 60, 60, Player.LT);

        // Load coin image for score display
        coinUI = new ImageIcon("images/Coins/coin.png").getImage();

        score = 0;
        coins.clear();

        // Random amount of coins every game: 10 through 40
        coinsToCollect = rand.nextInt(31) + 10;

        placeRandomCoins(coinsToCollect);
    }

    private void placeRandomCoins(int amount)
    {
        int cellSize = maze.getCellSize();
        int startX = maze.getStartX();
        int startY = maze.getStartY();

        int cols = maze.getPixelWidth() / cellSize;
        int rows = maze.getPixelHeight() / cellSize;

        while (coins.size() < amount)
        {
            int col = rand.nextInt(cols);
            int row = rand.nextInt(rows);

            int x = startX + col * cellSize + cellSize / 2 - 14;
            int y = startY + row * cellSize + cellSize / 2 - 14;

            Coin newCoin = new Coin(x, y);

            boolean badSpot = false;

            if (newCoin.overlaps(s))
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

            if (!badSpot)
            {
                coins.add(newCoin);
            }
        }
    }

    public void inGameLoop()
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

        int speed = NORMAL_SPEED;

        if (Debug.hacksOn)
        {
            speed = HACK_SPEED;
        }

        if (pressing[_W]) s.moveUP(speed);
        if (pressing[_S]) s.moveDN(speed);
        if (pressing[_A]) s.moveLT(speed);
        if (pressing[_D]) s.moveRT(speed);

        if (!noClipOn)
        {
            for (Rect wall : maze.getWalls())
            {
                if (wall.overlaps(s))
                {
                    wall.pushes(s);
                }
            }
        }

        for (Coin coin : coins)
        {
            if (!coin.isCollected() && coin.overlaps(s))
            {
                coin.collect(s);
                score++;
            }
        }

        int camX = (int)(s.x + s.w / 2 - screenW / 2);
        int camY = (int)(s.y + s.h / 2 - screenH / 2);

        int minCamX = maze.getStartX();
        int minCamY = maze.getStartY();

        int maxCamX = maze.getStartX() + maze.getPixelWidth() - screenW;
        int maxCamY = maze.getStartY() + maze.getPixelHeight() - screenH;

        if (maxCamX < minCamX) maxCamX = minCamX;
        if (maxCamY < minCamY) maxCamY = minCamY;

        if (camX < minCamX) camX = minCamX;
        if (camY < minCamY) camY = minCamY;
        if (camX > maxCamX) camX = maxCamX;
        if (camY > maxCamY) camY = maxCamY;

        Camera.setLocation(camX, camY, 0);
    }

    public void paint(Graphics g)
    {
        g.setColor(Color.decode("#121212"));
        g.fillRect(0, 0, screenW, screenH);

        for (Rect wall : maze.getWalls())
        {
            int x = (int)(wall.x - Camera.x);
            int y = (int)(wall.y - Camera.y);

            if (x + wall.w < 0 || x > screenW || y + wall.h < 0 || y > screenH)
                continue;

            layer.drawTiledWall(g, x, y, wall.w, wall.h, 20);

            g.setColor(Color.decode("#5a5248"));
            g.drawRect(x, y, wall.w, wall.h);
        }

        for (Coin coin : coins)
        {
            coin.draw(g);
        }

        s.draw(g);

        // Coin score display on right side
        int iconX = screenW - 140;
        int iconY = 15;

        g.drawImage(coinUI, iconX, iconY, 32, 32, null);

        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString(score + "/" + coinsToCollect, iconX + 40, iconY + 25);

        if (Debug.hacksOn)
        {
            Debug.draw(g, s, screenW, screenH, maze.getWalls().size(), noClipOn);
        }
    }
}