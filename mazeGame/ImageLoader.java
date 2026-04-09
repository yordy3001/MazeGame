package mazeGame;

import java.awt.*;
import javax.swing.ImageIcon;

public class ImageLoader
{
    public Image getImage(String name)
    {
        return new ImageIcon(name).getImage();
    }
}