
	
	Dimension thumbnailSize = new Dimension(50, 50);
	@Override
	public Dimension getPreferredSize()
	{
		int width = imagesInRow * (thumbnailSize.width + gapSize.width)
				+ gapSize.width;
		// na kazde strane je mezera o velikosti gapSize.width
	
		int height = (int) Math.ceil((double) mapObjects.length / imagesInRow)
				* (thumbnailSize.height + gapSize.height) + gapSize.height;
		// na kazde strane je mezera o velikosti gapSize.height

		return new Dimension(width, height);
	}

	@Override
	public Dimension getMinimumSize()
	{
		return new Dimension(50 + 2 * 5,
				50 + 2 * 5);
	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		paintObjects(g);
		paintDraggedObject(g);
	}
	
	private void paintObjects(Graphics g)
	{
		int panelWidth = getParent().getParent().getWidth();
		// sirka rightScrollPane
		imagesInRow = (panelWidth - gapSize.width)
				/ (thumbnailSize.width + gapSize.width);
		int rows = (int)Math.ceil((double)mapObjects.length / imagesInRow);

		for (int row = 0; row < rows; ++row)
		{
			for (int col = 0; col < imagesInRow; ++col)
			{
				int index = row * imagesInRow + col;
				if (index >= mapObjects.length)
				{	// na poslednim radku nemusi byt vsechny obrazky
					break;
				}
				
				int x = col * (thumbnailSize.width + gapSize.width)
						+ gapSize.width;
				int y = row * (thumbnailSize.height + gapSize.height)
						+ gapSize.height;
				BufferedImage drawnImage = mapObjects[index].look;
				
				paintThumbnailImage(x, y, drawnImage, g);
			}
		}
	}
	
	private void paintThumbnailImage(int x, int y, BufferedImage image, Graphics g)
	{
		int imageWidth = thumbnailSize.width;
		int imageHeight = thumbnailSize.height;
		
		if (image.getHeight() > image.getWidth())
		{
			double sizeRatio = (double) image.getWidth()
					/ image.getHeight();
			x += (1 - sizeRatio) * imageWidth / 2;
			imageWidth *= sizeRatio;
		}
		else if (image.getWidth() > image.getHeight())
		{
			double sizeRatio = (double) image.getHeight()
					/ image.getWidth();
			y += (1 - sizeRatio) * imageHeight / 2;
			imageHeight *= sizeRatio;
		}
		
		g.drawImage(image,
				x, y,
				x + imageWidth, y + imageHeight,
				0, 0,
				image.getWidth(), image.getHeight(), null);

	}

	private boolean imageIsDragged = false;	// priznak, ze je nejaky obrazek presouvan
	BufferedImage draggedImage = null;	// prave presouvany obrazek
	private int draggedX, draggedY;	// soucasna pozice obrazku
	void imageDragged(int x, int y)
	{	// nastavi nove souradnice presouvanemu obrazku
		draggedX = x;
		draggedY = y;
	}
	
	private void paintDraggedObject(Graphics g)
	{
		if (imageIsDragged == true)
		{
			paintThumbnailImage(draggedX, draggedY, draggedImage, g);
		}
	}
	
	
	class RightPanelMouseAdapter extends MouseAdapter
	{
		private int relPosX = 0, relPosY = 0;
		
		@Override
		public void mouseDragged(MouseEvent e)
		{
			RightPanel rightPanel = (RightPanel)e.getComponent();
			rightPanel.imageDragged(e.getX() - relPosX, e.getY() - relPosY);
			rightPanel.repaint();
		}
		
		@Override
		public void mousePressed(MouseEvent e)
		{
			RightPanel rightPanel = (RightPanel)e.getComponent();
			Point mouseCoords = e.getPoint();
			// prevod na cislo obrazku:
			int imageCol = (mouseCoords.x - gapSize.width)
					/ (RightPanel.thumbnailSize.width + RightPanel.gapSize.width);
			int imageRow = (mouseCoords.y - gapSize.height)
					/ (RightPanel.thumbnailSize.height + RightPanel.gapSize.height);
			// odecita se gapSize kvuli mezere od okraju
			int imageIndex = rightPanel.imagesInRow * imageRow + imageCol;		
			
			if (imageIndex >= rightPanel.mapObjects.length)
			{	// kliknuti bylo mimo obrazky na konci seznamu
				return;
			}

			relPosX = (mouseCoords.x - gapSize.width)
					- imageCol
					* (RightPanel.thumbnailSize.width + RightPanel.gapSize.width);
			relPosY = (mouseCoords.y - gapSize.height)
					- imageRow
					* (RightPanel.thumbnailSize.height + RightPanel.gapSize.height);
			
			if (relPosX > thumbnailSize.width || relPosY > thumbnailSize.height)
			{	// mys je v mezere mezi obrazky
				return;
			}
			
			rightPanel.draggedImage = rightPanel.mapObjects[imageIndex].look;
			rightPanel.imageDragged(e.getX() - relPosX, e.getY() - relPosY);
			rightPanel.imageIsDragged = true;
			rightPanel.repaint();
	}
		
		@Override
		public void mouseReleased(MouseEvent e)
		{
			RightPanel rightPanel = (RightPanel)e.getComponent();
			rightPanel.imageIsDragged = false;
			rightPanel.repaint();
		}
		
		@Override
		public void mouseExited(MouseEvent e)
		{
			RightPanel rightPanel = (RightPanel)e.getComponent();
			rightPanel.imageIsDragged = false;
			
			Tab tabType = ((RightScrollPane)rightPanel.getParent().getParent()).tabType;
			MapObject draggedObject = tabType.mapObject;
			draggedObject.loadLook(rightPanel.draggedImage);
			Main.gui.rightTabbedPane.draggedObject = draggedObject;
			
			rightPanel.repaint();
		}
	}