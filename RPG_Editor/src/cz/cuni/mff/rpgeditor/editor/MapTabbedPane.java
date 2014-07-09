package cz.cuni.mff.rpgeditor.editor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicButtonUI;

import cz.cuni.mff.rpgeditor.game.Map;


/**
 * Panel umisteny na stredu editoru. Je v nem vykreslena mapa s objekty, ktere
 * uzivatel pridal - pratele, nepratele a teren.
 */
public class MapTabbedPane extends JTabbedPane
{
	private static final long serialVersionUID = 1L;	

	protected MapScrollPane currentTab; // tab s prave zobrazovanou mapou
	
	MapTabbedPane()
	{
		for (Map map : Main.game.maps)
		{
			add(map);
		}

		if (this.getTabCount() == 0)
		{
			currentTab = null;
		}
		else
		{
			currentTab = (MapScrollPane)getComponentAt(0);
		}
		
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
					currentTab = (MapScrollPane) c;
				}
	
				Main.gui.leftPanel.miniMap.repaint();
			}
		});
	}

	Map getDisplayedMap()
	{ // prave zobrazena mapa
		if (currentTab != null)
		{
			return currentTab.mapPanel.map;
		}
		else
			return null;
	}

	MapPanel getDisplayedMapPanel()
	{ // MapPanel s prave zobrazenou mapou
		if (currentTab != null)
		{
			return currentTab.mapPanel;
		}
		else
			return null;
	}
	
	protected boolean gridVisible = false;
	// urcuje, zda je viditelna mrizka terenu

	public void swapGridVisibility()
	{
		if (currentTab != null && currentTab.mapPanel != null)
		{
			gridVisible = !gridVisible;
			currentTab.mapPanel.repaint();
		}
	}
	
	public void add(Map map)
	{
		MapScrollPane mapScrollPane = new MapScrollPane(map);
		addTab(map.getName(), mapScrollPane);
		
		int index = indexOfTab(map.getName());
		setTabComponentAt(index, new ClosableTabComponent(map.getName()));
		// prida tabu krizek k zavreni
	}
}

/**
 * Panel umisteny do hlavy zalozky. Pridava ke jmenu zaviraci tlacitko.
 */
class ClosableTabComponent extends JPanel
{
	private static final long serialVersionUID = 1L;
	String name;
	
	ClosableTabComponent(String name)
	{		
		this.name = name;
		
		setLayout(new GridBagLayout());
		setOpaque(false);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		JLabel titleLabel = new JLabel(name + "  ");
		add(titleLabel, gbc);

		gbc.gridx++;
		gbc.weightx = 0;
		gbc.anchor = GridBagConstraints.EAST;
		JButton closeButton = createTabCloseButton();
		add(closeButton, gbc);
		
		addMouseListener(panelMouseListener);
	}
	
	public JButton createTabCloseButton()
	{
		JButton closeButton = new JButton(new ImageIcon("icons/close.gif"));
		int size = 15;
		closeButton.setPreferredSize(new Dimension(size, size));
		setToolTipText("Close this tab");
		closeButton.setUI(new BasicButtonUI());
		closeButton.setContentAreaFilled(false);
		closeButton.setFocusable(false);
		closeButton.setBorder(BorderFactory.createEtchedBorder());
		closeButton.setBorderPainted(false);
		// vsechna tlacitka pouzivaji stejny mouse listener:
		closeButton.addMouseListener(buttonMouseListener);
		closeButton.setRolloverEnabled(true);
		// zavreni tabu pri kliknuti:
		closeButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ActionController.mapSaveDialog(name);
				
				int indexOfClosedTab = Main.gui.mapTabbedPane.indexOfTab(name);
				Main.gui.mapTabbedPane.remove(indexOfClosedTab);
			}
		});
		
		return closeButton;
	}
	
	private final MouseListener panelMouseListener = new MouseAdapter()
	{
		@Override
		public void mousePressed(MouseEvent e)
		{ // pri stisknuti tlacitka mysi je potreba zmenit tab:
			Main.gui.mapTabbedPane.setSelectedIndex(Main.gui.mapTabbedPane
					.indexOfTab(name));
		}
	};

	private final static MouseListener buttonMouseListener = new MouseAdapter()
	{ // listener slouzici k vykresleni borderu, pokud je mys nad tlacitkem
		@Override
		public void mouseEntered(MouseEvent e)
		{
			Component component = e.getComponent();
			AbstractButton button = (AbstractButton) component;
			button.setBorderPainted(true);
		}

		@Override
		public void mouseExited(MouseEvent e)
		{
			Component component = e.getComponent();
			AbstractButton button = (AbstractButton) component;
			button.setBorderPainted(false);
		}
	};
}




