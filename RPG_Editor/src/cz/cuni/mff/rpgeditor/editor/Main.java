package cz.cuni.mff.rpgeditor.editor;

import java.io.IOException;


public class Main
{
	static GUI gui;
	static cz.cuni.mff.rpgeditor.game.Game game;

	public static void main(String[] args) throws IOException
	{		
/*		gui = new GUI();
		gui.createWindow();
*/
		Map map = new Map("jmeno", 20, 65);
		map.filepath = "C:/Users/Jura/Desktop";
		ActionController.saveMap(map);
	}
}