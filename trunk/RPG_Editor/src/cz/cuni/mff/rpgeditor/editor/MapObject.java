package cz.cuni.mff.rpgeditor.editor;

import java.awt.image.BufferedImage;
import cz.cuni.mff.rpgeditor.game.GameObject;

public class MapObject
{
	BufferedImage look_on_map;
	GameObject game_object;
	
	public MapObject(BufferedImage look_on_map, GameObject game_object)
	{
		this.look_on_map = look_on_map;
		this.game_object = game_object;
	}
}