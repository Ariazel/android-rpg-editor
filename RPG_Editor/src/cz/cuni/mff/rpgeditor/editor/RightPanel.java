package cz.cuni.mff.rpgeditor.editor;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceAdapter;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import cz.cuni.mff.rpgeditor.editor.RightTabbedPane.Tab;
import cz.cuni.mff.rpgeditor.game.MapObject;


/**
 * Pravy panel okna editoru. Na nem jsou umisteny vsechny objekty, ktere jde
 * pridavat do mapy - spojenci, nepratele, teren... Objekty jsou do mapy
 * pridavany tahem tlacitka mysi a naslednym umistenim do mapy.
 */
public class RightPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	public RightPanel(Tab tab)
	{
		setLayout(new RightPanelFlowLayout(FlowLayout.LEADING));
		
		String[] imagePaths = new File(tab.path).list();
		// jmena obrazku
		for (int i = 0; i < imagePaths.length; ++i)
		{	// cela cesta k obrazku
			String s = tab.path + "/" + imagePaths[i]; 
			imagePaths[i] = s;
		}
		
		for (int i = 0; i < imagePaths.length; ++i)
		{
			// tvorba objektu typu daneho v enumu Tab dediciho
			// od tridy MapObject:
			MapObject mapObject = null;
			
			try
			{
				Constructor<? extends MapObject> ctor = tab.mapObject
						.getDeclaredConstructor(String.class);
				ctor.setAccessible(true);
				mapObject = ctor.newInstance(imagePaths[i]);
			}
			catch (InstantiationException e)
			{
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
			catch (InvocationTargetException e)
			{
				e.printStackTrace();
			}
			catch (NoSuchMethodException e)
			{
				e.printStackTrace();
			}
			
			MapObjectJLabel mapObjectLabel = new MapObjectJLabel(mapObject);
			add(mapObjectLabel);
			
			DragSource ds = DragSource.getDefaultDragSource();
			ds.createDefaultDragGestureRecognizer(mapObjectLabel,
					DnDConstants.ACTION_COPY, new RightbjectDragGestureListener(
							new MapObjectDropTargetListener()));
		}
	}		
	
	
	TransferableMapObject transferedMapObject = null;	
	/**
	 * Trida zajistujici zacatek dragu objektu.
	 */
	private class RightbjectDragGestureListener extends DragSourceAdapter
			implements DragGestureListener
	{
		MapObjectDropTargetListener dropListener;
		
		public RightbjectDragGestureListener(MapObjectDropTargetListener dropListener)
		{
			this.dropListener = dropListener;
		}

		@Override
		public void dragGestureRecognized(DragGestureEvent event)
		{
			Cursor cursor = null;
			MapObjectJLabel mapObjectLabel = (MapObjectJLabel) event.getComponent();

			if (event.getDragAction() == DnDConstants.ACTION_COPY)
			{
				cursor = DragSource.DefaultCopyDrop;
			}
			MapObject mapObject = mapObjectLabel.mapObject;
			
			MapPanel mapPanel = Main.gui.mapTabbedPane.getDisplayedMapPanel();
			new DropTarget(mapPanel, DnDConstants.ACTION_COPY, dropListener,
					true, null);

			transferedMapObject = new TransferableMapObject(mapObject);
			event.startDrag(cursor, transferedMapObject, this);
		}
		
		public void dragDropEnd(DragSourceDropEvent evt)
		{
			transferedMapObject = null;
		}
	}
	

	DataFlavor mapObjectFlavor = new DataFlavor(MapObject.class, "MapObject");
	/**
	 * Presouvana data jsou zabalena v teto tride.
	 */
	protected class TransferableMapObject implements Transferable
	{
		private MapObject mapObject;
		
		public TransferableMapObject(MapObject mapObject)
		{
			this.mapObject = mapObject;
		}
		
		@Override
		public Object getTransferData(DataFlavor flavor)
				throws UnsupportedFlavorException
		{
			if (flavor.equals(mapObjectFlavor))
				return mapObject;
			else
				throw new UnsupportedFlavorException(flavor);
		}

		@Override
		public DataFlavor[] getTransferDataFlavors()
		{
			return new DataFlavor[] { mapObjectFlavor };
		}

		@Override
		public boolean isDataFlavorSupported(DataFlavor flavor)
		{
			return flavor.equals(mapObjectFlavor);
		}
	}	

	/**
	 * Trida zajistujici drop pretahovaneho objektu.
	 */
	private class MapObjectDropTargetListener implements DropTargetListener
	{
		@Override
		public void drop(DropTargetDropEvent event)
		{
			transferedMapObject = null;
			
			try
			{
				Transferable tr = event.getTransferable();
				MapObject mapObject = (MapObject) tr.getTransferData(mapObjectFlavor);

				if (event.isDataFlavorSupported(mapObjectFlavor))
				{
					event.acceptDrop(DnDConstants.ACTION_COPY);
					
					Point dropLoc = event.getLocation();
					dropLoc.y += (mapObject.look.getHeight() / 2);
					// objekt je drzen veprostred, ale chyta se spodkem obrazku
					
					new ObjectAddition(dropLoc, mapObject);
					
					event.dropComplete(true);
					return;
				}
				event.rejectDrop();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				event.rejectDrop();
			}
		}

		@Override
		public void dragEnter(DropTargetDragEvent e)
		{
			Main.gui.mapTabbedPane.repaint();
			
		}
		
		@Override
		public void dragExit(DropTargetEvent e)
		{
			Main.gui.mapTabbedPane.repaint();
			
		}

		@Override
		public void dragOver(DropTargetDragEvent e)
		{
			Main.gui.mapTabbedPane.repaint();
			
		}

		@Override
		public void dropActionChanged(DropTargetDragEvent e)
		{	}
	}
}


/**
 * Label zobrazujici jeden objekt v pravem panelu.
 */
class MapObjectJLabel extends JLabel
{
	private static final long serialVersionUID = 1L;
	
	MapObject mapObject;
	final static Dimension thumbnailSize = new Dimension(50, 50);
	// velikost nahledu objektu v pravem panelu

	
	MapObjectJLabel(MapObject o)
	{
		mapObject = o;
		setBorder(new LineBorder(Color.BLACK, 1));
	}
	
	@Override
	public Dimension getPreferredSize()
	{
		return thumbnailSize;
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		BufferedImage image = mapObject.look;
		
		int x = 0;
		int y = 0;
		int imageWidth = thumbnailSize.width;
		int imageHeight = thumbnailSize.height;
		
		if (image.getHeight() > image.getWidth())
		{
			double sizeRatio = (double) image.getWidth()
					/ image.getHeight();
			x = (int)((1 - sizeRatio) * imageWidth / 2);
			imageWidth *= sizeRatio;
		}
		else if (image.getWidth() > image.getHeight())
		{
			double sizeRatio = (double) image.getHeight()
					/ image.getWidth();
			y = (int)((1 - sizeRatio) * imageHeight / 2);
			imageHeight *= sizeRatio;
		}
		
		g.drawImage(image,
				x, y,
				x + imageWidth, y + imageHeight,
				0, 0,
				image.getWidth(), image.getHeight(), null);
	}
}

/**
 * Upravena verze FlowLayoutu, aby se prvky v panelu ulozenem ve scrollpane
 * neskladaly do jedne rady, ale v pripade potreby se vytvorila druha rada.
 */
class RightPanelFlowLayout extends FlowLayout
{
	private static final long serialVersionUID = 1L;

	public RightPanelFlowLayout(int align)
	{
		super(align);
	}

	public Dimension minimumLayoutSize(Container target)
	{ 	// minimalni velikost se rovna velikosti libovolne komponenty, protoze
		// jsou vsechny komponenty stejne velke
		int componentCount = target.getComponentCount();
		
		if (componentCount > 0)
		{
			Dimension componentSize = target.getComponent(0).getPreferredSize();
			return new Dimension(componentSize.width + 2 * getHgap(),
					componentSize.height + 2 * getVgap());
		}
		else
		{
			return new Dimension(0, 0);
		}
	}

	public Dimension preferredLayoutSize(Container target)
	{ 	// protoze jsou vsechny komponenty stejne velke, neni treba je vsechny
		// prochazet, staci velikost jedne komponenty
		int componentCount = target.getComponentCount();
		
		if (componentCount == 0)
		{
			return new Dimension(0, 0);
		}
		Dimension componentSize = target.getComponent(0).getPreferredSize();
		
		Insets insets = target.getInsets();
		if (insets == null)
		{
			insets = new Insets(0, 0, 0, 0);
		}

		int componentsInRow, rows;
		Dimension gapSize = new Dimension(getHgap(), getVgap());

		if (target.getWidth() == 0)
		{	// target jeste neni vykreslen, preferovany pocet komponent na radek je 4
			// TODO: vyresit, proc ma target nekdy o komponentu min
			componentsInRow = 5;
		}
		else
		{
			componentsInRow = (target.getWidth() - 2 * gapSize.width - insets.left - insets.right)
					/ (componentSize.width + gapSize.width);
			if (componentsInRow == 0)
			{
				componentsInRow = 1;
			}
		}
		rows = (int) Math.ceil((double) componentCount / componentsInRow);

		int preferredWidth = componentsInRow * (componentSize.width + gapSize.width)
				+ gapSize.width	+ insets.left + insets.right;
		int preferredHeight = rows * (componentSize.height + gapSize.height)
				+ gapSize.height + insets.top + insets.bottom;

		return new Dimension(preferredWidth, preferredHeight);
	}
}