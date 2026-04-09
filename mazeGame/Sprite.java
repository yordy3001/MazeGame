package mazeGame;

import java.awt.*;
import java.io.*;

public class Sprite extends Rect implements SpriteActions
{
	//-----------------------------------------------------------------------//
	
	boolean physics = false;
	
	double g = 0.8;
	
	double vx;
	double vy;
	
	//-----------------------------------------------------------------------//
	
	int action = DN;
	
	boolean moving = false;
	
	//-----------------------------------------------------------------------//

	Animation[] animation;

	// real image size on screen
	int drawW;
	int drawH;

	// visual-only image shift
	int imageOffsetX;
	int imageOffsetY;

	// extra shift only when flipped left
	int leftFlipOffsetX;
	
	//-----------------------------------------------------------------------//
	
	public Sprite(String name, int x, int y, int w, int h, int action, String[] pose, int count, int duration, String filetype)
	{
		super(x, y, w, h);

		this.action = action;

		this.drawW = w;
		this.drawH = h;

		this.imageOffsetX = 0;
		this.imageOffsetY = 0;
		this.leftFlipOffsetX = 0;

		animation = new Animation[4];
		
		for (int i = 0; i < animation.length; i++)
		{
			animation[i] = new Animation(name + "_" + pose[i], count, duration, filetype);
		}
	}
	
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
		
		loadAnimations(name + ".sprite");
	}
	
	//-----------------------------------------------------------------------//
	
	public void loadAnimations(String filename)
	{
		File file = new File(filename);
		
		try
		{
			BufferedReader input = new BufferedReader(new FileReader(file));
			
			int animation_count = Integer.parseInt(input.readLine());

			animation = new Animation[animation_count];
			
			for (int i = 0; i < animation.length; i++)
			{
				String name     = input.readLine();
				int count       = Integer.parseInt(input.readLine());
				int duration    = Integer.parseInt(input.readLine());
				String filetype = input.readLine();

				animation[i] = new Animation(name, count, duration, filetype);
			}
			
			input.close();
		}
		catch (IOException x)
		{
			System.out.println("Could not load sprite file: " + filename);
			animation = null;
		}
	}
	
	//-----------------------------------------------------------------------//
	
	public void move()
	{
		x += vx;		
		y += vy;
		
		vy += g;

		if (physics == false)
		{
			vx = 0;
			vy = 0;
		}
	}
	
	//-----------------------------------------------------------------------//
	
	public void jump(int velocity)
	{
		vy = -velocity;
		moving = true;
	}
	
	//-----------------------------------------------------------------------//
	
	public void goUP(int dy)
	{
		vy = -dy;
		action = UP;
		moving = true;
	}
	
	//-----------------------------------------------------------------------//
	
	public void goDN(int dy)
	{
		vy = dy;
		action = DN;
		moving = true;
	}
	
	//-----------------------------------------------------------------------//
	
	public void goLT(int dx)
	{
		vx = -dx;
		action = LT;
		moving = true;
	}
	
	//-----------------------------------------------------------------------//
	
	public void goRT(int dx)
	{
		vx = dx;
		action = RT;
		moving = true;
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
		
		if (moving) image = animation[action].nextImage();
		else        image = animation[action].stillImage();
		
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