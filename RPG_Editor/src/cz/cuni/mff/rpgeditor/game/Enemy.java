package cz.cuni.mff.rpgeditor.game;


/**
 * Trida reprezentujici nepritele, postavu utocici na hrace.
 */
public class Enemy extends MovingObject
{
	private static final long serialVersionUID = 1L;
	int hp, speed; // TODO: dalsi vlastnosti + skripty utoku
	boolean is_alive;
	
	public Enemy(String graphics_filepath)
	{
		super(graphics_filepath);
	}
}
