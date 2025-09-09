package tetris.tile;

import java.awt.Color;
import java.awt.Dimension;

public interface TileGroup {
	//public Color[][] getTiles();
	
	public Dimension getGridSize();
	
	public Color getTileColor(int x, int y);
}
