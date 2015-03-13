package cz.cuni.mff.rpgeditor.editor;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;

import javax.imageio.ImageIO;

import cz.cuni.mff.rpgeditor.game.GameObject;
import cz.cuni.mff.rpgeditor.game.MovingObject;
import cz.cuni.mff.rpgeditor.game.StationaryObject;


/**
 * Trida slouzici k nacteni typu a obrazku objektu ze slozek do praveho panelu.
 * K tomuto ucelu je v hlavni slozce konfiguracni soubor, ve kterem jsou zapsana
 * data ke kazdemu objektu - typ (stojici/pohybujici se), cesta ke slozce
 */
class ObjectsLoader
{
	private ObjectsLoader() { }	// tato trida je staticka
	
	final static String CONFIG_FILE_PATH = "map_objects.cfg";
	
	static Map<String, List<MapObject>> loadMapObjects() throws IOException, DataFormatException
	{
		// TODO: konfiguracni soubor
		List<MapObject> map_objects = new ArrayList<>();
		
		BufferedReader br = new BufferedReader(new FileReader(CONFIG_FILE_PATH));
		GameObject game_object;
		String line = "";
		
		
		
		while ((line = br.readLine()) != null)
		{
			String[] s = line.split(";");
			assert(s.length == 4);
			
			String[] image_paths = new File(s[3]).list();
			
			BufferedImage[] images = new BufferedImage[image_paths.length];
			for (int i = 0; i < images.length; ++i)
			{
				images[i] = ImageIO.read(new File(image_paths[i]));
			}
			
			switch(s[0])
			{
			case "STATIONARY":
				game_object = new StationaryObject();
				break;
			case "MOVING":
				game_object = new MovingObject();
				break;
			default:
				throw new DataFormatException("Wrong format of file " + CONFIG_FILE_PATH);
			}
			
		}
	}
}
