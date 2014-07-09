package cz.cuni.mff.rpgeditor.editor;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * Stavovy radek umisteny dole v editoru. Bude zobrazovat prubeh prave
 * probihajicich akci a aktualni pozici na mape.
 */
public class StatusBar extends JPanel
{
	private static final long serialVersionUID = 1L;
	JLabel status;
	JLabel row;
	JLabel column;

	private static final Dimension MIN_SIZE_OF_STATUS_LABELS = new Dimension(
			80, 18);
	private static final Font STATUS_BAR_FONT = new Font("sans-serif",
			Font.PLAIN, 12);

	static JLabel fps;
	public static int frameCount = 0;

	StatusBar()
	{
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		status = new JLabel("Ready");
		status.setFont(new Font("sans-serif", Font.PLAIN, 12));
		status.setOpaque(true);
		status.setBackground(GUI.BACKGROUND_COLOR);
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(status, c);

		row = new JLabel("Row: ");
		row.setMinimumSize(MIN_SIZE_OF_STATUS_LABELS);
		row.setOpaque(true);
		row.setBackground(GUI.BACKGROUND_COLOR);
		row.setBorder(BorderFactory.createLoweredBevelBorder());
		row.setFont(STATUS_BAR_FONT);
		c.weightx = 0;
		add(row, c);

		column = new JLabel("Col: ");
		column.setMinimumSize(MIN_SIZE_OF_STATUS_LABELS);
		column.setOpaque(true);
		column.setBackground(GUI.BACKGROUND_COLOR);
		column.setBorder(BorderFactory.createLoweredBevelBorder());
		column.setFont(STATUS_BAR_FONT);
		add(column, c);

		setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, GUI.BORDER_COLOR));

		setMinimumSize(new Dimension(50, 18));
	}

	/**
	 * Zobrazi uzivatelske souradnice policka, na kterem je mys. Pokud je coords
	 * null, nezobrazi se nic.
	 * 
	 * @param coords
	 *            Uzivatelske souradnice, ktere se zobrazi.
	 */
	void showCurrentMousePosition(Point coords)
	{
		if (coords == null)
		{
			row.setText("Row: ");
			column.setText("Col: ");
		}
		else
		{
			row.setText("Row: " + coords.x);
			column.setText("Col: " + coords.y);
		}
	}


	void setCurrentStatus(String s)
	{
		if (s == null)
			status.setText("Ready");
		else
			status.setText(s);
	}
}