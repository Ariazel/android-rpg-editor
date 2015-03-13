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
	BufferedImage[] frames;
	
	public MovingObject(BufferedImage look)
	{
		super(look);
	}
}
