package mazeGame;

import java.awt.*;

public class Rect
{
	//-----------------------------------------------------------------------//
	
	double x;
	double y;
	
	int w;
	int h;
	
	boolean selected = false;
	Image img;
	
	//-----------------------------------------------------------------------//
		
	public Rect(int x, int y, int w, int h)
	{
		this.x = x;
		this.y = y;
		
		this.w = w;
		this.h = h;
	}
	
	//-----------------------------------------------------------------------//
	
	public boolean isSelected()
	{
		return selected;
	}
	
	//-----------------------------------------------------------------------//
	
	public void setSelected()
	{
		selected = true;
	}
	
	//-----------------------------------------------------------------------//
	
	public void clearSelected()
	{
		selected = false;
	}
	
	//-----------------------------------------------------------------------//
	
	public void toggle()
	{
		selected = !selected;
	}
	
	//-----------------------------------------------------------------------//
	
	public void pushLeft(Rect r)
	{
		r.x = x - r.w - 1;
	}
	
	//-----------------------------------------------------------------------//
	
	public void pushRight(Rect r)
	{
		r.x = x + w + 1;
	}
	
	//-----------------------------------------------------------------------//
	
	public void pushUp(Rect r)
	{
		r.y = y - r.h - 1;
	}
	
	//-----------------------------------------------------------------------//
	
	public void pushDown(Rect r)
	{
		r.y = y + h + 1;
	}	
	
	//-----------------------------------------------------------------------//
	
	public void pushes(Rect r)
	{
		double overlapLeft   = (r.x + r.w) - x;
		double overlapRight  = (x + w) - r.x;
		double overlapTop    = (r.y + r.h) - y;
		double overlapBottom = (y + h) - r.y;
		
		if(overlapLeft <= 0 || overlapRight <= 0 || overlapTop <= 0 || overlapBottom <= 0)
			return;
		
		double min = overlapLeft;
		int side = 0;
		
		if(overlapRight < min)
		{
			min = overlapRight;
			side = 1;
		}
		
		if(overlapTop < min)
		{
			min = overlapTop;
			side = 2;
		}
		
		if(overlapBottom < min)
		{
			min = overlapBottom;
			side = 3;
		}
		
		if(side == 0) pushLeft(r);
		else if(side == 1) pushRight(r);
		else if(side == 2) pushUp(r);
		else if(side == 3) pushDown(r);
	}
	
	//-----------------------------------------------------------------------//
	
	public boolean overlaps(Rect r)
	{
		return (x < r.x + r.w) &&
			   (x + w > r.x) &&
			   (y < r.y + r.h) &&
			   (y + h > r.y);
	}
	
	//-----------------------------------------------------------------------//
	
	public boolean contains(int mx, int my)
	{
		return (mx > x) && 
			   (mx < x + w) && 
			   (my > y) && 
			   (my < y + h);
	}
	
	//-----------------------------------------------------------------------//
	
	public void moveBy(int dx, int dy)
	{
		x += dx;
		y += dy;
	}
	
	//-----------------------------------------------------------------------//
	
	public void moveUP(int dy)
	{
		y -= dy;
	}
	
	//-----------------------------------------------------------------------//
	
	public void moveDN(int dy)
	{
		y += dy;
	}
	
	//-----------------------------------------------------------------------//
	
	public void moveLT(int dx)
	{
		x -= dx;
	}
	
	//-----------------------------------------------------------------------//
	
	public void moveRT(int dx)
	{
		x += dx;
	}
		
	//-----------------------------------------------------------------------//
	
	public void draw(Graphics g)
	{
		int x = (int)(this.x - Camera.x);
		int y = (int)(this.y - Camera.y);
		
		g.drawRect(x, y, w, h);
	}

	

	//-----------------------------------------------------------------------//
}