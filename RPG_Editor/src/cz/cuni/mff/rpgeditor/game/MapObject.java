package cz.cuni.mff.rpgeditor.game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public abstract class MapObject implements Cloneable
{
	public int id = -1;
	public BufferedImage look;
	
	public MapObject(String lookPath)
	{
		try
		{
			look = ImageIO.read(new File(lookPath));
		}
		catch (IOException e)
		{
			System.out.println("Cannot load image from " + lookPath);
		}
	}
	
	void setId(int id)
	{
		this.id = id;
	}
	
	boolean idIsSet()
	{
		if (id == -1)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public Object clone()
	{
		try
		{
			return super.clone();
		}
		catch (Exception e)
		{
			return null;
		}
	}
}
