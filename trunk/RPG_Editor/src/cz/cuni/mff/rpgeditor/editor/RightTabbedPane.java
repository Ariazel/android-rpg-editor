package cz.cuni.mff.rpgeditor.editor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cz.cuni.mff.rpgeditor.editor.RightPanel.TransferableMapObject;
import cz.cuni.mff.rpgeditor.game.MapObject;

/**
 * Komponenta pouzita v prave casti obrazovky, slouzici k zobrazeni objektu
 * vlozitelnych na mapu. Taby jsou instance tridy RightScrollPane.
 */
public class RightTabbedPane extends JTabbedPane
{
	private static final long serialVersionUID = 1L;
	
	protected RightScrollPane currentTab;
	
	RightTabbedPane()
	{
		for (Tab tab : Tab.values())
		{
			addTab(tab.name, new RightScrollPane(tab));
		}
		
		currentTab = (RightScrollPane)getComponentAt(0);
		
		addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent e)
			{
				int indexOfOpenedTab = getSelectedIndex();
				if (indexOfOpenedTab == -1)
				{	// neni otevreny zadny tab
					currentTab = null;
				}
				else
				{
					Component c = getComponentAt(indexOfOpenedTab);
					currentTab = (RightScrollPane) c;
				}
			}
		});
	}
	
	@Override
	public Dimension getPreferredSize()
	{
		int paneBorders = getWidth() - currentTab.getWidth();
		Dimension scrollPaneSize = currentTab.getPreferredSize();
		
		return new Dimension(scrollPaneSize.width + paneBorders,
				scrollPaneSize.height + paneBorders);
	}
	
	@Override
	public Dimension getMinimumSize()
	{
		int paneBorders = getWidth() - currentTab.getWidth();
		Dimension scrollPaneMinSize = currentTab.getMinimumSize();
		
		return new Dimension(scrollPaneMinSize.width + paneBorders,
				scrollPaneMinSize.height + paneBorders);
		
	}
	
	MapObject getTransferedMapObject()
	{
		TransferableMapObject o = currentTab.rightPanel.transferedMapObject;
		if (o == null)
		{
			return null;
		}
		else
		{
			try
			{
				return (MapObject)o.getTransferData(new DataFlavor(MapObject.class, "MapObject"));
			}
			catch (UnsupportedFlavorException e)
			{
				throw new Error("Cannot get transfered map object, unsupported flavor exception.");
			}
		}
	}
	
	enum Tab
	{
		ALLIES("Allies", "objects/allies", "cz.cuni.mff.rpgeditor.game.Ally"),
		ENEMIES("Enemies", "objects/enemies", "cz.cuni.mff.rpgeditor.game.Enemy"),
		TERRAIN("Terrain objects", "objects/terrain_objects", "cz.cuni.mff.rpgeditor.game.TerrainObject");
		
		String name;
		String path;
		Class<? extends MapObject> mapObject;
		
		@SuppressWarnings("unchecked")
		Tab(String name, String path, String objectClassName)
		{
			this.name = name;
			this.path = path;
			try
			{
				mapObject = (Class<MapObject>) Class.forName(objectClassName);
			}
			catch (ClassNotFoundException e)
			{
				throw new Error("Class" + objectClassName + "not found.");
			}
		}
	}
}
