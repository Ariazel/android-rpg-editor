package cz.cuni.mff.rpgeditor.editor;

import java.awt.Point;


/**
 * Reprezentace mapy v editoru. Mapa je dvourozmerne pole jednotlivych policek.
 */
public class Map
{
	String name;
	int width, height;
	private MapTile[][] map;

	// id, ktere je pouzito pri pridani dalsiho objektu na mapu
	private int currentId = 0;
		
	public Map(String name, int width, int height)
	{
		this.name = name;
		this.width = width;
		this.height = height;
		
		map = new MapTile[width][height];
		for (int y = 0; y < height; ++y)
			for (int x = 0; x < width; ++x)
			{
				map[x][y] = new MapTile();
			}
	}
	
	public String getName()
	{
		return name;
	}

	public int getWidth()
	{
		return width;
	}
	
	public int getWidthInPixels()
	{
		return (int)((getWidth() - 0.5)	* MapTile.imageSizeWidth);	// -0.5 kvuli pravemu okraji
	}

	public int getHeight()
	{
		return height;
	}
	
	public int getHeightInPixels()
	{
		return (getHeight() - 1) * MapTile.imageSizeHeight / 2;
	}

	public MapTile getTile(int x, int y)
	{
		return map[x][y];
	}
	
	public MapTile getTile(Point p)
	{
		return getTile(p.x, p.y);
	}
	
	/**
	 * Prida objekt na dane policko mapy.
	 * @param x	Souradnice X na mape.
	 * @param y Souradnice Y na mape.
	 * @param object Objekt, ktery ma byt pridan.
	 * @return Prave pridany objekt s pridelenym id.
	 */
	public MapObject addMapObject(int x, int y, MapObject object)
	{
		MapTile tile = map[x][y];
		if (tile.map_object != null)
		{	// na policku uz je objekt, nelze pridat
			throw new Error("Map tile already contains object.");
		}
		else
		{
			MapObject addedObject = object.createDefaultCopy();
			
			if (!addedObject.idIsSet())
			{
				addedObject.setId(currentId++);
			}
			map[x][y].map_object = addedObject;
			
			return addedObject;
		}
	}
	
	public void removeMapObject(int x, int y)
	{
		map[x][y].map_object = null;
	}

	public void changeTerrainType(int x, int y, MapTile.TerrainType type)
	{
		map[x][y].changeTerrainType(type);
	}
}