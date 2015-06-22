package cz.cuni.mff.rpgeditor.editor;

import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JScrollPane;

public class MapScrollPane extends JScrollPane
{
	private static final long serialVersionUID = 1L;
	
	MapPanel mapPanel;
	
	MapScrollPane(MapPanel map_panel)
	{		
		// kvuli minimape je potreba registrovat pohyb scrollbaru:
		BoundedRangeModel horizontalModel = new DefaultBoundedRangeModel();
		getHorizontalScrollBar().setModel(horizontalModel);
		horizontalModel.addChangeListener(new ScrollBarListener());

		BoundedRangeModel verticalModel = new DefaultBoundedRangeModel();
		getVerticalScrollBar().setModel(verticalModel);
		verticalModel.addChangeListener(new ScrollBarListener());

		mapPanel = map_panel;
		setViewportView(mapPanel);
	}
	
	String getMapName()
	{
		return mapPanel.getMapName();
	}
	
	boolean isMapChanged()
	{
		return !mapPanel.actionController.isInSavedState();
	}
}
