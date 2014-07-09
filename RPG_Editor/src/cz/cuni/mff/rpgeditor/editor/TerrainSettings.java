package cz.cuni.mff.rpgeditor.editor;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import cz.cuni.mff.rpgeditor.game.MapTile;


/**
 * Tato nastaveni jsou zobrazena pod minimapou v leftPanelu. Jsou zobrazena ve
 * chvili, kdy uzivatel vybere nastroj pro upravu terenu.
 */
class TerrainSettings extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	MapTile.TerrainType currentTType;
	int brushSize = 1;

	JComboBox<MapTile.TerrainType> listOfTypes;
	
	public TerrainSettings()
	{
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		setOpaque(false);

		c.gridwidth = GridBagConstraints.REMAINDER;
		JLabel text = new JLabel("Terrain type:");
		add(text, c);
		
		currentTType = MapTile.TerrainType.GRASS;

		listOfTypes = new JComboBox<MapTile.TerrainType>(MapTile.TerrainType.values());
		listOfTypes.addActionListener(this);
		add(listOfTypes, c);

		text = new JLabel("Brush size:");
		add(text, c);

		JRadioButton small = new JRadioButton();
		small.setActionCommand("1");
		small.setText("Small");
		small.addActionListener(this);

		JRadioButton medium = new JRadioButton();
		medium.setActionCommand("3");
		medium.setText("Medium");
		medium.addActionListener(this);

		JRadioButton large = new JRadioButton();
		large.setActionCommand("5");
		large.setText("Large");
		large.addActionListener(this);

		JRadioButton extra_large = new JRadioButton();
		extra_large.setActionCommand("7");
		extra_large.setText("Extra-large");
		extra_large.addActionListener(this);

		small.setOpaque(false);
		medium.setOpaque(false);
		large.setOpaque(false);
		extra_large.setOpaque(false);

		ButtonGroup sizesGroup = new ButtonGroup();
		sizesGroup.add(small);
		sizesGroup.add(medium);
		sizesGroup.add(large);
		sizesGroup.add(extra_large);

		JPanel sizesPanel = new JPanel();
		sizesPanel.setOpaque(false);
		sizesPanel.setLayout(new GridLayout(4, 2));
		sizesPanel.add(small);
		sizesPanel.add(medium);
		sizesPanel.add(large);
		sizesPanel.add(extra_large);

		small.setSelected(true);

		c.gridheight = GridBagConstraints.REMAINDER;
		add(sizesPanel, c);

	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("comboBoxChanged"))
			currentTType = (MapTile.TerrainType) listOfTypes.getSelectedItem();
		else
			brushSize = Integer.parseInt(e.getActionCommand());
	}
}