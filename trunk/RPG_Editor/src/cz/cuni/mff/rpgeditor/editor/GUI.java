package cz.cuni.mff.rpgeditor.editor;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * Slouzi k tvorbe hlavniho okna editoru. Okno obsahuje Menu, ToolBar,
 * RightPanel, MapPanel, LeftPanel a StatusBar. Kazda z komponent se stara o
 * udalosti samostatne.
 */
public class GUI
{
	// seznam barev pouzivanych v programu:
	final static Color BACKGROUND_COLOR = new Color(0xE0DFE3);
	final static Color BORDER_COLOR = new Color(0x9D9DA1);

	enum Tool
	{
		SELECTION_TOOL, TERRAIN_TOOL;
	}

	Tool selectedTool = Tool.SELECTION_TOOL;

	void createWindow()
	{
		javax.swing.SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				createAndShowGUI();
			}
		});
	}

	Menu menu = new Menu();
	ToolBar toolBar = new ToolBar();
	LeftPanel leftPanel = new LeftPanel();
	MapTabbedPane mapTabbedPane = new MapTabbedPane();
	RightTabbedPane rightTabbedPane = new RightTabbedPane();

	JSplitPane splitMapRight = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
			mapTabbedPane, rightTabbedPane);;
	// rozdeluje panel s mapou a pravy panel

	StatusBar statusBar = new StatusBar();
	
	void createAndShowGUI()
	{
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container pane = frame.getContentPane();
		pane.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		frame.setJMenuBar(menu);

		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.weightx = 1;
		pane.add(toolBar, c);

		c.weightx = 0;
		c.weighty = 1;
		c.gridwidth = 1;
		pane.add(leftPanel, c);

		c.gridwidth = GridBagConstraints.REMAINDER;
		splitMapRight.setResizeWeight(1.0);
		pane.add(splitMapRight, c);
		
		c.weightx = 1;
		c.weighty = 0;
		c.gridheight = GridBagConstraints.REMAINDER;
		pane.add(statusBar, c);

		frame.setPreferredSize(new Dimension(1024, 768));
		frame.pack();
		frame.setVisible(true);

		// nastaveni oddelovace podle preferovane sirky praveho panelu:
		splitMapRight.setDividerLocation(splitMapRight.getSize().width
				- rightTabbedPane.getPreferredSize().width - splitMapRight.getDividerSize() - 1);
	}
}

class ScrollBarListener implements ChangeListener
{

	@Override
	public void stateChanged(ChangeEvent e)
	{
		Main.gui.leftPanel.miniMap.repaint();
	}

}