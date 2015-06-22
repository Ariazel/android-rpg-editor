package cz.cuni.mff.rpgeditor.game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public abstract class GameObject implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	// animovana grafika objektu ve hre
	transient public BufferedImage[] graphics_frames;
	// cesta k souboru s grafikou
	protected String graphics_filepath;
	// hrac (ne)muze projit skrz tento objekt
	public boolean has_collision;
		
	protected GameObject(String graphics_filepath)
	{
		this.graphics_filepath = graphics_filepath;
		graphics_frames = getGraphicsFramesFromFile(graphics_filepath);
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == null)
			return false;
		
		if (!o.getClass().equals(this.getClass()))
			return false;
		
		GameObject other = (GameObject)o;
		
		if (!graphics_filepath.equals(other.graphics_filepath))
			return false;
		
		if (has_collision != other.has_collision)
			return false;
		
		return true;
	}
	
	@Override
	public int hashCode()
	{
		return has_collision ? graphics_filepath.hashCode() : graphics_filepath.hashCode() + 1;
	}
	
	@Override
	public abstract Object clone();

	public void load()
	{
		graphics_frames = getGraphicsFramesFromFile(graphics_filepath);
	}
	
	private static BufferedImage[] getGraphicsFramesFromFile(String image_filepath)
	{		
		List<BufferedImage> frames = new ArrayList<>();
		ImageInputStream ciis = null;
		File image_file = new File(image_filepath);
		
		try
		{
	        ImageReader reader = (ImageReader)ImageIO.getImageReadersByFormatName("gif").next();
	        ciis = ImageIO.createImageInputStream(image_file);
	        reader.setInput(ciis, false);
	
	        int noi = reader.getNumImages(true);
	
	        for (int i = 0; i < noi; i++)
	        {
	            frames.add(reader.read(i));
	        }
		}
		catch (IOException e)
		{
			System.err.println("Error while trying to open image: " + image_filepath);
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (ciis != null)
					ciis.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
        return frames.toArray(new BufferedImage[frames.size()]);
	}
}