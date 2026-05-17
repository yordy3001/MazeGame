package mazeGame;

import java.awt.Color;
import java.awt.Graphics;

public class Coin extends Sprite
{
    public static final int SIZE = 28;

    private boolean collected;

    public Coin(int x, int y)
    {
        super("Coin", x, y, SIZE, SIZE, SpriteActions.DN);

        drawW = 32;
        drawH = 32;

        imageOffsetX = 0;
        imageOffsetY = 0;
        leftFlipOffsetX = 0;

        collected = false;
    }

    public boolean isCollected()
    {
        return collected;
    }

    public void collect(Player p)
    {
        if (!collected && overlaps(p))
        {
            collected = true;
        }
    }

    public void draw(Graphics g)
    {
        if (!collected)
        {
            moving = true;
            super.draw(g);
        }
    
    }
}