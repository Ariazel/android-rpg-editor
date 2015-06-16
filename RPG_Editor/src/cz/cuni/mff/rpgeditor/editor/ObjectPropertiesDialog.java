package cz.cuni.mff.rpgeditor.editor;

import java.awt.Container;
import java.awt.Dialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import cz.cuni.mff.rpgeditor.game.StationaryObject;


public class ObjectPropertiesDialog extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	private JTextField[] PARAMS_ENEMY;
	private JTextField[] PARAMS_ALLY;
	
	private final int TF_WIDTH = 15;	// pocet sloupcu v textovych polich
	
	// pocet parametru skupiny s nejvice parametry
	// toto je vyuzito pri prepinani mezi skupinami;
	// u skupin s mene parametry je potreba vyplnit
	// prazdny prostor, aby se okno nepreskladavalo
	private int MAX_PARAMS_COUNT;
	private JPanel PARAMS_PANEL;
	
	public ObjectPropertiesDialog(MapObject o)
	{		
		super(Main.gui.mainFrame);		
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);        

		if (o.getClass().equals(StationaryObject.class))
		{
			return;	// stojici objekty nemaji zadne nastaveni
		}
		
		initAllyParams();
		initEnemyParams();
		initMaxParamsCount();
	
        Container pane = getContentPane();
		pane.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();

		JRadioButton enemy_radio = new JRadioButton("Enemy");
		enemy_radio.setSelected(true);
		enemy_radio.setActionCommand("Enemy");
		enemy_radio.addActionListener(this);
		
		JRadioButton ally_radio = new JRadioButton("Ally");
		ally_radio.setActionCommand("Ally");
		ally_radio.addActionListener(this);
		
		ButtonGroup ally_enemy_group = new ButtonGroup();
		ally_enemy_group.add(enemy_radio);
		ally_enemy_group.add(ally_radio);
		
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(5, 2, 2, 5);
		add(enemy_radio, c);
		
		c.gridx = 1;
		add(ally_radio, c);

		PARAMS_PANEL = new JPanel();
		PARAMS_PANEL.setLayout(new GridBagLayout());
		initParamsPanel(PARAMS_ENEMY);	// pri otevreni dialogu je vybrany enemy
		
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		c.gridheight = PARAMS_ENEMY.length;
		c.insets = new Insets(5, 5, 5, 5);
		add(PARAMS_PANEL, c);
		
		JButton ok_button = new JButton("OK");
		ok_button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				saveObjectParams();
			}
		});
		
		c.gridx = 0;
		c.gridy = PARAMS_ENEMY.length + 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(2, 5, 2, 5);
		add(ok_button, c);
		
		JButton cancel_button = new JButton("Cancel");
		cancel_button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}		
		});
		
		c.gridx = 1;
		add(cancel_button, c);
				
		pack();
		setResizable(false);
        setLocationRelativeTo(Main.gui.mapTabbedPane);	// nastavi dialog na stred mapy
        setVisible(true);	
	}
	
	private void initAllyParams()
	{
		PARAMS_ALLY = new JTextField[1];
		
		JTextField tf = new JTextField(TF_WIDTH);
		tf.setName("Name");
		PARAMS_ALLY[0] = tf;
	}
	
	private void initEnemyParams()
	{
		PARAMS_ENEMY = new JTextField[3];
		
		JTextField tf = new JTextField(TF_WIDTH);
		tf.setName("Name");
		PARAMS_ENEMY[0] = tf;
		
		tf = new JTextField(TF_WIDTH);
		tf.setName("Hit Points");
		PARAMS_ENEMY[1] = tf;
		
		tf = new JTextField(TF_WIDTH);
		tf.setName("Speed");
		PARAMS_ENEMY[2] = tf;
	}
	
	private void initMaxParamsCount()
	{
		if (PARAMS_ALLY.length > PARAMS_ENEMY.length)
			MAX_PARAMS_COUNT = PARAMS_ALLY.length;
		else
			MAX_PARAMS_COUNT = PARAMS_ENEMY.length;
	}
	
	private void initParamsPanel(JTextField[] params_tf)
	{		// TODO: upravit layout, aby neskakal pri zmene ally/enemy
		PARAMS_PANEL.removeAll();
		
		GridBagConstraints c = new GridBagConstraints();
		
		for (int i = 0; i < params_tf.length; ++i)
		{	// zaplneni JPanelu textovymi poli s popisky
			
			c.gridx = 0;
			c.gridy = i;
			c.insets = new Insets(2, 2, 2, 5);
			PARAMS_PANEL.add(new JLabel(params_tf[i].getName()), c);
			
			c.gridx = 1;
			PARAMS_PANEL.add(params_tf[i], c);
		}
		
		for (int i = params_tf.length; i < MAX_PARAMS_COUNT; ++i)
		{	// vyplneni prazdnych mist, aby se okno nepreskladavalo
			c.weightx = 1;
			c.weighty = 1;
			
			c.gridx = 0;
			c.gridy = i;
			c.insets = new Insets(2, 2, 2, 5);
			PARAMS_PANEL.add(new JLabel(" "), c);
			
			c.gridx = 1;
			PARAMS_PANEL.add(new JLabel(" "), c);
		}
		
		PARAMS_PANEL.revalidate();
		PARAMS_PANEL.repaint();
	}
	
	private void saveObjectParams()
	{
		
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().equals("Enemy"))
		{
			initParamsPanel(PARAMS_ENEMY);
		}
		else if (e.getActionCommand().equals("Ally"))
		{
			initParamsPanel(PARAMS_ALLY);
		}
	}
}