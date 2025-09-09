package tetris.level;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import tetris.tetronimo.Tetronimo;
import tetris.tile.TileGrid;

public final class LevelPanel extends JPanel implements KeyListener {
	private static final long serialVersionUID = 1L;
	
	private class LevelGrid extends JComponent {
		private static final long serialVersionUID = 1L;

		private final TileGrid levelTiles;
		
		private final TileGrid tetronimoTiles;
		
		public LevelGrid() {
			super();
			
			setLayout(null);
			
			tetronimoTiles = new TileGrid(null);
			add(tetronimoTiles);
			
			levelTiles = new TileGrid(null);
			add(levelTiles);
		}
		
		@Override
		public Dimension getPreferredSize() {
			return new Dimension(
					Level.WIDTH * TileGrid.SIZE + TileGrid.PADDING,
					Level.HEIGHT * TileGrid.SIZE + TileGrid.PADDING
				);
		}
		
		@Override
		public void paintComponent(Graphics graphics) {
			//graphics.setColor(Color.GRAY);
			//graphics.fillRect(0, 0, Level.WIDTH * TileGrid.SIZE, Level.HEIGHT * TileGrid.SIZE);
			
			graphics.setColor(Color.GRAY);
			
			/*for (int i = 0; i <= Level.WIDTH; i++)
				graphics.fillRect(i * TileGrid.SIZE, 0, TileGrid.PADDING, Level.HEIGHT * TileGrid.SIZE + TileGrid.PADDING);
			
			for (int i = 0; i <= Level.HEIGHT; i++)
				graphics.fillRect(0, i * TileGrid.SIZE, Level.WIDTH * TileGrid.SIZE, TileGrid.PADDING);*/
			
			for (int i = 0; i <= 1; i++)
				graphics.fillRect(i * Level.WIDTH * TileGrid.SIZE, 0, TileGrid.PADDING, Level.HEIGHT * TileGrid.SIZE + TileGrid.PADDING);
			
			for (int i = 0; i <= 1; i++)
				graphics.fillRect(0, i * Level.HEIGHT * TileGrid.SIZE, Level.WIDTH * TileGrid.SIZE, TileGrid.PADDING);
			
			levelTiles.setTileGroup(level);
			levelTiles.setBounds(0, 0, getWidth(), getHeight());
			
			Tetronimo tetronimo = level.getTetronimo();
			tetronimoTiles.setTileGroup(tetronimo);
			
			Point position = tetronimo.getPosition();
			Dimension size = tetronimoTiles.getPreferredSize();
			
			tetronimoTiles.setBounds(position.x * TileGrid.SIZE, (position.y - Level.HIDDEN_HEIGHT) * TileGrid.SIZE, size.width, size.height);
			
			super.paintComponent(graphics);
		}
	}
	
	private static class TetronimoLabel extends JPanel {
		private static final long serialVersionUID = 1L;
		
		private final TileGrid tileGrid;
		
		public TetronimoLabel(String text) {
			super();
			
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			
			JLabel label = new JLabel(text);
			//label.setHorizontalTextPosition(JLabel.CENTER);
			label.setFont(new Font("Monospaced", Font.BOLD, 18));
			label.setPreferredSize(new Dimension(25, 25));
			add(label, BorderLayout.NORTH);
			
			tileGrid = new TileGrid(null);
			add(tileGrid, BorderLayout.CENTER);
			
			//setBackground(Color.GRAY);
		}
		
		public void setTetronimo(Tetronimo tetronimo) {
			tileGrid.setTileGroup(tetronimo);
			//setSize(getPreferredSize());
			invalidate();
			validate();
			repaint();
		}
	}
	
	private Level level;
	
	private final LevelGrid levelGrid;
	
	private final TetronimoLabel nextPiece;
	
	private final TetronimoLabel heldPiece;
	
	private final JLabel score;
	
	public LevelPanel(Level level) {
		super();
		
		setLayout(new FlowLayout());
		
		JPanel levelPanel = new JPanel();
		levelPanel.setLayout(new BoxLayout(levelPanel, BoxLayout.Y_AXIS));
		add(levelPanel);
		
		score = new JLabel();
		score.setFont(new Font("Monospaced", Font.BOLD, 18));
		score.setPreferredSize(new Dimension(25, 25));
		levelPanel.add(score);
		
		levelGrid = new LevelGrid();
		levelPanel.add(levelGrid);
		
		JPanel labelPanel = new JPanel();
		labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
		add(labelPanel);
		
		nextPiece = new TetronimoLabel("Next:");
		labelPanel.add(nextPiece);
		
		heldPiece = new TetronimoLabel("Held:");
		labelPanel.add(heldPiece);
		
		setLevel(level);
		
		setSize(getPreferredSize());
		//addKeyListener(this);
	}
	
	public void setLevel(Level level) {
		this.level = level;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		nextPiece.setTetronimo(level.getNextTetronimo());
		heldPiece.setTetronimo(level.getHeldTetronimo());
		score.setText("Score: " + level.getScore());
		super.paintComponent(g);
	}

	@Override
	public void keyTyped(KeyEvent e) { }

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			level.moveTetronimo(-1, 0);
			break;
		case KeyEvent.VK_RIGHT:
			level.moveTetronimo(1, 0);
			break;
		case KeyEvent.VK_DOWN:
			level.updateTetronimo();
			break;
		case KeyEvent.VK_Z:
			level.rotateTetronimo(false);
			break;
		case KeyEvent.VK_X:
			level.rotateTetronimo(true);
			break;
		case KeyEvent.VK_SHIFT:
			level.swapHeldTetronimo();
			break;
		}
		
		repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) { }
}
