package main;

import javax.swing.JFrame;

/**
 * szimpla Window, ami a kép megjelenítéséhez szükséges.
 *
 * @author sharif
 */
public class MainWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 1400;
	public static final int HEIGHT = 1000;
	private MainPanel panel = new MainPanel();

	public MainWindow() {
		add(panel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
		setVisible(true);
	}
}