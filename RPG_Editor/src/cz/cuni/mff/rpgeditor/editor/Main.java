package cz.cuni.mff.rpgeditor.editor;


public class Main
{
	static GUI gui;
	static cz.cuni.mff.rpgeditor.game.Game game;

	public static void main(String[] args)
	{
		game = new cz.cuni.mff.rpgeditor.game.Game();
		game.maps.add(new cz.cuni.mff.rpgeditor.game.Map("map1", 77, 153));
		game.maps.add(new cz.cuni.mff.rpgeditor.game.Map("map2", 50, 50));
		game.maps.add(new cz.cuni.mff.rpgeditor.game.Map("map3", 21, 21));
		
		gui = new GUI();
		gui.createWindow();
	}
}