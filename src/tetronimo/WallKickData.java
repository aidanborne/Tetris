package tetris.tetronimo;

public final class WallKickData {
	// data[oldRotation][newRotation][i][0 | 1]
	private final int[][][][] data = new int[Tetronimo.ROTATION_COUNT][Tetronimo.ROTATION_COUNT][][];
	
	public WallKickData put(int oldRotation, int rotation, int[][] wallKicks) {
		this.data[oldRotation][rotation] = wallKicks;
		return this;
	}
	
	public int[][] get(int oldRotation, int rotation) {
		return this.data[oldRotation][rotation];
	}
}
