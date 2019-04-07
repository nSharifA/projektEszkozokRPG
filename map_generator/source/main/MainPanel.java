package main;
import java.awt.Graphics;

import javax.swing.JPanel;

public class MainPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PerlinNoise2D perlinNoise;

	public MainPanel() {
		perlinNoise = new PerlinNoise2D(100, 100, 52, 0);
		repaint();
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(perlinNoise.getNoiseImage(true, false), 0, 0, this);
	}
}