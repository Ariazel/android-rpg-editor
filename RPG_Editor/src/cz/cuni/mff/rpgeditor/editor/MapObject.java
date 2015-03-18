package cz.cuni.mff.rpgeditor.editor;

import java.awt.image.BufferedImage;

import cz.cuni.mff.rpgeditor.game.GameObject;

public class MapObject
{
	BufferedImage look_on_map;
	GameObject game_object;
	public int id = -1;
	
	public MapObject(BufferedImage look_on_map, GameObject game_object)
	{
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
	
	/**
	 * Vytvori kopii tohoto objektu. Je vyuzito pri pretazeni
	 * objektu z praveho panelu na mapu.
	 */
	public MapObject createDefaultCopy()
	{		
		return new MapObject(look_on_map, game_object.createDefaultCopy());
	}
}