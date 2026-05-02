package mazeGame;

import java.awt.Graphics;

public class Coin extends Sprite
{
    private boolean collected;

    public Coin(int x, int y)
    {
        super("Coin", x, y, 28, 28, SpriteActions.DN);

        // coin image size on screen
        drawW = 32;
        drawH = 32;

        // visual alignment
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
