package cz.cuni.mff.rpgeditor.editor;

import java.util.ArrayList;

import cz.cuni.mff.rpgeditor.game.Ally;
import cz.cuni.mff.rpgeditor.game.Enemy;
import cz.cuni.mff.rpgeditor.game.Map;
import cz.cuni.mff.rpgeditor.game.Quest;


/**
 * Hlavni datova struktura programu. Jsou v ni mapy, seznam spojencu a nepratel a
 * seznam vsech ukolu.
 */
public class Game
{
	String game_filepath;
	
	public ArrayList<Map> maps;
	public ArrayList<Ally> allies;
	public ArrayList<Enemy> enemies;
	public ArrayList<Quest> quests;
	
	public Game()
	{
		// TODO
		maps = new ArrayList<Map>();
		allies = new ArrayList<Ally>();
		enemies = new ArrayList<Enemy>();
		quests = new ArrayList<Quest>();
	}
}
