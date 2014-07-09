package cz.cuni.mff.rpgeditor.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;


/**
 * Lista umistena v editoru primo pod menu. Umoznuje rychle vykonat
 * nejpouzivanejsi akce - vytvoreni nove hry, ulozeni, nacteni, konverze...
 */
public class ToolBar extends JToolBar
{
	private static final long serialVersionUID = 1L;

	ToolBar()
	{
		setFloatable(false); // nelze pretahovat
		setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(
				0x9D9DA1)));

		setBackground(GUI.BACKGROUND_COLOR);

		createButton("New Map", "Create a new map", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ActionController.newMap();
			}
		});

		createButton("Open Map", "Open a map", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ActionController.openMap();
			}
		});

		createButton("Save Game", "Save the game", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ActionController.saveGame();
			}
		});

		add(new JToolBar.Separator());

		createButton("Undo", "Undo the last action", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				MapPanel p = Main.gui.mapTabbedPane.getDisplayedMapPanel();
				p.actionController.undoAction();
			}
		});

		createButton("Redo", "Redo the previously undone action",
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						MapPanel p = Main.gui.mapTabbedPane.getDisplayedMapPanel();
						p.actionController.redoAction();
					}
				});

		add(new JToolBar.Separator());

		createButton("Toggle Tile Grid", "Show or hide the tile grid",
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						Main.gui.mapTabbedPane.swapGridVisibility();
					}
				});

		add(new JToolBar.Separator());

		createButton("Selection Tool", "Select and manipulate objects",
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						ActionController.changeTool(GUI.Tool.SELECTION_TOOL);
						Main.gui.leftPanel.terrainSettings.setVisible(false);
					}
				});

		createButton("Terrain Tool", "Paint terrain", new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ActionController.changeTool(GUI.Tool.TERRAIN_TOOL);
				Main.gui.leftPanel.terrainSettings.setVisible(true);
			}
		});

		add(new JToolBar.Separator());

		createButton("Convert", "Convert game into apk file",
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						Main.game.convert();
					}
				});


		setMinimumSize(new Dimension(50, 22));
	}

	private void createButton(String name, String tooltip, ActionListener a)
	{
		name = name.replace(' ', '_');
		name = name.toLowerCase();
		ImageIcon icon = new ImageIcon("icons/" + name + ".gif");
		MyJButton b = new MyJButton(icon);
		b.setPreferredSize(new Dimension(icon.getIconWidth(), icon
				.getIconHeight()));
		b.setName(name.replace('_', ' '));
		b.setToolTipText(tooltip);
		b.addMouseListener(new ButtonToolTipper());
		b.addActionListener(a);
		add(b);
	}


	
	class ButtonToolTipper implements MouseListener
	{
		// metody MouseListeneru:
	@Override
		public void mouseClicked(MouseEvent e)
		{
		}
	
		@Override
		public void mouseEntered(MouseEvent e)
		{
			Main.gui.statusBar.setCurrentStatus(((MyJButton) (e.getComponent()))
					.getLongToolTipText());
		}
	
		@Override
		public void mouseExited(MouseEvent arg0)
		{
			Main.gui.statusBar.setCurrentStatus(null);
		}
	
		@Override
		public void mousePressed(MouseEvent arg0)
		{
		}
	
		@Override
		public void mouseReleased(MouseEvent arg0)
		{
		}
	}
}


class MyJButton extends JButton
{
	private static final long serialVersionUID = 1L;

	MyJButton(ImageIcon icon)
	{
		super(icon);
	}

	@Override
	public String getToolTipText(MouseEvent e)
	{
		return super.getName();
	}

	public String getLongToolTipText()
	{
		return super.getToolTipText();
	}
}