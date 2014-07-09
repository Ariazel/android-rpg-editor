package cz.cuni.mff.rpgeditor.editor;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;


/**
 * Levy panel v editoru. V teto casti editoru je minimapa.
 */
public class LeftPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	MiniMap miniMap;
	TerrainSettings terrainSettings;

	public LeftPanel()
	{
		setMinimumSize(new Dimension(150, 250));
		setLayout(new GridBagLayout());
		setOpaque(true);
		setBackground(GUI.BACKGROUND_COLOR);
		GridBagConstraints c = new GridBagConstraints();

		c.gridwidth = GridBagConstraints.REMAINDER;
		miniMap = new MiniMap();
		add(miniMap, c);

		terrainSettings = new TerrainSettings();
		add(terrainSettings, c);
		terrainSettings.setVisible(false);

		c.gridheight = GridBagConstraints.REMAINDER;
		c.weighty = 1.0;
		JPanel p = new JPanel();
		p.setOpaque(false);
		add(p, c); // aby komponenty byly nahore a zustaly tam
	}
}
