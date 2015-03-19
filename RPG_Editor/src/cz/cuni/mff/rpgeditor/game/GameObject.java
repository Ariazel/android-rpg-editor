package cz.cuni.mff.rpgeditor.game;

import java.awt.image.BufferedImage;

public abstract class GameObject
{
	// animovana grafika objektu ve hre
	public BufferedImage[] graphics_frames;
	// hrac (ne)muze projit skrz tento objekt
	boolean has_colision;
	
	public GameObject(BufferedImage[] graphics_frames)
	{
		this.graphics_frames = graphics_frames;
	}
	
	public GameObject createDefaultCopy()
	{
		return null;
	}
}