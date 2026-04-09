package mazeGame;

import java.awt.*;
import java.util.Random;

public class MazeGame extends GameBase
{
    private final int NORMAL_SPEED = 4;
    private final int HACK_SPEED = 20;

    Player s;
    MazeGenerator maze = new MazeGenerator(25, 25, 80, 20, 40, 40);

    ImageLayer layer;
    Random rand = new Random();

    boolean hWasDown = false;
    boolean cWasDown = false;   
    boolean noClipOn = false;   // collision off/on

    public void initialize()
    {
        Camera.setLocation(0, 0, 0);

        // RANDOM WALL TEXTURE
        String[] wallTextures = {
            "images/WallTextures/wall.png",
            "images/WallTextures/wall2.jpg",
            "images/WallTextures/wall3.jpg"
        };

        String chosenWall = wallTextures[rand.nextInt(wallTextures.length)];
        layer = new ImageLayer(chosenWall, 0, 0, 1, 16, 16);

        // RANDOM CHARACTER (.sprite FILE)
        String[] characters = {
            "Dragon",
            "Knight",
            //"Princess"
        };

        String chosenCharacter = characters[rand.nextInt(characters.length)];

        s = new Player(chosenCharacter, 60, 60, Player.LT);
    }

    public void inGameLoop()
    {
        // TOGGLE HACKS
        if (pressing[_H] && !hWasDown)
        {
            Debug.hacksOn = !Debug.hacksOn;

            
            if (!Debug.hacksOn)
            {
                noClipOn = false;
            }
        }
        hWasDown = pressing[_H];

        // TOGGLE NOCLIP ONLY IF HACKS ARE ON
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

        // COLLISION ONLY WHEN NOCLIP IS OFF
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

        // CAMERA
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

        s.draw(g);

        if (Debug.hacksOn)
        {
        	Debug.draw(g, s, screenW, screenH, maze.getWalls().size(), noClipOn);
        }
    }
}