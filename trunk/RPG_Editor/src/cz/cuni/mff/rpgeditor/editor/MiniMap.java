package cz.cuni.mff.rpgeditor.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import cz.cuni.mff.rpgeditor.game.Map;
import cz.cuni.mff.rpgeditor.game.MapTile;


class MiniMap extends JPanel implements MouseListener, MouseMotionListener
{
	private static final long serialVersionUID = 1L;
	private final static int SIZE_X = 150, SIZE_Y = 150; // velikost minimapy je
															// pevna
	Map displayedMap; 
	
	public MiniMap()
	{
		setMinimumSize(new Dimension(SIZE_X, SIZE_Y));
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		this.setOpaque(true);
		setBounds(0, 1, SIZE_X, SIZE_Y);

		addMouseListener(this);
		addMouseMotionListener(this);
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
			paintMiniMap(g);
			paintViewRectangle(g);
		}
	}

	/**
	 * Vykresli teren na minimapu.
	 * 
	 * @param g
	 *            Grafika, kterou bude kresleno.
	 */
	private void paintMiniMap(Graphics g)
	{
		MapTile.TerrainType previousTileType = displayedMap.getTile(0, 0).type;
		Color usedColor = previousTileType.miniMapColor;
		g.setColor(usedColor);
		
		Point2D miniWidthHeight = toMiniMapCoords(new Point(1, 1));
						// velikost jednoho policka mapy v pixelech na minimape
		double remainderWidth = 0, remainderHeight = 0;
						// policko mapy muze byt mene nez jeden pixel, proto je
						// potreba pocitat zbytek
		final double epsilon = 0.000001;
			// kvuli zaokrouhlovacim chybam je vzdy prictena miliontina k vysledku
		for (int y = 0; y < displayedMap.getHeight(); ++y)
		{
			remainderHeight += miniWidthHeight.getY() + epsilon;
			if (remainderHeight < 1)
			{	// pokud tento radek nema vysku pres 1 pixel
				continue;
			}

			for(int x = 0; x < displayedMap.getWidth(); ++x)
			{
				MapTile.TerrainType currentTileType = displayedMap.getTile(x, y).type;
				if (currentTileType != previousTileType)
				{	// vykreslovane policko ma jiny druh terenu nez predchozi
					usedColor = currentTileType.miniMapColor;
					g.setColor(usedColor);
					previousTileType = currentTileType;
				}
				
				remainderWidth += miniWidthHeight.getX() + epsilon;
				
				Point2D leftTopCorner = toMiniMapCoords(new Point(x, y));
				
				g.fillRect((int)leftTopCorner.getX(), (int)leftTopCorner.getY(),
						(int)remainderWidth, (int)remainderHeight);
				
				remainderWidth -= (int)remainderWidth;
			}

			remainderHeight -= (int)remainderHeight;
		}
	}
	
	private Point2D.Double toMiniMapCoords(Point p)
	{
		double xRatio = (double)SIZE_X / displayedMap.getWidth();
		double yRatio = (double)SIZE_Y / displayedMap.getHeight();
		
		return new Point2D.Double(p.x * xRatio, p.y * yRatio);
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

	/**
	 * Metoda slouzici k posunuti pohledu na mape pomoci minimapy.
	 * @param x Souradnice x na minimape.
	 * @param y Souradnice y na minimape.
	 */
	private void moveViewTo(int x, int y)
	{
		Rectangle view = getViewRectangle();
		
		Point p = new Point();
		Dimension size = Main.gui.mapTabbedPane.getDisplayedMapPanel()
				.getPreferredSize();

		int offset = Main.gui.mapTabbedPane.getDisplayedMapPanel().heightOfSkyImage;

		float xRatio = (float) size.width / SIZE_X;
		float yRatio = (float) (size.height - offset) / SIZE_Y;
		p.x = (int) (x * xRatio - (view.width * xRatio) / 2);
		p.y = (int) ((y * yRatio) - (view.height * yRatio) / 2) + offset;

		if (p.x < 0)
			p.x = 0;
		if (p.y < 0)
			p.y = 0;

		if (p.x + (view.width * xRatio) > size.getWidth())
			p.x = (int) (size.getWidth() - view.width * xRatio);
		if (p.y + (view.height * yRatio) > size.getHeight())
			p.y = (int) (size.getHeight() - view.height * yRatio);


		Main.gui.mapTabbedPane.currentTab.getViewport()
				.setViewPosition(p);
	}

	class TerrainColor
	{
		String path;
		Color color;
	}
	
	// metody MouseListeneru a MouseMotionListeneru:

	@Override
	public void mouseDragged(MouseEvent e)
	{
		if (Main.gui.mapTabbedPane.currentTab != null)
		{
			moveViewTo(e.getX(), e.getY());
		}
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
		if (Main.gui.mapTabbedPane.currentTab != null)
		{
			moveViewTo(e.getX(), e.getY());
		}
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		// neni treba nic udelat

	}
}