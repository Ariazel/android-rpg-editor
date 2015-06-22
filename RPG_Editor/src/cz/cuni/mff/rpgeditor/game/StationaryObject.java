package cz.cuni.mff.rpgeditor.game;


/**
 * Nehybny objekt na mape, napr. skaly a stromy.
 * Tento objekt si nepotrebuje udrzovat vlastni souradnice,
 * kazde pole ma v sobe ulozeny stacionarni objekt (pokud nejaky ma).
 */
public class StationaryObject extends GameObject
{
	private static final long serialVersionUID = 1L;

	public StationaryObject(String graphics_filepath)
	{
		super(graphics_filepath);
	}
	
	@Override
	public Object clone()
	{
		return new StationaryObject(graphics_filepath);
	}
}
