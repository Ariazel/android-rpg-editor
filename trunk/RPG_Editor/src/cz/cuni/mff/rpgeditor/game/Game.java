package cz.cuni.mff.rpgeditor.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.DataFormatException;


/**
 * Hlavni datova struktura programu. Jsou v ni mapy, seznam spojencu a nepratel a
 * seznam vsech ukolu.
 */
public class Game
{
	public ArrayList<Map> maps;
	public ArrayList<Ally> allies;
	public ArrayList<Enemy> enemies;
	public ArrayList<Quest> quests;

	public static void main(String[] args) throws IOException,
			DataFormatException
	{
		Game game = new Game();
		Loader l = new Loader();
		game.quests = l.loadQuests();
		game.allies = l.loadAllies(game.quests);
	}
	
	public Game()
	{
		maps = new ArrayList<Map>();
		allies = new ArrayList<Ally>();
		enemies = new ArrayList<Enemy>();
		quests = new ArrayList<Quest>();
	}

	public void convert()
	{
		Converter c = new Converter();
		c.convert(this);
	}
}
