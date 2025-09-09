package tetris.tetronimo;

import java.awt.Color;
import java.util.Random;

// guarantees you get a piece once every 7
public final class TetronimoGenerator {
	private final static class TetronimoBag {
		private static final TetronimoFactory[] TETRONIMOS;
		
		private static final int PIECE_COUNT = 7;
		
		private static final Random random = new Random();
		
		// defining these wall kicks was so fun
		static {
			WallKickData I_wallKicks = new WallKickData();
			I_wallKicks.put(0, 1, new int[][] { { 0, 0 }, { -2, 0 }, { 1, 0 }, { -2, -1}, { 1, 2 } })
				.put(1, 0, new int[][] { { 0, 0 }, { 2, 0 }, { -1, 0 }, { 2, 1}, { -1, -2} })
				.put(1, 2, new int[][] { { 0, 0 }, { -1, 0 }, { 2, 0 }, { -1, 2}, { 2, -1} })
				.put(2, 1, new int[][] { { 0, 0 }, { 1, 0 }, { -2, 0 }, { 1, -2}, { -2, 1} })
				.put(2, 3, new int[][] { { 0, 0 }, { 2, 0 }, { -1, 0 }, { 2, 1}, { -1, -2} })
				.put(3, 2, new int[][] { { 0, 0 }, {-2, 0 }, { 1, 0 }, { -2, -1}, { 1, 2} })
				.put(3, 0, new int[][] { { 0, 0 }, { 1, 0 }, { -2, 0 }, { 1, -2}, { -2, 1} })
				.put(0, 3, new int[][] { { 0, 0 }, { -1, 0 }, { 2, 0 }, { -1, 2}, { 2, -1} });
			
			TetronimoFactory I = new TetronimoFactory(Color.CYAN, new float[] { 2, 2 }, I_wallKicks, false, new boolean[][] {
				null,
				{ true, true, true, true }
			});
			
			WallKickData normalWallKicks = new WallKickData();
			normalWallKicks.put(0, 1, new int[][] { { 0, 0 }, { -1, 0 }, { -1, 1 }, { 0, 2 }, { -1, -2 } })
				.put(1, 0, new int[][] { { 0, 0 }, { 1, 0 }, { 1, -1 }, { 0, 2}, { 1, 2 } })
				.put(1, 2, new int[][] { { 0, 0 }, { 1, 0 }, { 1, -1 }, { 0, 2 }, { 1, 2 } })
				.put(2, 1, new int[][] { { 0, 0 }, { -1, 0 }, { -1, 1 }, { 0, -2 }, { -1, -2 } })
				.put(2, 3, new int[][] { { 0, 0 }, { 1, 0 }, { 1, 1 }, { 0, -2 }, {1, -2 } })
				.put(3, 2, new int[][] { { 0, 0 }, { -1, 0 }, { -1, -1 }, { 0, 2 }, { -1, 2 } })
				.put(3, 0, new int[][] { { 0, 0 }, { -1, 0 }, { -1, -1 }, { 0, 2 }, { -1, 2 } })
				.put(0, 3, new int[][] { { 0, 0 }, { 1, 0 }, { 1, 1 }, { 0, -2 }, { 1, -2 } });
			
			TetronimoFactory  J = new TetronimoFactory(Color.BLUE, new float[] { 1.5f, 1.5f }, normalWallKicks, false, new boolean[][] {
				{ true },
				{ true, true, true }
			});
			
			TetronimoFactory  L = new TetronimoFactory(Color.ORANGE, new float[] { 1.5f, 1.5f }, normalWallKicks, false, new boolean[][] {
				{ false, false, true },
				{ true, true, true }
			});
			
			TetronimoFactory O = new TetronimoFactory(Color.YELLOW, new float[] { 2, 1 }, normalWallKicks, false, new boolean[][] {
				{ false, true, true },
				{ false, true, true }
			});
			
			TetronimoFactory S = new TetronimoFactory(Color.GREEN, new float[] { 1.5f, 1.5f }, normalWallKicks, false, new boolean[][] {
				{ false, true, true },
				{ true, true }
			});
			
			TetronimoFactory T = new TetronimoFactory(Color.MAGENTA, new float[] { 1.5f, 1.5f }, normalWallKicks, true, new boolean[][] {
				{ false, true },
				{ true, true, true }
			});
			
			TetronimoFactory Z = new TetronimoFactory(Color.RED, new float[] { 1.5f, 1.5f }, normalWallKicks, false, new boolean[][] {
				{ true, true },
				{ false, true, true }
			});
			
			TETRONIMOS = new TetronimoFactory[] { I, J, L, O, S, T, Z };
		}
		
		private TetronimoFactory[] tetronimos = new TetronimoFactory[PIECE_COUNT];
		
		private int index = 0;
		
		public TetronimoBag() {
			refill();
		}
		
		// are there no array utility methods??
		public void refill() {
			TetronimoFactory[] unshuffled = new TetronimoFactory[PIECE_COUNT];
			
			for (int i = 0; i < PIECE_COUNT; i++)
				unshuffled[i] = TETRONIMOS[i];
			
			int remaining = PIECE_COUNT;
			
			while (remaining > 0) {
				int index = random.nextInt(remaining);
				//System.out.println("index: " + index);
				tetronimos[PIECE_COUNT - remaining] = unshuffled[index];
				
				for (int i = index; i < remaining - 1; i++)
					unshuffled[i] = unshuffled[i + 1];
				
				remaining--;
			}
			
			this.index = 0;
		}
		
		public boolean isEmpty() {
			return this.index >= PIECE_COUNT;
		}
		
		public Tetronimo peek() {
			return isEmpty() ? null : tetronimos[index].toTetronimo();
		}
		
		public Tetronimo next() {
			Tetronimo tetronimo = peek();
			index++;
			return tetronimo;
		}
	}
	
	private static TetronimoBag bag1 = new TetronimoBag(), bag2 = new TetronimoBag();
	
	public static Tetronimo peek() {
		return bag1.isEmpty() ? bag2.peek() : bag1.peek();
	}
	
	public static Tetronimo next() {
		if (bag1.isEmpty()) {
			TetronimoBag temp = bag1;
			bag1.refill();
			bag1 = bag2;
			bag2 = temp;
		}
		
		return bag1.next();
	}
}
