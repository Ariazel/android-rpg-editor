package cz.cuni.mff.rpgeditor.game;

import java.awt.image.BufferedImage;


/**
 * Trida reprezentujici nepritele, postavu utocici na hrace.
 */
public class Enemy extends MovingObject
{
	int hp, speed; // TODO: dalsi vlastnosti
	boolean is_alive;
	
	public Enemy(BufferedImage[] graphics)
	{
		super(graphics);
	}
}
