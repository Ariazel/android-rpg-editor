package cz.cuni.mff.rpgeditor.game;


/**
 * Pohybujici se objekt ve hre - spojenec/nepritel/past
 */
public class MovingObject extends GameObject
{
	private static final long serialVersionUID = 1L;
	// souradnice pohybliveho objektu na mape
	int coord_x, coord_y;
	// objekt je v blizkosti hrace a tedy aktivni (hybe se/utoci)
	boolean is_active;
	// TODO: dva skripty, jeden na pohyb a jeden na utok, reference ulozene zde
		
	public MovingObject(String graphics_filepath)
	{
		super(graphics_filepath);
		// TODO: look_game je jeden velky obrazek s jednotlivymi natocenimi a animacemi, rozdelit do frames
	}
	
	@Override
	public Object clone()
	{
		return new MovingObject(graphics_filepath);
	}

}
