package cz.cuni.mff.rpgeditor.game;

import java.awt.image.BufferedImage;
import java.util.ArrayList;


/**
 * Postava ovladana pocitacem, ktera hraci nechce ublizit. Kazda postava muze
 * hraci zadavat ukoly, pokud ma splneny prerekvizity.
 */
public class Ally extends MovingObject
{
	String name;
	ArrayList<Quest> questsToGive;

	public Ally(BufferedImage look)
	{
		super(look);
	}
}
