package cz.cuni.mff.rpgeditor.game;

import java.util.ArrayList;


/**
 * Trida, ktera vytvori RPG podle vznikle hry.
 */
public class Converter
{
	void convert(Game g)
	{
		convertAllies(g.allies);
		convertEnemies(g.enemies);
		convertMaps(g.maps);
		convertQuests(g.quests);
	}

	private void convertAllies(ArrayList<Ally> allies)
	{

	}

	private void convertEnemies(ArrayList<Enemy> enemies)
	{

	}

	private void convertMaps(ArrayList<Map> maps)
	{

	}

	private void convertQuests(ArrayList<Quest> quests)
	{

	}
}
