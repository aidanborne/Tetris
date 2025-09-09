package tetris.tetronimo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

import tetris.tile.TileGroup;

public final class Tetronimo implements TileGroup {
	public static final int ROTATION_COUNT = 4;
	
	private final Color[][][] tiles;
	
	private final WallKickData wallKicks;
	
	private Integer wallKickUsed;
	
	private Point position = new Point(0, 0);
	
	private int rotation = 0;
	
	// laughs in boolean flags
	private boolean tPiece;
	
	private boolean held;
	
	public Tetronimo(Color[][][] tiles, WallKickData wallKicks, boolean tPiece) {
		this.tiles = tiles;
		this.wallKicks = wallKicks;
		this.tPiece = tPiece;
	}
	
	public void setWallKickUsed(Integer wallKick) {
		this.wallKickUsed = wallKick;
	}
	
	public Integer getWallKickUsed() {
		return this.wallKickUsed;
	}

	public Color[][] getTiles() {
		return tiles[rotation];
	}
	
	@Override
	public Dimension getGridSize() {
		return new Dimension(4, 4);
	}
	
	@Override
	public Color getTileColor(int x, int y) {
		return tiles[rotation][x][y];
	}
	
	public Point getPosition() {
		return position;
	}
	
	public void setPosition(Point point) {
		this.position = point;
	}
	
	public int getRotation() {
		return rotation;
	}
	
	public int[][] setRotation(int rotation) {
		int oldRotation = this.rotation;
		this.rotation = rotation % ROTATION_COUNT;
		
		if (this.rotation < 0)
			this.rotation += ROTATION_COUNT;
		
		return wallKicks.get(oldRotation, this.rotation);
	}
	
	public int[][] rotateRight() {
		return setRotation(rotation + 1);
	}
	
	public int[][] rotateLeft() {
		return setRotation(rotation - 1);
	}
	
	public boolean canTSpin() {
		return tPiece;
	}
	
	public void markHeld() {
		held = true;
	}
	
	public boolean wasHeld() {
		return held;
	}
}
