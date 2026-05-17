package mazeGame;

import java.awt.*;

public class Debug
{
    public static boolean hacksOn = false;

    public static void draw(Graphics g, Sprite s, int screenW, int screenH, int wallCount, boolean noClipOn)
    {
        s.drawHitbox(g);

        Font oldFont = g.getFont();
        Color oldColor = g.getColor();

        Font debugFont = new Font("Arial", Font.BOLD, 18);
        g.setFont(debugFont);

        String[] lines = {
            "DEBUG INFO",
            "Player X: " + (int)s.x,
            "Player Y: " + (int)s.y,
            "Camera X: " + (int)Camera.x,
            "Camera Y: " + (int)Camera.y,
            "Screen W: " + screenW,
            "Screen H: " + screenH,
            "Walls: " + wallCount,
            "Hacks: " + (hacksOn ? "ON" : "OFF"),
            "Collision: " + (noClipOn ? "OFF" : "ON")
        };

        int x = 20;
        int y = 35;
        int lineHeight = 25;

        int padding = 12;
        int boxW = 230;
        int boxH = lines.length * lineHeight + padding;

        g.setColor(new Color(0, 0, 0, 170));
        g.fillRoundRect(x - padding, y - 22, boxW, boxH, 10, 10);

        g.setColor(Color.WHITE);
        g.drawRoundRect(x - padding, y - 22, boxW, boxH, 10, 10);

        for (int i = 0; i < lines.length; i++)
        {
            g.drawString(lines[i], x, y + i * lineHeight);
        }

        g.setFont(oldFont);
        g.setColor(oldColor);
    }
}