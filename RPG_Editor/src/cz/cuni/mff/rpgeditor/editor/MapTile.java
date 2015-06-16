package cz.cuni.mff.rpgeditor.editor;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;


public class MapTile
{
	public static final int imageSizeWidth = 60;
	public static final int imageSizeHeight = 30;
	
	// Index obrazku terenu, tyto obrazky jsou ulozene u typu terenu.
	public int imageIndex;
	// Typ terenu.
	public TerrainType type;
	// Objekt na policku. Mezi tyto se radi jak nehybne, tak pohyblive objekty.
	MapObject map_object = null;

	MapTile(String name)
	{
		type = TerrainType.valueOf(name);
		Random r = new Random();
		imageIndex = r.nextInt(type.tileImages.size());
	}

	MapTile()
	{
		this("GRASS");
	}
	
	public void changeTerrainType(TerrainType type)
	{
		if (this.type != type)
		{ // pokud nejde o stejny typ terenu
			this.type = type;
			Random r = new Random();
			imageIndex = r.nextInt(type.tileImages.size());
		}
	}
	
	public java.awt.Image getImage()
	{
		return type.tileImages.get(imageIndex);
	}
	
	public enum TerrainType
	{
		GRASS ("Grass", "tiles/green"),
		DIRT ("Dirt", "tiles/dirt");
		
		String name;
		String path;
		public Color miniMapColor;
		ArrayList<BufferedImage> tileImages;

		TerrainType(String name, String path)
		{
			this.name = name;
			this.path = path;
			
			String nameOfFolder = path.substring(path.lastIndexOf('/') + 1);
			String miniFilePath = path + '/' + nameOfFolder + "_mini.gif";
			BufferedImage miniFile = openImage(miniFilePath);
			miniMapColor = new Color(miniFile.getRGB(0, 0));
			
			// nacteni vsech dostupnych obrazku terenu pro pozdejsi vykreslovani:
			String[] files = new File(path).list();
			String regex = new String(nameOfFolder + "[\\d]+\\.gif");
			tileImages = new ArrayList<BufferedImage>();
			for (String fileName : files)
			{
				if (fileName.matches(regex))
				{
					tileImages.add(openImage(path + "/" + fileName));
				}
			}
		}
		
		private BufferedImage openImage(String path)
		{
			BufferedImage image;
			
			try
			{
				image = ImageIO.read(new File(path));
			}
			catch (IOException e)
			{
				throw new Error("Cannot load image from " + path);
			}
			
			return image;

		}
		
		@Override
		public String toString()
		{
			return name;
		}
	}
}
