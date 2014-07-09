package cz.cuni.mff.rpgeditor.game;

import java.util.ArrayList;


/**
 * Postava ovladana pocitacem, ktera hraci nechce ublizit. Kazda postava muze
 * hraci zadavat ukoly, pokud ma splneny prerekvizity.
 */
public class Ally extends MapObject
{
	String name;
	ArrayList<Quest> questsToGive;

	public Ally(String lookPath)
	{
		super(lookPath);
	}
}
