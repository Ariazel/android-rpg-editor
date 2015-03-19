package cz.cuni.mff.rpgeditor.editor;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

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
	
	private final static String CONFIG_FILE_PATH = "cfg/map_objects.cfg";
	
	static Map<String, List<MapObject>> loadMapObjects()
	{
		Map<String, List<MapObject>> map_objects_groups = new HashMap<>();
		// struktura, ve ktere jsou ulozeny jmena zalozek + seznam objektu v pravem panelu
		
		BufferedReader br = null;
		try
		{
			br = new BufferedReader(new FileReader(CONFIG_FILE_PATH));
			String line = "";
			
			while ((line = br.readLine()) != null)
			{
				String[] line_split = line.split(";");
				
				assert(line_split.length == 4);
				// STATIONARY/MOVING, jmeno zalozky v editoru, cesta k obrazkum do editoru, cesta k obrazkum do hry
				
				String[] image_filenames = new File(line_split[3]).list();	// cesty k souborum s obrazky do editoru
				
				List<MapObject> map_objects = new ArrayList<>();
				
				for (int i = 0; i < image_filenames.length; ++i)
				{
					String file_name = image_filenames[i].substring(0, image_filenames[i].indexOf('.'));
					
					GameObject gobject_to_add;
					if (line_split[0].equals("STATIONARY"))
					{
						gobject_to_add = new StationaryObject(getGraphicsFrames(line_split[3], file_name));
					}
					else if (line_split[0].equals("MOVING"))
					{
						gobject_to_add = new MovingObject(getGraphicsFrames(line_split[3], file_name));
					}
					else
					{
						throw new DataFormatException("Wrong format of file " + CONFIG_FILE_PATH);
					}
					
					BufferedImage look_on_map = ImageIO.read(new File(line_split[2] + "/" + image_filenames[i]));
					MapObject mo = new MapObject(look_on_map, gobject_to_add);
					map_objects.add(mo);
				}
				
				map_objects_groups.put(line_split[1], map_objects);
			}
		}
		catch (IOException e)
		{
			System.err.println("Error while trying to load map object files.");
			e.printStackTrace();
		}
		catch (DataFormatException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (br != null)
					br.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		return map_objects_groups;
	}
	
	private static BufferedImage[] getGraphicsFrames(String file_path, String file_name) throws IOException
	{
		return getGraphicsFramesFromFile(new File(file_path + "/" + file_name + ".gif"));
	}
	
	private static BufferedImage[] getGraphicsFramesFromFile(File image) throws IOException
	{
		List<BufferedImage> frames = new ArrayList<>();
		
        ImageReader reader = (ImageReader)ImageIO.getImageReadersByFormatName("gif").next();
        ImageInputStream ciis = ImageIO.createImageInputStream(image);
        reader.setInput(ciis, false);   

        int noi = reader.getNumImages(true);

        for (int i = 0; i < noi; i++)
        {
            frames.add(reader.read(i));
        }
        
        return frames.toArray(new BufferedImage[frames.size()]);
	}
}
