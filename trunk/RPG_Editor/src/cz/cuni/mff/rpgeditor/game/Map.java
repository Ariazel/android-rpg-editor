package cz.cuni.mff.rpgeditor.game;

/**
 * Reprezentace mapy. Mapa je dvourozmerne pole jednotlivych policek.
 */
public class Map
{
	String name;
	int width, height;
	private MapTile[][] map;
	
	public int currentId = 0;
	// id, ktere je pouzito pri pridani dalsiho objektu na mapu
		
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
		if (tile.object != null)
		{	// na policku uz je objekt, nelze pridat
			throw new Error("Map tile already contains object.");
		}
		else
		{
			MapObject addedObject = (MapObject)object.clone();
			
			if (!addedObject.idIsSet())
			{
				addedObject.setId(currentId++);
			}
			map[x][y].object = addedObject;
			
			return addedObject;
		}
	}
	
	public void removeMapObject(int x, int y)
	{
		map[x][y].object = null;
	}

	public void changeTerrainType(int x, int y, MapTile.TerrainType type)
	{
		map[x][y].changeTerrainType(type);
	}
}