package cz.cuni.mff.rpgeditor.game;

import java.awt.image.BufferedImage;


/**
 * Pohybujici se objekt ve hre - spojenec/nepritel/past
 */
public class MovingObject extends GameObject 
{
	// souradnice pohybliveho objektu na mape
	int coord_x, coord_y;
	// objekt je v blizkosti hrace a tedy aktivni (hybe se/utoci)
	boolean is_active;
	// TODO: dva skripty, jeden na pohyb a jeden na utok, reference ulozene zde
	
	public MovingObject(BufferedImage[] graphics_frames)
	{
		super(graphics_frames);
		// TODO: look_game je jeden velky obrazek s jednotlivymi natocenimi a animacemi, rozdelit do frames
	}
	
	@Override
	public GameObject createDefaultCopy()
	{
		return new MovingObject(graphics_frames);
	}
}
