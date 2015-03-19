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

	final JFrame mainFrame = new JFrame();
	final Menu menu = new Menu();
	final ToolBar toolBar = new ToolBar();
	final LeftPanel leftPanel = new LeftPanel();
	final MapTabbedPane mapTabbedPane = new MapTabbedPane();
	final RightTabbedPane rightTabbedPane = new RightTabbedPane();

	final JSplitPane splitMapRight = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
			mapTabbedPane, rightTabbedPane);;
	// rozdeluje panel s mapou a pravy panel

	StatusBar statusBar = new StatusBar();
	
	void createAndShowGUI()
	{		
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container pane = mainFrame.getContentPane();
		pane.setLayout(new GridBagLayout());		

		GridBagConstraints c = new GridBagConstraints();

		mainFrame.setJMenuBar(menu);

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

		mainFrame.setPreferredSize(new Dimension(1024, 768));
		mainFrame.pack();
		mainFrame.setVisible(true);

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