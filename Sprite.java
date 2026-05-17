package mazeGame;

import java.awt.*;
import java.io.*;

public class Sprite extends Rect implements SpriteActions
{
    //-----------------------------------------------------------------------//

    int action = DN;
    boolean moving = false;

    //-----------------------------------------------------------------------//

    Animation[] animation;

    // Real image size on screen
    int drawW;
    int drawH;

    // Visual-only image shift
    int imageOffsetX;
    int imageOffsetY;

    // Extra shift only when flipped left
    int leftFlipOffsetX;

    //-----------------------------------------------------------------------//

    public Sprite(String name, int x, int y, int w, int h, int action)
    {
        super(x, y, w, h);

        this.action = action;

        this.drawW = w;
        this.drawH = h;

        this.imageOffsetX = 0;
        this.imageOffsetY = 0;
        this.leftFlipOffsetX = 0;

        loadAnimations(findSpriteFile(name));
    }

    //-----------------------------------------------------------------------//

    private String findSpriteFile(String name)
    {
        String normalPath = name + ".sprite";
        File normalFile = new File(normalPath);

        if (normalFile.exists())
        {
            return normalPath;
        }

        String playerPath = "Assets/Players/" + name + "/" + name + ".sprite";
        File playerFile = new File(playerPath);

        if (playerFile.exists())
        {
            return playerPath;
        }

        String coinPath = "Assets/Textures/Coins/Coin.sprite";
        File coinFile = new File(coinPath);

        if (name.equals("Coin") && coinFile.exists())
        {
            return coinPath;
        }

        String texturePath = "Assets/Textures/" + name + "s/" + name + ".sprite";
        File textureFile = new File(texturePath);

        if (textureFile.exists())
        {
            return texturePath;
        }

        System.out.println("SPRITE FILE NOT FOUND:");
        System.out.println("Tried: " + normalFile.getAbsolutePath());
        System.out.println("Tried: " + playerFile.getAbsolutePath());
        System.out.println("Tried: " + coinFile.getAbsolutePath());
        System.out.println("Tried: " + textureFile.getAbsolutePath());

        return normalPath;
    }

    //-----------------------------------------------------------------------//

    public void loadAnimations(String filename)
    {
        File file = new File(filename);

        try
        {
            BufferedReader input = new BufferedReader(new FileReader(file));

            int animationCount = Integer.parseInt(input.readLine());

            animation = new Animation[animationCount];

            for (int i = 0; i < animation.length; i++)
            {
                String name = input.readLine();
                int count = Integer.parseInt(input.readLine());
                int duration = Integer.parseInt(input.readLine());
                String filetype = input.readLine();

                animation[i] = new Animation(name, count, duration, filetype);
            }

            input.close();
        }
        catch (Exception x)
        {
            System.out.println("Could not load sprite file: " + filename);
            System.out.println("Absolute path: " + file.getAbsolutePath());

            x.printStackTrace();

            animation = null;
        }
    }

    //-----------------------------------------------------------------------//

    public void moveUP(int dy)
    {
        y -= dy;
        action = UP;
        moving = true;
    }

    //-----------------------------------------------------------------------//

    public void moveDN(int dy)
    {
        y += dy;
        action = DN;
        moving = true;
    }

    //-----------------------------------------------------------------------//

    public void moveLT(int dx)
    {
        x -= dx;
        action = LT;
        moving = true;
    }

    //-----------------------------------------------------------------------//

    public void moveRT(int dx)
    {
        x += dx;
        action = RT;
        moving = true;
    }

    //-----------------------------------------------------------------------//

    private boolean hasValidAnimation()
    {
        return animation != null &&
               action >= 0 &&
               action < animation.length &&
               animation[action] != null;
    }

    //-----------------------------------------------------------------------//

    private void drawFallback(Graphics g, int boxX, int boxY)
    {
        g.setColor(Color.RED);
        g.fillRect(boxX, boxY, w, h);

        g.setColor(Color.BLACK);
        g.drawRect(boxX, boxY, w, h);
    }

    //-----------------------------------------------------------------------//

    public void drawHitbox(Graphics g)
    {
        int boxX = (int)(this.x - Camera.x);
        int boxY = (int)(this.y - Camera.y);

        g.setColor(Color.GREEN);
        g.drawRect(boxX, boxY, w, h);
    }

    //-----------------------------------------------------------------------//

    public void draw(Graphics g)
    {
        int boxX = (int)(this.x - Camera.x);
        int boxY = (int)(this.y - Camera.y);

        if (!hasValidAnimation())
        {
            drawFallback(g, boxX, boxY);
            moving = false;
            return;
        }

        Image image;

        if (moving)
        {
            image = animation[action].nextImage();
        }
        else
        {
            image = animation[action].stillImage();
        }

        if (image == null)
        {
            drawFallback(g, boxX, boxY);
            moving = false;
            return;
        }

        int imageX = boxX - (drawW - w) / 2 + imageOffsetX;
        int imageY = boxY - (drawH - h) / 2 + imageOffsetY;

        if (action == LT)
        {
            g.drawImage(image, imageX + drawW + leftFlipOffsetX, imageY, -drawW, drawH, null);
        }
        else
        {
            g.drawImage(image, imageX, imageY, drawW, drawH, null);
        }

        moving = false;
    }

    //-----------------------------------------------------------------------//
}