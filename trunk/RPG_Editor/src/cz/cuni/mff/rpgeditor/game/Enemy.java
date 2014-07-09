package cz.cuni.mff.rpgeditor.game;


/**
 * Trida reprezentujici nepritele. Kazdy nepritel ma svou povahu, podle ktere se
 * ridi.
 */
public class Enemy extends MapObject
{
	int hp; // ...
	int positionX, positionY;
	boolean alive;
	Behavior behavior;
	
	public Enemy(String lookPath)
	{
		super(lookPath);
	}
}


class Behavior
{
	int aggresivity; // ...


	boolean shouldMove()
	{
		return true;
	}
}
