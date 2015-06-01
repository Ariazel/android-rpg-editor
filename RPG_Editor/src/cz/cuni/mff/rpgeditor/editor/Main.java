package cz.cuni.mff.rpgeditor.editor;


public class Main
{
	static GUI gui;
	static cz.cuni.mff.rpgeditor.game.Game game;

	public static void main(String[] args)
	{		
		gui = new GUI();
		gui.createWindow();
	}
}