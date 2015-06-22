package cz.cuni.mff.rpgeditor.editor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;

import cz.cuni.mff.rpgeditor.game.GameObject;

public class MapObject implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	transient BufferedImage look_on_map;
	private String look_filepath;
	GameObject game_object;
	transient public int id = -1;
	
	private static int current_id = 0;	// kazdy novy objekt dostane id + 1 predchoziho
	
	public MapObject(String look_filepath, GameObject game_object)
	{
		assert(game_object != null);

		this.look_filepath = look_filepath;
		this.game_object = game_object;
		this.id = current_id++;
		
		loadLook();
	}
	
	private void loadLook()
	{
		try
		{
			look_on_map = ImageIO.read(new File(look_filepath));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Vytvori kopii tohoto objektu. Je vyuzito pri pretazeni
	 * objektu z praveho panelu na mapu.
	 */
	@Override
	public Object clone()
	{
		return new MapObject(look_filepath, (GameObject)game_object.clone());
	}
	
	/**
	 * Nacte obrazky objektu po nacteni ze souboru.
	 */
	void load()
	{
		loadLook();		
		game_object.load();
	}
}