package cz.cuni.mff.rpgeditor.game;

import java.util.ArrayList;


/**
 * Postava ovladana pocitacem, ktera hraci nechce ublizit. Kazda postava muze
 * hraci zadavat ukoly, pokud ma splneny prerekvizity.
 */
public class Ally extends MovingObject
{
	private static final long serialVersionUID = 1L;
	String name;
	ArrayList<Quest> questsToGive;	
	
	public Ally(String graphics_filepath)
	{
		super(graphics_filepath);
	}
}
