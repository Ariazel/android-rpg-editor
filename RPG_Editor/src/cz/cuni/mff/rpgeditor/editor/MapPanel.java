package cz.cuni.mff.rpgeditor.editor;

import gnu.trove.TIntProcedure;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

import com.infomatiq.jsi.SpatialIndex;
import com.infomatiq.jsi.rtree.RTree;


/**
 * Trida slouzici k vykresleni mapy v editoru.
 */
public class MapPanel extends JPanel implements Scrollable
{
	private static final long serialVersionUID = 1L;
	
	protected Map map;
	// mapa vykreslena na tomto panelu
	final int heightOfSkyImage = 256;
	// urcuje posun mapy od horni hrany teto komponenty (kvuli obloze)
	SpatialIndex mapObjectsIndex = new RTree();
	// seznam obdelniku objektu na mape ulozenych v R-strome
	HashMap<Integer, com.infomatiq.jsi.Rectangle> objectRectangles = new HashMap<Integer, com.infomatiq.jsi.Rectangle>();
	// seznam obdelniku objektu na mape ulozenych v hash mape
	ActionController actionController = new ActionController();
	// kazdy panel ma vlastni seznam poslednich akci
	
	MapPanel(Map map)
	{
		MapMouseAdapter mouseListener = new MapMouseAdapter();
		addMouseMotionListener(mouseListener);
		addMouseListener(mouseListener);

		this.map = map;
		
		mapObjectsIndex.init(null);
	}
	
	@Override
	public Dimension getPreferredSize()
	{
		if (map != null)
		{
			return new Dimension(map.getWidthInPixels(),
					map.getHeightInPixels() + heightOfSkyImage);
		}
		else
		{
			return new Dimension(0, 0);
		}
	}

	String getMapName()
	{
		return map.name;
	}
	
	/**
	 * Metoda slouzici k vykresleni mapy v prostrednim panelu.
	 */
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		paintSky(g);
		paintMap(g);
		if (Main.gui.mapTabbedPane.gridVisible)
		{
			paintGrid(g);
		}
		if (!objectIsDragged())
		{
			paintMouseGrid(g);
		}
		paintMapObjects(g);
		paintDraggedObject(g);
	}
	
	/**
	 * Vykresli nebe nad mapou, ktere podpori isometricky pohled.
	 * 
	 * @param g
	 *            Grafika pouzita ke kresleni.
	 */
	private void paintSky(Graphics g)
	{		
		java.awt.Image skyImage = null;
		String pathOfSkyImage = "res/sky.gif";
		try
		{
			skyImage = ImageIO.read(new File(pathOfSkyImage));
		}
		catch (IOException e)
		{
			System.err.print("Cannot load image from " + pathOfSkyImage);
			System.exit(1);
		}
		
		int widthOfSkyImage = 400;
		int numberOfSkyImages = map.getWidthInPixels() / widthOfSkyImage;
							// pocet opakovani obrazku nebe nad mapou
		for (int actualImage = 0; actualImage <= numberOfSkyImages; ++actualImage)
		{
			g.drawImage(skyImage, actualImage * widthOfSkyImage, 0,
					(actualImage + 1) * widthOfSkyImage, heightOfSkyImage, 0,
					0, widthOfSkyImage, heightOfSkyImage, null);
		}
	}
	
	/**
	 * Vykresli mapu.
	 * @param g Grafika pouzita ke kresleni.
	 */
	private void paintMap(Graphics g)
	{
		java.awt.Image image;	// obrazek terenu, ktery bude prekreslen na tento panel
		int destLeftTopX, destLeftTopY,
			// souradnice leveho horniho rohu obdelniku v cilovem obrazku
			destRightBotX, destRightBotY,
			// souradnice praveho dolniho rohu obdelniku v cilovem obrazku
			srcLeftTopX, srcLeftTopY,
			// souradnice leveho horniho rohu obdelniku ve zdrojovem obrazku
			srcRightBotX, srcRightBotY;
			// souradnice praveho dolniho rohu obdelniku ve zdrojovem obrazku

		for (int y = 0; y < map.getHeight(); ++y)
		{
			for (int x = 0; x < map.getWidth(); ++x)
			{
				image = map.getTile(x, y).getImage();

				destLeftTopX = x * MapTile.imageSizeWidth;
				if (y % 2 == 1)
				{ 	// rady se vykresluji stridave o pul pole posunute, aby do
					// sebe naklonene ctverce zapadaly
					destLeftTopX -= MapTile.imageSizeWidth / 2;
				}

				destLeftTopY = (y - 1) * MapTile.imageSizeHeight / 2 + heightOfSkyImage;
				destRightBotX = destLeftTopX + MapTile.imageSizeWidth;
				destRightBotY = destLeftTopY + MapTile.imageSizeHeight;
				
				srcLeftTopX = 0;
				srcRightBotX = MapTile.imageSizeWidth;
		
				srcLeftTopY = 0;
				srcRightBotY = MapTile.imageSizeHeight;
				if (y== 0)
				{ /*
				 * Na prvnim radku je potreba vykreslit pouze spodni polovinu
				 * policka, protoze zbytek presahuje do obrazku nebe. Ostatni
				 * okraje mapy neni treba resit, protoze tam policka presahuji
				 * mimo panel
				 */
					srcLeftTopY = MapTile.imageSizeHeight / 2;
					destLeftTopY += MapTile.imageSizeHeight / 2;
				}

				g.drawImage(image, destLeftTopX, destLeftTopY, destRightBotX,
						destRightBotY, srcLeftTopX, srcLeftTopY, srcRightBotX,
						srcRightBotY, null);
			}
		}
	}

	/**
	 * Vykresli mrizku terenu oddelujici jednotliva policka cernymi carami.	
	 * @param g Grafika, kterou je kresleno.
	 */
	private void paintGrid(Graphics g)
	{
		g.setColor(new Color(0x000000));

		for (int index = 0; index < map.getWidth() + map.getHeight(); ++index)
		{
			g.drawLine(0, index * MapTile.imageSizeHeight + heightOfSkyImage, index
					* MapTile.imageSizeWidth, heightOfSkyImage);
			g.drawLine((map.getWidth() - index) * MapTile.imageSizeWidth,
					heightOfSkyImage, map.getWidth() * MapTile.imageSizeWidth, index
							* MapTile.imageSizeHeight + heightOfSkyImage);
		}
	}

	/**
	 * Vykresli na mape mrizku u pozice mysi podle prave pouzivaneho nastroje.
	 * 
	 * @param g
	 *            Grafika pouzita ke kresleni.
	 */
	private void paintMouseGrid(Graphics g)
	{
		Point mousePosition = getMousePosition();
		if (mousePosition == null)
		{
			return;
		}
		
		Point posOnMap = tileFromPixelCoords(mousePosition);

		if (Main.gui.selectedTool == GUI.Tool.TERRAIN_TOOL)
		{
			int brushSize = Main.gui.leftPanel.terrainSettings.brushSize;
			LinkedList<Point> selectedTiles = getSelectedTiles(brushSize,posOnMap);
			paintSelectedSquares(g, selectedTiles);
		}
	}
	
	MapObject selected_object;
	Point selected_object_position;
	/**
	 * Vykresli objekty na mape.
	 * @param g Grafika pouzita ke kresleni.
	 */
	private void paintMapObjects(Graphics g)
	{		
		for (int y = 0; y < map.getHeight(); ++y)
		{
			for (int x = 0; x < map.getWidth(); ++x)
			{
				MapObject object = map.getTile(x, y).map_object;
				paintTileObject(g, x, y, object);
				
				if (object != null && object.equals(selected_object))
				{
					Rectangle objectRect = getObjectRectangle(x, y, object);
					g.drawRect(objectRect.x, objectRect.y, objectRect.width, objectRect.height);
				}
			}
		}
	}
	
	/**
	 * Vykresli objekt na policku danem souradnicemi.
	 * @param g Grafika pouzita ke kresleni.
	 * @param x Souradnice x policka s objektem.
	 * @param y Souradnice Y policka s objektem.
	 */
	private void paintTileObject(Graphics g, int x, int y, MapObject object)
	{
		if (object == null)
		{
			return;
		}

		Rectangle objectRect = getObjectRectangle(x, y, object);
		g.drawImage(object.look_on_map,
				objectRect.x, objectRect.y,
				objectRect.x + objectRect.width, objectRect.y + objectRect.height,
				0, 0,
				object.look_on_map.getWidth(), object.look_on_map.getHeight(),
				null);
	}
	
	MapObject dragged_object = null;
	Point dragged_object_old_position = null;
	// pokud je objekt presouvan, je zde ulozena jeho stara pozice v pixelech a udaje o nem
	
	/**
	 * Vykresli objekt, ktery je prave presouvan a je u mysi.
	 * @param g Grafika pouzita ke kresleni.
	 */
	private void paintDraggedObject(Graphics g)
	{
		MapObject addedMapObject = Main.gui.rightTabbedPane.getTransferedMapObject();
		// objekt pridavany z praveho panelu
		if (!objectIsDragged())
		{
			return;
		}
		
		MapObject objectToPaint;
		if (addedMapObject == null)
		{
			objectToPaint = dragged_object;
		}
		else if (dragged_object == null)
		{
			objectToPaint = addedMapObject;
		}
		else
		{
			throw new Error("Two objects being dragged at once.");
		}
		
		BufferedImage image = objectToPaint.look_on_map;
		
		Point mousePos = getMousePosition();
		
		if (mousePos == null)
		{
			return;
		}
		
		mousePos.y += image.getHeight() / 2;
		// objekt je drzen veprostred, ale prichytavat k polickam se ma spodek
		Point coords = tileFromPixelCoords(mousePos);
		
		if (coords.x < 0 || coords.y < 0 || coords.x >= map.getWidth()
				|| coords.y >= map.getHeight())
		{
			return;
		}
		
		paintTileObject(g, coords.x, coords.y, objectToPaint);
	}
	
	private boolean objectIsDragged()
	{
		MapObject addedMapObject = Main.gui.rightTabbedPane.getTransferedMapObject();
		if (addedMapObject == null && dragged_object == null)
		{	// zadny objekt neni v DnD rezimu
			return false;
		}
		else
		{
			return true;
		}

	}
	
	/**
	 * Urci, ktere pole na mape obsahuje bod.
	 * 
	 * @param x
	 *            Souradnice X daneho bodu v pixelech.
	 * @param y
	 *            Souradnice Y daneho bodu v pixelech.
	 * @return Souradnice pole na mape, na kterem lezi bod, muze mit i zapornou
	 *         hodnotu.
	 */
	private Point tileFromPixelCoords(Point p)
	{
		if (p == null)
		{
			return null;
		}
		
		int coordX = p.x / MapTile.imageSizeWidth;
		int topOffset = heightOfSkyImage - MapTile.imageSizeHeight / 2;
		// odsazeni mapy od horniho okraje panelu
		int coordY = 2 * ((p.y - topOffset) / MapTile.imageSizeHeight);
		// vybira pouze sude indexy, ale muze se stat, ze bod lezi v pruhledne
		// casti policka, tedy o jedno policko jinde

		Point relativePos = new Point(p.x % MapTile.imageSizeWidth,
				(p.y - topOffset) % MapTile.imageSizeHeight);
		// kde ve vybranem policku se bod nachazi
		double lineAngle = (double)MapTile.imageSizeHeight / MapTile.imageSizeWidth;
		// uhel primek ohranicujicich policka
		if (relativePos.y < -lineAngle * relativePos.x + MapTile.imageSizeHeight / 2)
		{	// bod je v levem hornim rohu mimo policko
			--coordY;
		}
		else if (relativePos.y < lineAngle * relativePos.x - MapTile.imageSizeHeight / 2)
		{	// bod je v pravem hornim rohu mimo policko
			++coordX;
			--coordY;
		}
		else if(relativePos.y > lineAngle * relativePos.x + MapTile.imageSizeHeight / 2)
		{	// bod je v levem spodnim rohu mimo policko
			++coordY;
		}
		else if(relativePos.y > -lineAngle * relativePos.x + 3 * MapTile.imageSizeHeight / 2)
		{	// bod je v pravem spodnim rohu mimo policko
			++coordX;
			++coordY;
		}
		return new Point(coordX, coordY);
	}

	
	/**
	 * Prevede seznam bodu zadanych v pixelech na souradnice policka na mape.
	 * @param points Zadany seznam bodu.
	 * @return Seznam souradnic policek.
	 */
	private LinkedList<Point> tilesFromPixelCoords(LinkedList<Point> points)
	{
		LinkedList<Point> result = new LinkedList<Point>();
		
		for (Point p : points)
		{	// prevedeni vsech bodu zadanych v pixelech na souradnice policek na mape
			Point coords = tileFromPixelCoords(p);
			if (!result.contains(coords))
			{
				result.add(coords);
			}
		}
		
		return result;
	}
	
	/**
	 * Vykresli cerne ctverce kolem zadaneho seznamu poli na mape.
	 * @param g Grafika pouzita ke kresleni.
	 * @param squares Seznam ctvercu, kolem kterych bude kresleno.
	 */
	private void paintSelectedSquares(Graphics g, LinkedList<Point> squares)
	{
		g.setColor(new Color(0xFFFFFF));
		for (Point tile : squares)
		{

			int leftTopX, leftTopY; // levy horni roh policka
			int rightBotX, rightBotY; // pravy dolni roh policka
			
			leftTopX = tile.x * MapTile.imageSizeWidth;
			if (tile.y % 2 == 1)
			{	// liche radky jsou posunute o pul policka doleva
				leftTopX -= MapTile.imageSizeWidth / 2;
			}
			leftTopY = heightOfSkyImage + (tile.y - 1) * MapTile.imageSizeHeight / 2;
			
			rightBotX = leftTopX + MapTile.imageSizeWidth;
			rightBotY = leftTopY + MapTile.imageSizeHeight;

			g.drawLine((leftTopX + rightBotX) / 2, leftTopY, rightBotX, (leftTopY + rightBotY) / 2);
			g.drawLine(rightBotX, (leftTopY + rightBotY) / 2, (leftTopX + rightBotX) / 2, rightBotY);
			g.drawLine((leftTopX + rightBotX) / 2, rightBotY, leftTopX, (leftTopY + rightBotY) / 2);
			g.drawLine(leftTopX, (leftTopY + rightBotY) / 2, (leftTopX + rightBotX) / 2, leftTopY);
		}
	}

	/**
	 * Urci, ktera policka sousedi s polickem centre a podle dane velikosti
	 * vytvori ctverec policek okolo centra.
	 * 
	 * @param size
	 *            Velikost ctverce okolo centra.
	 * @param centre
	 *            Stred ctverce.
	 * @return Seznam poli obklopujicich centrum.
	 */
	private LinkedList<Point> getSelectedTiles(int size, Point centre)
	{
		LinkedList<Point> selectedTiles = new LinkedList<Point>();
		
		for (int row = 0; row < size; ++row)
		{
			for (int col = 0; col <= row; ++col)
			{
				// vypocet radku a sloupce prislusny cislovani vnitrni
				// reprezentace, navic je od y odectena size - 1, aby byla mys v
				// prostrednim ctverci:
				int x = centre.x - row / 2 + col;
				int y = centre.y + row - size + 1;

				if (centre.y % 2 == 1 && y % 2 == 0)
				{	// liche a sude radky jsou o jedno pole posunute
					--x;
				}

				if (x < 0 || y < 0 || x >= map.getWidth()
						|| y >= map.getHeight())
				{	// policko je mimo mapu
					continue;
				}

				selectedTiles.add(new Point(x, y));
			}
		}

		for (int row = size - 1; row > 0; --row)
			for (int col = 0; col < row; ++col)
			{
				// stejne vypocty jako v predchozim cyklu, ale pocita spodni
				// polovinu ctverce (indexy jdou odshora dolu)
				int x = centre.x - row / 2 + col;
				int y = centre.y + size - row;

				if (centre.y % 2 == 0 && y % 2 == 1)
				{	// liche a sude radky jsou od sebe o jedno pole pousunute
					++x;
				}

				if (x < 0 || y < 0 || x >= map.getWidth()
						|| y >= map.getHeight())
				{	// policko je mimo mapu
					continue;
				}

				selectedTiles.add(new Point(x, y));
			}

		return selectedTiles;
	}
	
	/**
	 * Prevede souradnice vnitrni reprezentace mapy na uzivatelske souradnice.
	 * 
	 * @param p
	 *            Souradnice policka podle vnitrni reprezentace.
	 * @return Uzivatelske souradnice policka.
	 */
	private Point toUserCoords(Point p)
	{
		// prevod souradnic na uzivatelske, protoze souradnice
		// vnitrni reprezentace by mohly mast:
		int row, col;
		if (p.y % 2 == 0)
			row = map.getWidth() - p.x + p.y / 2 - 1;
		else
			row = map.getWidth() - p.x + p.y / 2;
		col = p.x + p.y / 2 + 1;

		return new Point(row, col);
	}

	/**
	 * Prida objekt na dane misto na mape.
	 * 
	 * @param position
	 *            Souradnice v pixelech, kam ma byt objekt pridan.
	 * @param object
	 *            Pridavany objekt.
	 * @return Zda se operace pridani zdarila.
	 */
	boolean addMapObjectToPixelCoords(Point coords, MapObject object)
	{
		Point position = tileFromPixelCoords(coords);
		
		return addMapObject(position, object);
	}
	
	/**
	 * Prida objekt na policko na mape.
	 * @param Position Souradnice pole, na ktere ma byt objekt pridan.
	 * @param Object Objekt, ktery se ma pridat.
	 * @return True, pokud bylo pridani objektu uspesne.
	 */
	boolean addMapObject(Point position, MapObject object)
	{
		if (position.x < 0 || position.y < 0 || position.x >= map.getWidth()
				|| position.y >= map.getHeight())
		{	// pokud jsou souradnice mimo mapu, neni mozne pridat objekt
			return false;
		}
		
		if (map.getTile(position.x, position.y).map_object != null)
		{
			return false;
		}
		
		MapObject addedObject = map.addMapObject(position.x, position.y, object);
		
		com.infomatiq.jsi.Rectangle addedObjectRect = getObjectRTreeRectangle(position.x, position.y, addedObject);
		
		objectRectangles.put(addedObject.id, addedObjectRect);
		mapObjectsIndex.add(addedObjectRect, addedObject.id);
		
		return true;
	}	
	
	/**
	 * Odstrani objekt z mapy.
	 * @param coords Souradnice, na kterych ma byt objekt odstranen.
	 * @return Odstraneny objekt.
	 */
	MapObject removeMapObject(Point position)
	{
		Point coords = Main.gui.mapTabbedPane.getDisplayedMapPanel().tileFromPixelCoords(position);
		
		if (coords.x < 0 || coords.y < 0 || coords.x >= map.getWidth()
				|| coords.y >= map.getHeight())
		{	// pokud jsou souradnice mimo mapu, neni mozne odebrat objekt
			return null;
		}

		MapObject objectToRemove = map.getTile(coords.x, coords.y).map_object;
		
		if (objectToRemove == null)
		{	// na policku zadny objekt neni
			return null;
		}
		
		com.infomatiq.jsi.Rectangle objectRect = getObjectRTreeRectangle(coords.x, coords.y, objectToRemove);
		
		objectRectangles.remove(objectToRemove.id);
		mapObjectsIndex.delete(objectRect, objectToRemove.id);
		
		map.removeMapObject(coords.x, coords.y);
		
		return objectToRemove;
	}
	
	private Rectangle getObjectRectangle(int x, int y, MapObject object)
	{
		Point center = new Point(x * MapTile.imageSizeWidth,
				y * MapTile.imageSizeHeight / 2 + heightOfSkyImage);
			// stred policka
		
		if (y % 2 == 0)
		{
			center.x += MapTile.imageSizeWidth / 2;
		}
		
		BufferedImage look = object.look_on_map;
		int leftTopX = center.x - look.getWidth() / 2;
		int leftTopY = center.y - look.getHeight();

		return new Rectangle(leftTopX, leftTopY, look.getWidth(), look.getHeight());
	}
	
	private com.infomatiq.jsi.Rectangle getObjectRTreeRectangle(int x, int y, MapObject object)
	{
		Rectangle objectRect = getObjectRectangle(x, y, object);
		return new com.infomatiq.jsi.Rectangle(objectRect.x, objectRect.y,
				objectRect.x + objectRect.width, objectRect.y
						+ objectRect.height);
	}
	
	/* ******************************
	 * METODY MOUSELISTENERU:		*
	 ********************************/
	private class MapMouseAdapter extends MouseAdapter
	{
		@Override
		public void mouseEntered(MouseEvent e)
		{
			repaint();
		}

		@Override
		public void mouseExited(MouseEvent e)
		{
			Main.gui.statusBar.showCurrentMousePosition(null);
			repaint();
		}

		Point mousePressedPoint = null;		
		@Override
		public void mousePressed(MouseEvent e)
		{
			if (Main.gui.selectedTool == GUI.Tool.SELECTION_TOOL)
			{
				mousePressedPoint = e.getPoint();
				com.infomatiq.jsi.Point clickedPoint = new com.infomatiq.jsi.Point(
						mousePressedPoint.x, mousePressedPoint.y);
				
				ExecutedTIntProcedure proc = new ExecutedTIntProcedure();				
				mapObjectsIndex.nearest(clickedPoint, proc, 0.0f);
				// zjisti, jestli bod kliknuti lezi v nekterem z obdelniku (ve
				// vzdalenosti 0 od obdelniku)
				if (proc.executed == false)
				{	// bylo kliknuto mezi vsechny objekty
					selected_object = null;
				}
			}
			else if (Main.gui.selectedTool == GUI.Tool.TERRAIN_TOOL)
			{
				Point centre = tileFromPixelCoords(e.getPoint());
				LinkedList<Point> selectedTiles = getSelectedTiles(
						Main.gui.leftPanel.terrainSettings.brushSize, centre);

				if (selectedTiles != null)
				{
					new TerrainChange(selectedTiles);
				}
			}

			repaint();
			Main.gui.leftPanel.miniMap.repaint();
		}
		
		@Override
		public void mouseClicked(MouseEvent e)
		{
			if (selected_object != null && e.getClickCount() == 2)
			{
				if (Main.gui.selectedTool == GUI.Tool.SELECTION_TOOL)
				{
					new ObjectPropertiesDialog(selected_object);
				}
			}
		}
		
		class ExecutedTIntProcedure implements TIntProcedure
		{
			boolean executed = false;

			@Override
			public boolean execute(int id)
			{				
				com.infomatiq.jsi.Rectangle r = objectRectangles.get(id);

				Point bottomMid = new Point(
						(int) (r.minX + (r.maxX - r.minX) / 2), (int) r.maxY);
				

				if (executed == false		// v miste kliknuti je jediny objekt
						|| executed == true && selected_object_position.y < bottomMid.y)
					// nebo pokud se v miste kliknuti prekryva vice objektu, je vybrany
					// ten v popredi
				{	
					selected_object_position = bottomMid;
					selected_object = map.getTile(tileFromPixelCoords(bottomMid)).map_object;
				}

				executed = true;

				repaint();
				return true;
			}
		}

		@Override
		public void mouseReleased(MouseEvent e)
		{
			if (Main.gui.selectedTool == GUI.Tool.SELECTION_TOOL)
			{
				if (dragged_object != null)
				{
					Point newPosition = e.getPoint();
					newPosition.y += dragged_object.look_on_map.getHeight() / 2;
					// objekt je drzen vprostred, ale ma byt pridan na policko vespod					
					new ObjectMovement(dragged_object_old_position, newPosition, dragged_object);
					dragged_object = null;
					dragged_object_old_position = null;
				}
			}
			else if (Main.gui.selectedTool == GUI.Tool.TERRAIN_TOOL)
			{

			}

			if (actionController.getLastAction() != null
					&& !actionController.getLastAction().changedSomething())
			{
				actionController.removeLastAction();
			}
			
			repaint();
		}

		Point lastPosition = null;
		// posledni pozice je potreba pri zmene terenu, aby bylo mozne urcit,
		// pres ktera policka mys prejela, pokud byl pohyb moc rychly

		final int minDrag = 5;
		@Override
		public void mouseDragged(MouseEvent e)
		{
			if (Main.gui.selectedTool == GUI.Tool.SELECTION_TOOL)
			{
				if (mousePressedPoint == null)
				{	// objekt je jiz prepnuty do DnD rezimu
					repaint();
					return;
				}
				
				if (selected_object == null)
				{	// je tazeno mysi, ale na zacatku mys nebyla na zadnem objektu
					return;
				}
				
				Point mousePos = e.getPoint();
				
				if (mousePressedPoint.distance(mousePos) > minDrag)
				{
					dragged_object_old_position = selected_object_position;
					
					Point selectedObjectCoords = tileFromPixelCoords(selected_object_position);
					dragged_object = map.getTile(selectedObjectCoords.x, selectedObjectCoords.y).map_object;
					
					selected_object_position = null;
					mousePressedPoint = null;
				}
			}
			else if (Main.gui.selectedTool == GUI.Tool.TERRAIN_TOOL)
			{ // pri rychlem tahu mysi by mohla byt preskocena nektera
				// policka, proto je potreba pocitat trajektorii mysi a menit
				// vsechna policka po ceste
				LinkedList<Point> centres = getLineFromPoints(lastPosition,
						e.getPoint());

				LinkedList<Point> centreCoordsList = tilesFromPixelCoords(centres);
				// prevedeni vsech bodu zadanych v pixelech na souradnice
				// policek na mape

				LinkedList<Point> allSelectedTiles = new LinkedList<Point>();
				for (Point coords : centreCoordsList)
				{
					LinkedList<Point> selectedTiles = getSelectedTiles(
							Main.gui.leftPanel.terrainSettings.brushSize,
							coords);

					for (Point tile : selectedTiles)
					{
						if (!allSelectedTiles.contains(tile))
						{
							allSelectedTiles.add(tile);
						}
					}
				}

				if (allSelectedTiles != null)
				{
					((TerrainChange) actionController.getLastAction())
							.addTiles(allSelectedTiles);
				}
			}
			lastPosition = e.getPoint();

			repaint();
			Main.gui.leftPanel.miniMap.repaint();
		}

		@Override
		public void mouseMoved(MouseEvent e)
		{
			Point p = tileFromPixelCoords(e.getPoint());
			if (p == null || e.getY() < heightOfSkyImage)
			{ // nic se ve stavovem radku nezobrazi
				Main.gui.statusBar.showCurrentMousePosition(null);
			}
			else
			{
				Main.gui.statusBar.showCurrentMousePosition(toUserCoords(p));
			}
			
			lastPosition = e.getPoint();

			repaint();
		}

		private LinkedList<Point> getLineFromPoints(Point first, Point second)
		{
			LinkedList<Point> line = new LinkedList<Point>();

			if (first == null)
			{
				line.add(second);
				return line;
			}
			else if (second == null)
			{
				line.add(first);
				return line;
			}

			int maxDist;
			if (Math.abs(first.x - second.x) > Math.abs(first.y - second.y))
			{
				maxDist = Math.abs(first.x - second.x);
			}
			else
			{
				maxDist = Math.abs(first.y - second.y);
			}


			for (int i = 0; i < maxDist; ++i)
			{
				double x = first.x
						- ((double) (first.x - second.x) / maxDist * i);
				double y = first.y
						- ((double) (first.y - second.y) / maxDist * i);

				Point p = new Point((int) x, (int) y);
				if (!line.contains(p))
				{
					line.add(p);
				}
			}

			return line;
		}
	}
	
	
	/* **************************
	 *  Metody Scrollable:		*
	 ****************************/
	@Override
	public Dimension getPreferredScrollableViewportSize()
	{
		return getPreferredSize();
	}

	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect,
			int orientation, int direction)
	{
		if (orientation == SwingConstants.HORIZONTAL)
		{
			return 20 * MapTile.imageSizeHeight;
		}
		else
		{
			return 20 * MapTile.imageSizeWidth;
		}
	}

	@Override
	public boolean getScrollableTracksViewportHeight()
	{
		return false;
	}

	@Override
	public boolean getScrollableTracksViewportWidth()
	{
		return false;
	}

	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect,
			int orientation, int direction)
	{
		if (orientation == SwingConstants.HORIZONTAL)
		{
			return MapTile.imageSizeHeight;
		}
		else
		{
			return MapTile.imageSizeWidth;
		}
	}
}
