package cz.cuni.mff.rpgeditor.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


/**
 * Vytvori horni listu menu s moznostmi pro praci se soubory, editaci...
 */
public class Menu extends JMenuBar
{
	private static final long serialVersionUID = 1L;

	Menu()
	{
		JMenu file = new JMenu("File");

		JMenuItem item = new JMenuItem("New");
		item.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ActionController.newMap();
			}
		});
		file.add(item);

		item = new JMenuItem("Save");
		item.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ActionController.saveGame();
			}
		});
		file.add(item);

		item = new JMenuItem("Save As...");
		item.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{

			}
		});
		file.add(item);

		item = new JMenuItem("Exit");
		item.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		file.add(item);

		add(file);


		JMenu edit = new JMenu("Edit");

		item = new JMenuItem("Undo");
		item.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				MapPanel p = Main.gui.mapTabbedPane.getDisplayedMapPanel();
				p.actionController.undoAction();
			}
		});
		edit.add(item);

		item = new JMenuItem("Redo");
		item.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				MapPanel p = Main.gui.mapTabbedPane.getDisplayedMapPanel();
				p.actionController.redoAction();
			}
		});
		edit.add(item);

		add(edit);


		JMenu view = new JMenu("View");

		add(view);


		JMenu tools = new JMenu("Tools");

		add(tools);


		JMenu help = new JMenu("Help");

		add(help);
	}
}
