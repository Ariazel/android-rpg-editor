package cz.cuni.mff.rpgeditor.editor;

import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JScrollPane;

public class MapScrollPane extends JScrollPane
{
	private static final long serialVersionUID = 1L;
	
	MapPanel mapPanel;
	
	MapScrollPane(Map map)
	{		
		// kvuli minimape je potreba registrovat pohyb scrollbaru:
		BoundedRangeModel horizontalModel = new DefaultBoundedRangeModel();
		getHorizontalScrollBar().setModel(horizontalModel);
		horizontalModel.addChangeListener(new ScrollBarListener());

		BoundedRangeModel verticalModel = new DefaultBoundedRangeModel();
		getVerticalScrollBar().setModel(verticalModel);
		verticalModel.addChangeListener(new ScrollBarListener());

		mapPanel = new MapPanel(map);
		setViewportView(mapPanel);
	}
}
