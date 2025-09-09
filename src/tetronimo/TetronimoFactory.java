package tetris.tetronimo;

import java.awt.Color;

public final class TetronimoFactory {
	// tiles[rotation][x][y]
	private final Color[][][] tiles = new Color[Tetronimo.ROTATION_COUNT][4][4];
	
	private final WallKickData wallKicks;
	
	private final boolean tPiece;
	
	public TetronimoFactory(Color color, float[] center, WallKickData wallKicks, boolean tPiece, boolean[][] grid) {
		this.tPiece = tPiece;
		
		this.wallKicks = wallKicks;
		
		float cx = center[0] - 0.5f, cy = center[1] - 0.5f;
		
		for (int x = 0; x < grid.length; x++) {
			if (grid[x] == null)
				continue;
			
			for (int y = 0; y < grid[x].length; y++) {
				if (!grid[x][y])
					continue;
				
				double nx = y - cx, ny = x - cy;
				double temp;
				
				for (int i = 0; i < Tetronimo.ROTATION_COUNT; i++) {
					if (i != 0) {
						temp = nx;
						nx = -ny;
						ny = temp;
					}
					
					//System.out.println(x + " " + y);
					//System.out.println((nx + cx) + " " + (ny + cy));
					//System.out.println(i);
					tiles[i][(int) (nx + cx)][(int) (ny + cy)] = color;
				}
			}
		}
	}
	
	public Tetronimo toTetronimo() {
		return new Tetronimo(tiles, wallKicks, tPiece);
	}
}
