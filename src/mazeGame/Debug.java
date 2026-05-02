package mazeGame;

import java.awt.*;

public class Debug
{
    public static boolean hacksOn = false;

    public static void draw(Graphics g, Sprite s, int screenW, int screenH, int wallCount, boolean noClipOn)
    {
        s.drawHitbox(g);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 18));

        g.drawString("Dragon X: " + (int)s.x, 20, 30);
        g.drawString("Dragon Y: " + (int)s.y, 20, 55);

        g.drawString("Camera X: " + Camera.x, 20, 80);
        g.drawString("Camera Y: " + Camera.y, 20, 105);

        g.drawString("Screen W: " + screenW, 20, 130);
        g.drawString("Screen H: " + screenH, 20, 155);

        g.drawString("Walls: " + wallCount, 20, 180);
        g.drawString("Hacks: " + (hacksOn ? "ON" : "OFF"), 20, 205);
        g.drawString("Collision: " + (noClipOn ? "OFF" : "ON"), 20, 230);
    }
}