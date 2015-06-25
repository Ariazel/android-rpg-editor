package cz.cuni.mff.rpgeditor.editor;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import cz.cuni.mff.rpgeditor.editor.MapTile.TerrainType;
import cz.cuni.mff.rpgeditor.game.MovingObject;
import cz.cuni.mff.rpgeditor.game.StationaryObject;


public class ActionController
{
	private ArrayList<Reversible> lastActions = new ArrayList<Reversible>();
	// seznam akci, aby mohly byt pripadne vraceny zpet
	int index = 0; // index v lastActions

	void addAction(Reversible a)
	{
		// pokud je index uprostred listu akci, akce za
		// posledni se pridanim nove akce ztrati
		if (lastActions.size() != index)
			lastActions = new ArrayList<Reversible>(
					lastActions.subList(0, index));

		if (index == 100)	// jde se vratit maximalne o 100 kroku zpet
			lastActions.remove(0);
		else
			++index;

		lastActions.add(a);
	}

	void removeLastAction()
	{
		lastActions.remove(lastActions.size() - 1);
		--index;
	}

	boolean isInSavedState()
	{	// mapa nebyla od nacteni upravena
		// TODO: lepsi urceni uprav v mape bez ztraty poslednich akci k vraceni
		return index == 0;
	}
	

	void undoAction()
	{
		if (index > 0)
		{
			--index;
			lastActions.get(index).reverse();

			Main.gui.mapTabbedPane.repaint();
			Main.gui.leftPanel.miniMap.repaint();
		}
	}

	void redoAction()
	{
		if (lastActions.size() > index)
		{
			++index;
			lastActions.get(index - 1).forward();

			Main.gui.mapTabbedPane.repaint();
			Main.gui.leftPanel.miniMap.repaint();
		}
	}

	Reversible getLastAction()
	{
		if (lastActions.size() < 1)
		{
			return null;
		}
		else
		{
			return lastActions.get(lastActions.size() - 1);
		}
	}


	static void newMap()
	{
		final int minSize = 20;
		final int maxSize = 200;

		final JFrame frame = new JFrame("New Map");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLayout(new GridBagLayout());
		frame.setMinimumSize(new Dimension(180, 120));
		frame.setResizable(false);

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.NONE;
		c.gridwidth = 1;
		c.weightx = 0.0;
		frame.add(new JLabel("Name: "), c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weightx = 1.0;
		c.insets = new Insets(3, 3, 3, 3);
		final JTextField name = new JTextField();
		frame.add(name, c);
		
		c.fill = GridBagConstraints.NONE;
		c.gridwidth = 1;
		c.weightx = 0.0;
		frame.add(new JLabel("Width: "), c);


		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weightx = 1.0;
		c.insets = new Insets(3, 3, 3, 3);

		final JTextField width = new JTextField();
		width.setInputVerifier(new InputVerifier()
		{
			public boolean verify(JComponent input)
			{
				JTextField tf = (JTextField) input;
				try
				{
					int x = Integer.parseInt(tf.getText());
					if (x < minSize || x > maxSize)
						return false;
					else
						return true;
				}
				catch (NumberFormatException e)
				{
					return false;
				}
			}
		});
		frame.add(width, c);


		c.fill = GridBagConstraints.NONE;
		c.gridwidth = 1;
		c.weightx = 0.0;
		frame.add(new JLabel("Height: "), c);


		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weightx = 1.0;
		c.insets = new Insets(3, 3, 3, 3);

		final JTextField height = new JTextField();
		height.setInputVerifier(new InputVerifier()
		{
			public boolean verify(JComponent input)
			{
				JTextField tf = (JTextField) input;
				try
				{
					int y = Integer.parseInt(tf.getText());
					if (y < minSize || y > maxSize)
						return false;
					else
						return true;
				}
				catch (NumberFormatException e)
				{
					return false;
				}
			}
		});
		frame.add(height, c);


		c.fill = GridBagConstraints.NONE;
		c.gridwidth = 1;
		c.weightx = 0.0;

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (width.getText().equals("") || height.getText().equals(""))
					return;

				String nameString = name.getText();
				int widthNum = Integer.parseInt(width.getText());
				int heightNum = Integer.parseInt(height.getText());

				if (widthNum < minSize || widthNum > maxSize || heightNum < minSize
						|| heightNum > maxSize)
					return;

				Map newMap = new Map(nameString, widthNum, heightNum);
				Main.gui.mapTabbedPane.add(new MapPanel(newMap));
				Main.gui.mapTabbedPane.setSelectedIndex(Main.gui.mapTabbedPane.getTabCount() - 1);
				//TODO: New map prida tab, new game vymeni stare taby za jeden novy
//				Main.gui.mapPanel.currentMap = new cz.cuni.mff.rpgeditor.game.Map(Integer
//					.parseInt(x.getText()), Integer.parseInt(y.getText()));
/*
				Main.gui.mapPanel.setSize(Main.gui.mapPanel.getPreferredSize());
				Main.gui.leftPanel.miniMap
						.paintComponent(Main.gui.leftPanel.miniMap
								.getGraphics());
				Main.gui.mapPanel.paintComponent(Main.gui.mapPanel
						.getGraphics());
*/
				frame.dispose();
			}
		});
		frame.add(okButton, c);

		c.gridwidth = GridBagConstraints.REMAINDER;

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				frame.dispose();
			}
		});
		frame.add(cancelButton, c);

		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * Otevre soubor s mapou zadanou v parametru. Informace
	 * o strukture souboru jsou u ukladani mapy.
	 * @param filepath Cesta k souboru s mapou.
	 */
	static MapPanel openMap(File f)
	{
		String filepath = f.getAbsolutePath();
		
		if (!filepath.endsWith(".rpm"))
		{	// soubor nema spravnou koncovku
			System.err.println("Chosen file is not a map file.");
			return null;
		}
		
		ObjectInputStream object_in = null;
		
		int max_stationary_id = -1, max_moving_id = -1;
		
		try
		{
			object_in = new ObjectInputStream(new FileInputStream(f));
			
			String map_name = filepath.substring(filepath.lastIndexOf(File.separator) + 1, filepath.indexOf('.'));
			int width = object_in.readInt();
			int height = object_in.readInt();
			
			Map map = new Map(map_name, width, height);
			map.filepath = filepath;
			
			int[][] object_ids = new int[width][height];
			
			for (int i = 0; i < width; ++i)
			{
				for (int j = 0; j < height; ++j)
				{
					int tile_int = object_in.readInt();					
				
					int terrain_type = tile_int >> 24;					
					map.changeTerrainType(i, j, TerrainType.values()[terrain_type]);
					
					object_ids[i][j] = tile_int & 0x00FFFFFF;	// prvni byte je teren, neni potreba si jej pamatovat
					
					int object_type = object_ids[i][j] >> 22;	// prvni dva bity po terenu jsou typ objektu
					int object_index = object_ids[i][j] & 0x003FFFFF;	// 22 bitu zprava je index objektu
					
					if (object_type == 0)
					{	// stojici objekt
						if (object_index > max_stationary_id)
						{	//je potreba zjistit nejvetsi index, podle toho
							// se urci, jak dlouho cist stojici objekty
							max_stationary_id = object_index;
						}
					}
					else if (object_type == 1)
					{	// pohyblivy objekt
						if (object_index > max_moving_id)
						{
							max_moving_id = object_index;
						}
					}
				}
			}
			
			if (object_in.readInt() != Integer.MAX_VALUE)
			{	// konec mapy/zacatek objektu je oznaceny pomoci max int
				throw new IOException("Inconsistent file: " + f.getAbsolutePath());
			}
			
			List<MapObject> stationary_objects = new ArrayList<>();
			for (int i = 0; i < max_stationary_id; ++i)
			{
				MapObject so = (MapObject)object_in.readObject();
				so.load();	// nacteni transient parametru
				stationary_objects.add(so);
			}
			
			if (object_in.readInt() != Integer.MAX_VALUE)
			{	// konec stojicich objektu je oznaceny pomoci max int
				throw new IOException("Inconsistent file: " + f.getAbsolutePath());
			}
			
			List<MapObject> moving_objects = new ArrayList<>();
			for (int i = 0; i <= max_moving_id; ++i)
			{	// narozdil od stojicich objektu jsou pohyblive objekty cislovane od 0
				MapObject mo = (MapObject)object_in.readObject();
				mo.load();	// nacteni transient parametru
				moving_objects.add(mo);
			}
			
			if (object_in.readInt() != Integer.MAX_VALUE)
			{	// konec pohybujicich se objektu je oznaceny pomoci max int
				throw new IOException("Inconsistent file: " + f.getAbsolutePath());
			}
			
			// KONEC CTENI SOUBORU, ZBYVA PRIDAT OBJEKTY NA MAPU
			
			MapPanel panel = new MapPanel(map);
			// je potreba pracovat s panelem, aby objekty
			// po vlozeni byly klikatelne
			
			for (int i = 0; i < width; ++i)
			{
				for (int j = 0; j < height; ++j)
				{
					if (object_ids[i][j] == 0)	// na policku neni zadny objekt
						continue;
					
					int object_type = object_ids[i][j] >> 22;	// typ objektu jsou prvni dva bity
					int object_index = object_ids[i][j] & 0x003FFFFF;	// zbylych 22 bitu je poradi v listu
										
					if (object_type == 0)	// na policku je stojici objekt
						panel.addMapObject(new Point(i, j), stationary_objects.get(object_index - 1));	// stojici objekty jsou cislovane od 1
					else if (object_type == 1)
						panel.addMapObject(new Point(i, j), moving_objects.get(object_index));
				}
			}
			
			return panel;
		}
		catch (IOException e)
		{
			System.err.println("Can't open map file: " + f.getAbsolutePath());
			e.printStackTrace();
			
			return null;
		} 
		catch (ClassNotFoundException e)
		{
			System.err.println("Problem while loading objects from " + f.getAbsolutePath());
			e.printStackTrace();
			
			return null;
		}
		finally
		{
			try
			{
				if (object_in != null)
					object_in.close();
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Ulozi mapu do souboru definovaneho v mape (filepath/name).
	 * Mapa se uklada do binarniho souboru, kazde policko reprezentuji ctyri byty,
	 * z toho 1 byte je typ terenu, nasledujici dva bity jsou typ objektu
	 * (stojici/pohyblivy), zbylych 22 je odkaz na objekt na policku.
	 * Na konci souboru jsou ulozene objekty.
	 * @param map Mapa, ktera se ma ulozit.
	 */
	static void saveMap(Map map)
	{
		if (map.filepath == null)
		{
			map.filepath = showMapSaveChooser();
			
			if (map.filepath == null)	// soubor nebyl vybran
				return;
		}
		
		File f = new File(map.filepath);
		ObjectOutputStream object_out = null;
		
		try
		{
			object_out = new ObjectOutputStream(new FileOutputStream(f));
			if (!f.exists())
			{
				f.createNewFile();
			}
			
			// velikost mapy v prvnich osmi bytech
			object_out.writeInt(map.getWidth());
			object_out.writeInt(map.getHeight());
			
			List<MapObject> stationary_objects = new ArrayList<>();
			List<MapObject> moving_objects = new ArrayList<>();
			
			for (int i = 0; i < map.getWidth(); ++i)
			{
				for (int j = 0; j < map.getHeight(); ++j)
				{
					MapTile tile = map.getTile(i, j);
					int terrain_type = tile.type.ordinal();
					
					int tile_code = terrain_type << 24;	// teren je na prvnich osmi bitech
					
					if (tile.map_object == null)
					{
						// kod se ulozi v soucasne podobe
					}
					else
					{
						MapObject tile_object = tile.map_object;
						if (tile_object.game_object instanceof StationaryObject)
						{	// stojici objekty nemaji temer zadne nastaveni, proto se vyplati
							// udrzovat	pole vsech objektu a kontrolovat rovnost - pokud tento
							// stojici objekt existuje jinde na mape, pouzije se stejne id
							int current_object_id = 0;
							for(MapObject o : stationary_objects)
							{	// nastavi current_object_id na pozici
								// objektu v listu
								if (o.game_object.equals(tile_object.game_object))
									break;
								else
									++current_object_id;
							}
							
							if (current_object_id == stationary_objects.size())
							{
								stationary_objects.add(tile_object);
							}
							
							tile_code += current_object_id + 1;	// nula je rezervovana pro prazdne pole
						}
						else if (tile_object.game_object instanceof MovingObject)
						{	// kazdy pohyblivy objekt muze byt unikatni, vypati se ulozit
							// vsechny pohylive objekty bez prohledavani shody
							tile_code += (1 << 22);	// oznaceni pohybliveho objektu
							tile_code += moving_objects.size();
							
							moving_objects.add(tile_object);
						}
					}
					
					object_out.writeInt(tile_code);
				}
			}
			
			// konec zapisovani terenu s odkazy na objekty
			object_out.writeInt(Integer.MAX_VALUE);
			//zacatek zapisovani objektu
			
			for (MapObject o : stationary_objects)
			{
				object_out.writeObject(o);
			}
			
			object_out.writeInt(Integer.MAX_VALUE);
			
			for (MapObject o : moving_objects)
			{
				object_out.writeObject(o);
			}
			
			object_out.writeInt(Integer.MAX_VALUE);
		}
		catch (IOException e)
		{
			System.err.println("Can't save map file: " + f.getAbsolutePath());
			e.printStackTrace();
		}
		finally
		{
			try
			{				
				if (object_out != null)
					object_out.close();
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	static void saveMapAs(Map map)
	{
		map.filepath = showMapSaveChooser();
		saveMap(map);
	}
	
	static void saveGame()
	{
		// TODO
	}
	
	static void saveGameAs()
	{
		//TODO
	}
	
	static MapPanel showMapOpenChooser()
	{
		JFileChooser fc = new JFileChooser();
		
		FileNameExtensionFilter filter = new FileNameExtensionFilter("RPG Editor Map File (.rpm)", "rpm");
		fc.setFileFilter(filter);
		
		int return_val = fc.showOpenDialog(Main.gui.mapTabbedPane);
		
		if (return_val == JFileChooser.APPROVE_OPTION)
		{
			File file = fc.getSelectedFile();
			
			return openMap(file);
		}
		
		return null;
	}
	
	/**
	 * Zobrazi file chooser, aby mohl uzivatel vybrat umisteni k ulozeni mapy.
	 * @param mapName Jmeno mapy.
	 * @return Cesta, ve ktere ma byt ulozena mapa.
	 */
	static String showMapSaveChooser()
	{
		JFileChooser fc = new JFileChooser();
		
		FileNameExtensionFilter filter = new FileNameExtensionFilter("RPG Editor Map File (.rpm)", "rpm");
		fc.setFileFilter(filter);
		
		int return_val = fc.showSaveDialog(Main.gui.mapTabbedPane);
		
		if (return_val == JFileChooser.APPROVE_OPTION)
		{
			File file = fc.getSelectedFile();
			return file.getPath();
		}
		else
		{
			return null;
		}
	}
	
	static void showMapSaveDialog(String map_name)
	{
		// TODO
	}
	
	static void gameSaveDialog(String gameName)
	{
		// TODO
	}
	
	static void changeTool(GUI.Tool toolType)
	{
		Main.gui.selectedTool = toolType;
	}
}
