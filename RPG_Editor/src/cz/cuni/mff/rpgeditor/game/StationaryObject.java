package cz.cuni.mff.rpgeditor.game;

import java.awt.image.BufferedImage;


/**
 * Nehybny objekt na mape, napr. skaly a stromy.
 * Tento objekt si nepotrebuje udrzovat vlastni souradnice,
 * kazde pole ma v sobe ulozeny stacionarni objekt (pokud nejaky ma).
 */
public class StationaryObject extends GameObject
{
	public StationaryObject(BufferedImage[] graphics_frames)
	{
		super(graphics_frames);
	}
	
	@Override
	public GameObject createDefaultCopy()
	{
		return new StationaryObject(graphics_frames);
	}
}
