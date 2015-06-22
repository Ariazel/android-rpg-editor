package cz.cuni.mff.rpgeditor.editor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsLoader
{
	private MapsLoader() { }	// trida MapsLoader je staticka
	
	private final static String CONFIG_FILE_PATH = "cfg/maps.cfg";
	
	/**
	 * Otevre mapy zapsane v konfiguracnim souboru maps.cfg
	 * @return List nactenych map.
	 */
	public static List<MapPanel> loadMapsOnStartup()
	{
		List<MapPanel> maps = new ArrayList<>();
		
		BufferedReader br = null;
		try
		{
			br = new BufferedReader(new FileReader(CONFIG_FILE_PATH));
			
			String line;
			while ((line = br.readLine()) != null)
			{
				maps.add(loadMapFromFile(new File(line)));
			}
		}
		catch (IOException e)
		{
			System.err.println("Error while trying to load maps.");
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
		
		return maps;
	}
	
	/**
	 * Nacte mapu ze zadaneho souboru.
	 * @param f	Soubor s mapou.
	 * @return	Mapa v editoru.
	 */
	public static MapPanel loadMapFromFile(File f)
	{
		return ActionController.openMap(f);
	}
}
