package cz.cuni.mff.rpgeditor.game;

import java.awt.image.BufferedImage;
import java.io.Serializable;

public abstract class GameObject implements Serializable
{
	private static final long serialVersionUID = -1L;
	
	// animovana grafika objektu ve hre
	public BufferedImage[] graphics_frames;
	// hrac (ne)muze projit skrz tento objekt
	boolean has_colision;
	
	public GameObject(BufferedImage[] graphics_frames)
	{
		this.graphics_frames = graphics_frames;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == null)
			return false;
		
		if (!o.getClass().equals(GameObject.class))
		{
			return false;
		}
		
		return true;
	}
	
	public abstract GameObject createDefaultCopy();
}