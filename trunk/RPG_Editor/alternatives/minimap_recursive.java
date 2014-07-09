package cz.cuni.mff.rpgeditor.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import cz.cuni.mff.rpgeditor.game.Image;
import cz.cuni.mff.rpgeditor.game.Map;
import cz.cuni.mff.rpgeditor.game.MapTile;


class MiniMap extends JPanel implements MouseListener, MouseMotionListener
{
	private static final long serialVersionUID = 1L;
	private final static int SIZE_X = 150, SIZE_Y = 150; // velikost minimapy je
															// pevna
	Rectangle lastView;
	Map displayedMap;
	
	public MiniMap()
	{
		setMinimumSize(new Dimension(SIZE_X, SIZE_Y));
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		this.setOpaque(true);
		setBounds(0, 1, SIZE_X, SIZE_Y);

		addMouseListener(this);
		addMouseMotionListener(this);

		lastView = new Rectangle();// new Rectangle(0, 0, 100, 100);
	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(SIZE_X, SIZE_Y);
	}

	@Override
	public Dimension getMinimumSize()
	{
		return getPreferredSize();
	}

	@Override
	public Dimension getMaximumSize()
	{
		return getPreferredSize();
	}

	public void paintComponent(java.awt.Graphics g)
	{
		super.paintComponent(g);
		displayedMap = Main.gui.mapTabbedPane.getDisplayedMap();
		if (displayedMap != null)
		{
			paintPartOfMiniMap(g, new Rectangle(0, 0, displayedMap.getWidth(),
					displayedMap.getHeight()));
			paintViewRectangle(g);
		}
	}

	/**
	 * Rekurzivne vykresli cast minimapy danou obdelnikem.
	 * 
	 * @param g
	 *            Grafika, kterou bude kresleno.
	 * @param r
	 *            Obdelnik na mape urcujici vykreslovanou oblast.
	 */
	private void paintPartOfMiniMap(Graphics g, Rectangle paintedPart)
	{
		displayedMap = Main.gui.mapTabbedPane.getDisplayedMap();
		
		int x = paintedPart.x;
		int y = paintedPart.y;
		
		Image currentType = new Image(getMiniFile(displayedMap.getTile(x, y)));
		
		Rectangle sameTerrainRect = getLargestSubrectangle(paintedPart);
			// Obdelnik se stejnym typem terenu, takze je mozne na minimape vykreslit
			// vetsi obdelnik misto vykreslovani jednotlivych pixelu.
		Rectangle miniMapRect = convertToMiniMapRect(sameTerrainRect);
		g.setColor(new Color(currentType.getImage().getRGB(0, 0)));
		g.fillRect(miniMapRect.x, miniMapRect.y, miniMapRect.width, miniMapRect.height);
		
		if (sameTerrainRect.width != paintedPart.width && sameTerrainRect.height == paintedPart.height)
		{	// leva cast obdelniku je vykreslena, je potreba vykreslit pravou cast
			paintPartOfMiniMap(g, new Rectangle(sameTerrainRect.x + sameTerrainRect.width,
												paintedPart.y,
												paintedPart.width - sameTerrainRect.width,
												paintedPart.height));
		}
		else if (sameTerrainRect.height != paintedPart.height && sameTerrainRect.width == paintedPart.width)
		{	// horni cast obdelniku je vykreslena, je potreba vykreslit spodni cast
			paintPartOfMiniMap(g, new Rectangle(paintedPart.x,
												sameTerrainRect.y + sameTerrainRect.height,
												paintedPart.width,
												paintedPart.height - sameTerrainRect.height));
		}
		else if (sameTerrainRect.height != paintedPart.height
				&& sameTerrainRect.width != paintedPart.width)
		{ // je vykreslena cast obdelniku v levem hornim rohu, je potreba
			// vykreslit
			// obdelniky vpravo, dole a vpravo dole
			paintPartOfMiniMap(g, new Rectangle(sameTerrainRect.x + sameTerrainRect.width,
												paintedPart.y,
												paintedPart.width - sameTerrainRect.width,
												paintedPart.height));
			paintPartOfMiniMap(g, new Rectangle(paintedPart.x,
												sameTerrainRect.y + sameTerrainRect.height,
												sameTerrainRect.width,
												paintedPart.height - sameTerrainRect.height));
		}
	}

	private String getMiniFile(MapTile tile)
	{
		return tile.image.getPathOfFolder() + '/' + tile.image.getFolderName() + "_mini.gif";
	}
	
	/**
	 * Dostane souradnice leveho horniho rohu obdelniku na mape a snazi se
	 * najit co nejvetsi obdelnik se stejnym terenem.
	 * @param x Souradnice x na mape.
	 * @param y Souradnice y na mape.
	 * @return Nejvetsi nalezeny obdelnik prepocitany na velikost minimapy.
	 */
	private Rectangle getLargestSubrectangle(Rectangle paintedPart)
	{
		Rectangle initialRect = new Rectangle(paintedPart.x, paintedPart.y, 1, 1);

		Rectangle highRect = enlargeRectangleX(
				enlargeRectangleY(initialRect, paintedPart.height),
				paintedPart.width);
		Rectangle wideRect = enlargeRectangleY(
				enlargeRectangleX(initialRect, paintedPart.width),
				paintedPart.height);

		Rectangle largerRect = getLargerRectangle(highRect, wideRect);
	
		return largerRect;
	}
	
	/**
	 *  Zvetsi obdelnik ve smeru osy X o co nejvice tak, aby
	 *  byl vsude v obdelniku stejny teren.
	 * @param originalRect Puvodni obdelnik, ktery nebude funkci zmenen.
	 * @param maxWidth Maximalni sirka obdelniku po zvetseni.
	 * @return Novy zvetseny obdelnik.
	 */
	private Rectangle enlargeRectangleX(Rectangle originalRect, int maxWidth)
	{
		if (originalRect.width > maxWidth)
		{
			throw new Error("Unexpected rectangle in enlargeRectangleX");
		}
		
		Rectangle largerRect = new Rectangle(originalRect);
		displayedMap = Main.gui.mapTabbedPane.getDisplayedMap();
		MapTile searchedTile = displayedMap.getTile(largerRect.x, largerRect.y);
		
		while (displayedMap.getWidth() > largerRect.x + largerRect.width)
		{ // obdelnik jeste neni u okraje mapy, je moznost jej zvetsit
			if (largerRect.width == maxWidth)
			{	// obdelnik je v ramci omezeni nejvetsi mozny
				return largerRect;
			}
			
			for (int row = largerRect.y; row < largerRect.y + largerRect.height; ++row)
			{
				MapTile currentTile = displayedMap.getTile(largerRect.x
						+ largerRect.width, row);
				if (!searchedTile.terrainEquals(currentTile))
				{	// v nasledujicim sloupci bylo nalezeno pole jineho terenu
					return largerRect;
				}
			}
			largerRect.width++;
		}
		
		return largerRect;
	}
	
	/**
	 *  Zvetsi obdelnik ve smeru osy Y o co nejvice tak, aby
	 *  byl vsude v obdelniku stejny teren.
	 * @param originalRect Puvodni obdelnik, ktery nebude funkci zmenen
	 * @param maxHeight Maximalni vyska obdelniku po zvetseni.
	 * @return Novy zvetseny obdelnik.
	 */	
	private Rectangle enlargeRectangleY(Rectangle originalRect, int maxHeight)
	{		
		if (originalRect.height > maxHeight)
		{
			throw new Error("Unexpected rectangle in enlargeRectangleY");
		}

		Rectangle largerRect = new Rectangle(originalRect);
		displayedMap = Main.gui.mapTabbedPane.getDisplayedMap();
		MapTile searchedTile = displayedMap.getTile(largerRect.x, largerRect.y);
		
		while (displayedMap.getHeight() > largerRect.y + largerRect.height)
		{ // obdelnik jeste neni u okraje mapy, je moznost jej zvetsit
			if (largerRect.height == maxHeight)
			{	// obdelnik je v ramci omezeni nejvetsi mozny
				return largerRect;
			}

			for (int col = largerRect.x; col < largerRect.x + largerRect.width; ++col)
			{
				MapTile currentTile = displayedMap.getTile(col, largerRect.y
						+ largerRect.height);
				if (!searchedTile.terrainEquals(currentTile))
				{
					return largerRect;
				}
			}
			largerRect.height++;
		}
		
		return largerRect;
	}
	
	private Rectangle getLargerRectangle(Rectangle first, Rectangle second)
	{
		int firstArea = first.width * first.height;
		int secondArea = second.width * second.height;
		
		if (firstArea > secondArea)
		{
			return first;
		}
		else
		{
			return second;
		}
	}
	
	private Rectangle convertToMiniMapRect(Rectangle r)
	{
		displayedMap = Main.gui.mapTabbedPane.getDisplayedMap();
		int mapWidth = displayedMap.getWidth();
		int mapHeight = displayedMap.getHeight();
		
		double xRatio = (double)SIZE_X / mapWidth;
		double yRatio = (double)SIZE_Y / mapHeight;

		return new Rectangle((int)Math.round(r.x * xRatio), (int)Math.round(r.y * yRatio),
				(int)Math.round(r.width * xRatio), (int)Math.round(r.height * yRatio));
	}

	// vykresli obdelnik pohledu:
 	private void paintViewRectangle(Graphics g)
	{
		g.setColor(new Color(0xFFFFFF));
		Rectangle viewRect = getViewRectangle();
		g.drawRect(viewRect.x, viewRect.y, viewRect.width, viewRect.height);
	}


	/**
	 * Tato metoda urci obdelnik pohledu na mape a vrati jej v souradnicich
	 * minimapy.
	 * 
	 * @return Obdelnik na minimape, ktery odpovida pohledu.
	 */
	private Rectangle getViewRectangle()
	{
		MapPanel mapPanel = Main.gui.mapTabbedPane.getDisplayedMapPanel();
		Dimension size = mapPanel.getPreferredSize();

		int offset = Main.gui.mapTabbedPane.getDisplayedMapPanel().heightOfSkyImage;

		double xRatio = (double) SIZE_X / size.width;
		double yRatio = (double) SIZE_Y / (size.height - offset);

		Rectangle view = mapPanel.getVisibleRect();

		int upperLeftX = (int) (view.x * xRatio);
		int upperLeftY = (int) ((view.y - offset) * yRatio);
		int width = (int) (view.width * xRatio);
		int height = (int) (view.height * yRatio);
		
		return new Rectangle(upperLeftX, upperLeftY, width, height);
	}

	private void moveViewTo(int x, int y)
	{
		Rectangle view = getViewRectangle();
		Point p = new Point();
		Dimension size = Main.gui.mapTabbedPane.getDisplayedMapPanel()
				.getPreferredSize();

		int offset = Main.gui.mapTabbedPane.getDisplayedMapPanel().heightOfSkyImage;

		float xratio = (float) size.width / SIZE_X;
		float yratio = (float) (size.height - offset) / SIZE_Y;
		p.x = (int) (x * xratio - (view.width * xratio) / 2);
		p.y = (int) ((y * yratio) - (view.height * yratio) / 2) + offset;

		if (p.x < 0)
			p.x = 0;
		if (p.y < 0)
			p.y = 0;

		if (p.x + (view.width * xratio) > size.getWidth())
			p.x = (int) (size.getWidth() - view.width * xratio);
		if (p.y + (view.height * yratio) > size.getHeight())
			p.y = (int) (size.getHeight() - view.height * yratio);


		Main.gui.mapTabbedPane.getDisplayedMapScrollPane().getViewport()
				.setViewPosition(p);
	}

	
	// metody MouseListeneru a MouseMotionListeneru:

	@Override
	public void mouseDragged(MouseEvent e)
	{
		moveViewTo(e.getX(), e.getY());
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		// neni treba nic udelat

	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		// posunuti se provede jiz v metode mousePressed

	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		// neni treba nic udelat
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		// neni treba nic udelat

	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		moveViewTo(e.getX(), e.getY());
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		// neni treba nic udelat

	}
}