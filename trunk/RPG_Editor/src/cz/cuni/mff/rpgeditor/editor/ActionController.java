package cz.cuni.mff.rpgeditor.editor;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import cz.cuni.mff.rpgeditor.game.Map;


public class ActionController
{
	private ArrayList<Reversible> lastActions = new ArrayList<Reversible>();
	// seznam akci, aby mohly byt pripadne vraceny zpet
	int index = 0; // index v lastActions

	void addAction(Reversible a)
	{
		if (lastActions.size() != index)
			lastActions = new ArrayList<Reversible>(
					lastActions.subList(0, index));

		if (index == 100)
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
				Main.game.maps.add(newMap);
				Main.gui.mapTabbedPane.add(newMap);
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

	static void openMap()
	{

	}

	static void saveGame()
	{
		
	}
	
	static void saveGameAs()
	{
		
	}
	
	static void mapSaveDialog(String mapName)
	{
		
	}
	
	static void gameSaveDialog(String gameName)
	{
		
	}
	
	static void changeTool(GUI.Tool toolType)
	{
		Main.gui.selectedTool = toolType;
	}
}
