package mazeGame;

import java.awt.*;


public class Animation extends ImageLoader
{
	//-----------------------------------------------------------------------//

	Image[] image;
	
	int current = 0;
	
	int duration;
	int delay;
	
	//-----------------------------------------------------------------------//

	public Animation(String name, int count, int duration, String filetype)
	{
		image = new Image[count];
		
		this.duration = duration;
		
		delay = duration;
		
		for(int i = 0; i < image.length; i++)
		{
			image[i] = getImage(name + i + "."  + filetype);
		}
	}	
	
	//-----------------------------------------------------------------------//
    // ******************************* NEW ********************************* //
	//-----------------------------------------------------------------------//

	public Animation(String name, int count, int duration, String filetype, int start)
	{
		image = new Image[count];
		
		this.duration = duration;
		
		delay = duration;
		
		for(int i = start; i < image.length + start; i++)
		{
			image[i] = getImage(name + i + "."  + filetype);
		}
	}	
	
	//-----------------------------------------------------------------------//

	public Image stillImage()
	{
		return image[0];
	}
	
	//-----------------------------------------------------------------------//
	
	public Image nextImage()
	{
		delay--;
		
		if(delay == 0)
		{
			if( current == image.length-1)   current = 1;
			else                             current++;
			
			delay = duration;
		}
				
		return image[current];
	}
	
	//-----------------------------------------------------------------------//

}