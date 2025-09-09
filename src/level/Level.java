package tetris.level;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Random;

import tetris.tetronimo.Tetronimo;
import tetris.tetronimo.TetronimoGenerator;
import tetris.tile.TileGroup;

public final class Level implements TileGroup {
	private static final Random random = new Random();
	
	public static final int WIDTH = 10;
	
	public static final int HEIGHT = 15;
	
	public static final int HIDDEN_HEIGHT = 2;
	
	private static Point randomPosition() {
		return new Point(random.nextInt(WIDTH), 0);
	}
	
	private final Color[][] tiles = new Color[WIDTH][HEIGHT + HIDDEN_HEIGHT];
	
	private int score;
	private boolean gameOver;
	
	private Tetronimo tetronimo;
	
	private Tetronimo heldTetronimo;
	
	public Level() {
		resetTetronimo();
	}
	
	public int getScore() {
		return score;
	}
	
	public boolean isGameOver() {
		return gameOver;
	}
	
	public Tetronimo getTetronimo() {
		return tetronimo;
	}
	
	public Tetronimo getNextTetronimo() {
		return TetronimoGenerator.peek();
	}
	
	public Tetronimo getHeldTetronimo() {
		return heldTetronimo;
	}
	
	private void findValidPosition() {
		tetronimo.setPosition(randomPosition());
		
		while (!checkTetronimo())
			tetronimo.setPosition(randomPosition());
	}
	
	public void resetTetronimo() {
		tetronimo = TetronimoGenerator.next();
		findValidPosition();
	}
	
	public void swapHeldTetronimo() {
		if (tetronimo.wasHeld())
			return;
		
		Tetronimo temp = tetronimo;
		
		if (heldTetronimo == null)
			tetronimo = TetronimoGenerator.next();
		else
			tetronimo = heldTetronimo;
		
		findValidPosition();
		
		temp.markHeld();
		heldTetronimo = temp;
	}

	@Override
	public Dimension getGridSize() {
		return new Dimension(WIDTH, HEIGHT);
	}
	
	@Override
	public Color getTileColor(int x, int y) {
		return tiles[x][y + HIDDEN_HEIGHT];
	}
	
	private static boolean isWithinBounds(int x, int y) {
		return x >= 0 && x < WIDTH && y >= 0 && y < (HEIGHT + HIDDEN_HEIGHT);
	}
	
	private boolean isOccupied(int x, int y) {
		return !isWithinBounds(x, y) || tiles[x][y] != null;
	}
	
	private boolean checkTetronimo() {
		Color[][] tiles = tetronimo.getTiles();
		Point position = tetronimo.getPosition();
		
		int px = position.x;
		int py = position.y;
		
		for (int x = 0; x < tiles.length; x++) {
			for (int y = 0; y < tiles[x].length; y++) {
				if (tiles[x][y] == null)
					continue;
				
				if (isOccupied(x + px, y + py))
					return false;
			}
		}
		
		return true;
	}
	
	public boolean moveTetronimo(int x, int y) {
		Point position = tetronimo.getPosition();
		tetronimo.setPosition(new Point(position.x + x, position.y + y));
		
		if (!checkTetronimo()) {
			tetronimo.setPosition(position);
			return false;
		}
		
		tetronimo.setWallKickUsed(null);
		
		return true;
	}
	
	public boolean rotateTetronimo(boolean clockwise) {
		int rotation = tetronimo.getRotation();
		Point position = tetronimo.getPosition();
		
		int[][] wallKicks = tetronimo.setRotation(rotation + (clockwise ? 1 : -1));

		for (int i = 0; i < wallKicks.length; i++) {
			int[] offset = wallKicks[i];
			
			tetronimo.setPosition(new Point(position.x + offset[0], position.y + offset[1]));
			
			if (checkTetronimo()) {
				tetronimo.setWallKickUsed(i);
				return true;
			}
		}

		tetronimo.setRotation(rotation);
		tetronimo.setPosition(position);
		return false;
	}
	
	public enum TSpin {
		NONE,
		MINI,
		PROPER
	}
	
	private int getOccupied(int[][] points) {
		int count = 0;
		
		for (int[] point : points) {
			if (isOccupied(point[0], point[1]))
				count++;
		}
		
		return count;
	}
	
	public void placeTetronimo() {
		Color[][] tiles = tetronimo.getTiles();
		Point position = tetronimo.getPosition();
		
		int px = position.x;
		int py = position.y;
		
		for (int x = 0; x < tiles.length; x++) {
			for (int y = 0; y < tiles[x].length; y++) {
				if (tiles[x][y] != null)
					this.tiles[x + px][y + py] = tiles[x][y];
			}
		}
		
		int[] linesToRemove = new int[4];
		int linesCleared = 0;
		
		// Java has labels? no way
		row:
		for (int y = 0; y < HEIGHT + HIDDEN_HEIGHT && linesCleared < 4; y++) {
			for (int x = 0; x < WIDTH; x++) {
				if (this.tiles[x][y] == null)
					continue row;
			}
			
			linesToRemove[linesCleared++] = y;
		}
		
		TSpin tSpin = TSpin.NONE;
		
		if (tetronimo.canTSpin() && tetronimo.getWallKickUsed() != null
				&& getOccupied(new int[][] { { 2 + px, py }, { px, 2 + py }, { 2 + px, 2 + py }, { px, py } }) >= 3) {
			tSpin = TSpin.PROPER;
			
			int wallKick = tetronimo.getWallKickUsed();
			
			if (wallKick > 0 && wallKick < 4) {
				int[][] miniPoints;
				
				switch (tetronimo.getRotation()) {
					case 0: miniPoints = new int[][] { { 0, 0 }, { 2, 0 } };
					case 1: miniPoints = new int[][] { { 2, 0 }, { 2, 2 } };
					case 2: miniPoints = new int[][] { { 0, 2 }, { 2, 2 } };
					case 3: miniPoints = new int[][] { { 0, 0 }, { 0, 2 } };
					default: miniPoints = new int[0][];
				}
				
				if (getOccupied(miniPoints) != 2)
					tSpin = TSpin.MINI;
			}
		}
		
		switch (tSpin) {
			case NONE:
				if (linesCleared > 0)
					score += linesCleared == 4 ? 800 : 100 + 200 * (linesCleared - 1);
				break;
			case MINI:
				score += 100 * Math.pow(2, linesCleared);
				break;
			case PROPER:
				score += 400 * (linesCleared + 1);
				break;
		}
		
		for (int i = 0; i < linesCleared; i++) {
			for (int j = linesToRemove[i]; j > 0; j--) {
				for (int x = 0; x < WIDTH; x++) {
					this.tiles[x][j] = this.tiles[x][j - 1]; 
				}
			}
		}
		
		for (int x = 0; x < WIDTH; x++)
			this.tiles[x][0] = null;
		
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HIDDEN_HEIGHT; y++) {
				if (this.tiles[x][y] != null) {
					gameOver = true;
					return;
				}
			}
		}
		
		resetTetronimo();
	}
	
	public void updateTetronimo() {
		if (!moveTetronimo(0, 1))
			placeTetronimo();
	}
}
