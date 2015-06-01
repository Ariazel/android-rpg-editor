package cz.cuni.mff.rpgeditor.editor;

import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import cz.cuni.mff.rpgeditor.game.MovingObject;
import cz.cuni.mff.rpgeditor.game.StationaryObject;

public class ObjectPropertiesDialog extends JDialog
{
	private static final long serialVersionUID = 1L;
	
	private final int DIALOG_WIDTH = 300;
	private final int DIALOG_HEIGHT = 300;
	
	private final int TEXT_FIELDS_WIDTH = 50;
	private final int TEXT_FIELDS_POS_X = 200;
	
	private final String[] PARAMS_ENEMY =
		{
			"Hit Points",
			"Speed"
		};
	private final String[] PARAMS_ALLY =
		{
			"Name"
		};
	
	public ObjectPropertiesDialog(MapObject o)
	{		
		super(Main.gui.mainFrame);		
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        

		if (o.getClass().equals(StationaryObject.class))
		{
			return;	// stojici objekty nemaji zadne nastaveni
		}
        
        setLocationRelativeTo(Main.gui.mapTabbedPane);	// nastavi levy horni roh na stred mapy
        Point p = getLocation();
        p.x -= DIALOG_WIDTH / 2;
        p.y -= DIALOG_HEIGHT / 2;
        setLocation(p);	// stred dialogu v centru mapy
	
        Container pane = getContentPane();
		pane.setLayout(null);
		
		Insets insets = pane.getInsets();
		Dimension size;
				
/*		JTextField[] values = new JTextField[PARAMS_COUNT];
		for (int i = 0; i < PARAMS_COUNT; ++i)
			values[i] = new JTextField();
		
		for (int row = 0; row < PARAMS_COUNT; ++ row)
		{
			pane.add(labels[row]);
			size = labels[row].getPreferredSize();
			labels[row].setBounds(25 + insets.left, 5 + insets.top + 40 * row,
				size.width, size.height);
			
			pane.add(values[row]);
			values[row].setBounds(25 + insets.left + size.width, 5 + insets.top + 40 * row,
					size.width, size.height);
		}
		
		setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
		setResizable(false);
		
        setVisible(true);	
	}
	
	private */
}
}