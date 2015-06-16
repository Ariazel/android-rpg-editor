package cz.cuni.mff.rpgeditor.editor;

import java.awt.image.BufferedImage;
import java.io.Serializable;

import cz.cuni.mff.rpgeditor.game.GameObject;

public class MapObject implements Serializable
{
	private static final long serialVersionUID = 4L;
	
	BufferedImage look_on_map;
	GameObject game_object;
	public int id = -1;
	
	public MapObject(BufferedImage look_on_map, GameObject game_object)
	{
		assert(game_object != null);
		
		this.look_on_map = look_on_map;
		this.game_object = game_object;
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
	
	/**
	 * Vytvori kopii tohoto objektu. Je vyuzito pri pretazeni
	 * objektu z praveho panelu na mapu.
	 */
	public MapObject createDefaultCopy()
	{		
		return new MapObject(look_on_map, game_object.createDefaultCopy());
	}
}