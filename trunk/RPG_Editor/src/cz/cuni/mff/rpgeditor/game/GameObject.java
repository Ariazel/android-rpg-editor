package cz.cuni.mff.rpgeditor.game;

import java.awt.image.BufferedImage;

public abstract class GameObject
{
	// grafika objektu
	public BufferedImage look;
	// hrac (ne)muze projit skrz tento objekt
	boolean has_colision;
	
	public GameObject(BufferedImage look)
	{
		this.look = look;
	}
}