package tetris;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import tetris.level.Level;
import tetris.level.LevelPanel;
import tetris.tile.TileGrid;

public final class Tetris extends JFrame implements KeyListener {
	private static final long serialVersionUID = 1L;

	private static String getScoreText(Integer score) {
		String text = "Press 'Space' to start a new game!";
		
		if (score != null)
			text = "Final score: " + score + "<br>" + text;
		
		return "<html>" + text;
	}
	
	private JLabel scoreLabel;
	
	private LevelPanel levelPanel;
	
	private Level level;
	
	private Tetris() throws InterruptedException {
		super("Tetris");
		
		scoreLabel = new JLabel(getScoreText(null), SwingConstants.CENTER);
		scoreLabel.setFont(new Font("Monospaced", Font.BOLD, 18));
		scoreLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		add(scoreLabel, BorderLayout.CENTER);
	
		addKeyListener(this);
		pack();
		
		setSize(new Dimension((int) (Level.WIDTH * TileGrid.SIZE * 1.5), (int) (Level.HEIGHT * TileGrid.SIZE * 1.2)));
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void startGameNoThread() throws InterruptedException {
		level = new Level();
		
		if (levelPanel != null) 
			levelPanel.setLevel(level);
		else {
			levelPanel = new LevelPanel(level);
			addKeyListener(levelPanel);
		}
		
		add(levelPanel);
		remove(scoreLabel);
		
		while (!level.isGameOver()) {
			repaint();
			Thread.sleep(1000);
			level.updateTetronimo();
		}
		
		scoreLabel.setText(getScoreText(level.getScore()));
		
		remove(levelPanel);
		add(scoreLabel);

		invalidate();
		validate();
		repaint();
	}
	
	public Thread startGame() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					startGameNoThread();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		
		thread.start();
		
		return thread;
	}
	
	public static void main(String[] args) throws InterruptedException {
		Tetris tetris = new Tetris();
		tetris.setVisible(true);
	}

	@Override
	public void keyTyped(KeyEvent e) { }

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE && (level == null || level.isGameOver()))
			startGame();
	}

	@Override
	public void keyReleased(KeyEvent e) { }
}
