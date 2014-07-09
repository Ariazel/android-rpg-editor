package cz.cuni.mff.rpgeditor.editor;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;

import cz.cuni.mff.rpgeditor.game.Map;
import cz.cuni.mff.rpgeditor.game.MapObject;
import cz.cuni.mff.rpgeditor.game.MapTile.TerrainType;


/**
 * Rozhrani, ktere rozsiruji vsechny typy akci na mape. Mezi tyto akce patri
 * zmena terenu, pridani, posun, ci smazani objektu a uprava objektu.
 */
public interface Reversible
{
	public void forward();

	public void reverse();

	public boolean changedSomething();
}


/**
 * Trida reprezentuje akci zmeny terenu na mape. Pokud byl pouzit nastroj na
 * zmenu typu terenu, vznikne instance teto tridy a zaridi zmenu terenu.
 */
class TerrainChange implements Reversible
{
	class TileTerrainChange
	{
		int x, y; // souradnice, na kterych se zmenil teren
		TerrainType oldType; // stary typ terenu na danem policku

		protected TileTerrainChange(TerrainType oldType, int x, int y)
		{
			this.oldType = oldType;
			this.x = x;
			this.y = y;
		}
	}

	ArrayList<TileTerrainChange> changes = new ArrayList<TileTerrainChange>();
	TerrainType newType;	// typ terenu, na ktery jsou policka menena, tento typ
							// je stejny pro vsechna policka

	TerrainChange(LinkedList<Point> indices)
	{
		newType = Main.gui.leftPanel.terrainSettings.currentTType;
		addTiles(indices);
		
		MapPanel p = Main.gui.mapTabbedPane.getDisplayedMapPanel();
		p.actionController.addAction(this);
	}

	@Override
	public void forward()
	{
		partForward(0, changes.size());
	}

	@Override
	public void reverse()
	{
		Map mapToChange = Main.gui.mapTabbedPane.getDisplayedMap();

		if (mapToChange != null)
		{
			for (int i = 0; i < changes.size(); ++i)
			{
				 mapToChange.changeTerrainType(
						changes.get(i).x, changes.get(i).y,	changes.get(i).oldType);
			}
		}
	}

	/**
	 * Zmeni cast poli z teto akce.
	 * 
	 * @param from
	 *            Od ktereho indexu se maji menit pole.
	 * @param to
	 *            Do ktereho indexu se maji menit pole.
	 */
	private void partForward(int from, int to)
	{
		Map mapToChange = Main.gui.mapTabbedPane.getDisplayedMap();

		if (mapToChange != null)
		{
			for (int i = from; i < to; ++i)
			{
				 mapToChange.changeTerrainType(
						 	changes.get(i).x, changes.get(i).y,	newType);
			}
		}
	}

	/**
	 * Prida policka k teto akci a zmeni je na nove. Tato metoda je zavolana,
	 * pokud uzivatel pri zmene terenu tahne mysi pres vice poli.
	 * 
	 * @param oldTiles
	 *            Stare typy terenu na polich.
	 * @param newTile
	 *            Novy typ terenu na polich.
	 * @param indices
	 *            Indexy poli na mape.
	 */
	public void addTiles(LinkedList<Point> indices)
	{
		Map mapToChange = Main.gui.mapTabbedPane.getDisplayedMap();

		int from = changes.size();
		if (mapToChange != null)
		{
			for (Point p : indices)
			{
				cz.cuni.mff.rpgeditor.game.MapTile tile = mapToChange.getTile(p.x, p.y);
				TileTerrainChange ttc = new TileTerrainChange(tile.type, p.x, p.y);
	
				if (!ttc.oldType.equals(newType))
					if (!changes.contains(ttc))
						changes.add(ttc);
			}
	
			partForward(from, changes.size());
		}
	}

	@Override
	public boolean changedSomething()
	{
		return changes.size() != 0;
	}
}


/**
 * Trida reprezentuje akci pridani noveho objektu na mapu. Pokud ma na mapu byt
 * pridan novy objekt, vznikne instance teto tridy a vlozi jej.
 */
class ObjectAddition implements Reversible
{
	Point position;
	MapObject addedObject;
	private boolean changedSomething = false;

	ObjectAddition(Point position, cz.cuni.mff.rpgeditor.game.MapObject addedObject)
	{
		this.position = position;
		this.addedObject = addedObject;
		forward();
		
		if (changedSomething())
		{
			MapPanel p = Main.gui.mapTabbedPane.getDisplayedMapPanel();
			p.actionController.addAction(this);
		}
	}

	@Override
	public void forward()
	{
		MapPanel p = Main.gui.mapTabbedPane.getDisplayedMapPanel();
		changedSomething = p.addMapObject(position, addedObject);
	}

	@Override
	public void reverse()
	{		
		MapPanel p = Main.gui.mapTabbedPane.getDisplayedMapPanel();
		p.removeMapObject(position);
	}

	@Override
	public boolean changedSomething()
	{
		return changedSomething;
	}
}


/**
 * Trida reprezentuje akci premisteni objektu na mape. Pokud ma byt objekt
 * premisten, vznikne instance teto tridy a premisti jej.
 */
class ObjectMovement implements Reversible
{
	Point oldPosition, newPosition;
	MapObject movedObject;
	boolean changedSomething = false;

	public ObjectMovement(Point oldPosition, Point newPosition, MapObject movedObject)
	{
		this.oldPosition = oldPosition;
		this.newPosition = newPosition;
		this.movedObject = movedObject;
		
		forward();
		
		if (changedSomething())
		{
			MapPanel p = Main.gui.mapTabbedPane.getDisplayedMapPanel();
			p.actionController.addAction(this);
		}
	}

	@Override
	public void forward()
	{		
		MapPanel p = Main.gui.mapTabbedPane.getDisplayedMapPanel();
		
		changedSomething = (p.removeMapObject(oldPosition) != null);
		changedSomething &= p.addMapObject(newPosition, movedObject);
		// pokud nevysla nektera z techto dvou operaci, je potreba vratit mapu
		// do puvodniho stavu:
		if (!changedSomething)
		{
			p.addMapObject(oldPosition, movedObject);
		}
	}

	@Override
	public void reverse()
	{
		MapPanel p = Main.gui.mapTabbedPane.getDisplayedMapPanel();
		p.removeMapObject(newPosition);
		p.addMapObject(oldPosition, movedObject);
	}

	@Override
	public boolean changedSomething()
	{
		return changedSomething;
	}
}


/**
 * Trida reprezentuje akci smazani objektu z mapy. Pokud ma byt objekt smazan,
 * vznikne instance teto tridy a smaze jej.
 */
class ObjectRemoval implements Reversible
{
	Point position;
	MapObject removedObject;
	boolean changedSomething = false;

	public ObjectRemoval(Point position)
	{
		this.position = position;
		
		forward();
		
		if (changedSomething())
		{
			MapPanel p = Main.gui.mapTabbedPane.getDisplayedMapPanel();
			p.actionController.addAction(this);
		}
	}

	@Override
	public void forward()
	{
		MapPanel p = Main.gui.mapTabbedPane.getDisplayedMapPanel();
		removedObject = p.removeMapObject(position);
		
		changedSomething = (removedObject != null);
	}

	@Override
	public void reverse()
	{
		MapPanel p = Main.gui.mapTabbedPane.getDisplayedMapPanel();
		p.addMapObject(position, removedObject);
	}

	@Override
	public boolean changedSomething()
	{
		return changedSomething;
	}
}


class ObjectEdited implements Reversible
{
	// TODO: ALL
	@Override
	public void forward()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void reverse()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean changedSomething()
	{
		return false;
		// TODO Auto-generated method stub

	}

}