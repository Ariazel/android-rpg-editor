package cz.cuni.mff.rpgeditor.editor;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class RightScrollPane extends JScrollPane
{
	private static final long serialVersionUID = 1L;
	
	RightPanel rightPanel;

	public RightScrollPane(List<MapObject> tab)
	{
		rightPanel = new RightPanel(tab);
		setViewportView(rightPanel);
		this.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
	}
	
	@Override
	public Dimension getPreferredSize()
	{
		Dimension panelSize = rightPanel.getPreferredSize();
		
		return new Dimension(panelSize.width,
				panelSize.height);
	}
	
	@Override
	public Dimension getMinimumSize()
	{
		int paneBorders = getWidth() - rightPanel.getWidth();
		Dimension panelSize = rightPanel.getMinimumSize();
		
		return new Dimension(panelSize.width + paneBorders,
				panelSize.height + paneBorders);
	}
}
