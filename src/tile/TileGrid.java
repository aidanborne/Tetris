package tetris.tile;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;

public final class TileGrid extends JComponent {
	private static final long serialVersionUID = 1L;

	public static final int SIZE = 36;
	
	public static final int PADDING = 5;
	
	private TileGroup tileGroup;
	
	public TileGrid(TileGroup tileGroup) {
		super();
		setTileGroup(tileGroup);
	}
	
	public TileGroup getTileGroup() {
		return tileGroup;
	}
	
	public void setTileGroup(TileGroup tileGroup) {
		this.tileGroup = tileGroup;
	}
	
	@Override
	public Dimension getPreferredSize() {
		Dimension gridSize = tileGroup != null ? tileGroup.getGridSize() : new Dimension(4, 4);
		
		int width = gridSize.width * SIZE;
		int height = gridSize.height * SIZE;
		
		return new Dimension(width + PADDING, height + PADDING);
	}

	@Override
	public void paintComponent(Graphics graphics) {
		if (tileGroup == null)
			return;
		
		setSize(getPreferredSize());
		
		//graphics.setColor(Color.black);
		//graphics.fillRect(0, 0, graphics.getClipBounds().width, graphics.getClipBounds().height);
		
		Dimension gridSize = tileGroup.getGridSize();
		
		for (int x = 0; x < gridSize.width; x++) {
			for (int y = 0; y < gridSize.height; y++) {
				Color color = tileGroup.getTileColor(x, y);
				
				if (color == null)
					continue;
				
				graphics.setColor(color);
				
				graphics.fillRect(
					x * SIZE + PADDING, y * SIZE + PADDING,
					SIZE - PADDING, SIZE - PADDING
				);
			}
		}
	}
}
