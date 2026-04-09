package mazeGame;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Shape;

public class ImageLayer extends ImageLoader
{
	//-----------------------------------------------------------------------//

	Image image;

	//-----------------------------------------------------------------------//

	int x;
	int y;
	int z;

	int w;
	int h;

	//-----------------------------------------------------------------------//

	public ImageLayer(String filename, int x, int y, int z, int w, int h)
	{
		image = getImage(filename);

		this.x = x;
		this.y = y;
		this.z = z;

		this.w = w;
		this.h = h;
	}

	//-----------------------------------------------------------------------//

	public void draw(Graphics g)
	{
		for (int i = 0; i < 20; i++)
		{
			g.drawImage(image, x + w * i - Camera.x / z, y - Camera.y / z, w, h, null);
		}
	}

	//-----------------------------------------------------------------------//

	public void drawTiledWall(Graphics g, int x, int y, int w, int h, int tileSize)
	{
		Shape oldClip = g.getClip();

		g.setClip(x, y, w, h);

		for (int yy = y; yy < y + h; yy += tileSize)
		{
			for (int xx = x; xx < x + w; xx += tileSize)
			{
				g.drawImage(image, xx, yy, tileSize, tileSize, null);
			}
		}

		g.setClip(oldClip);
	}
}