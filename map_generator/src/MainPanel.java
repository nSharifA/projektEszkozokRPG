import javax.swing.*;
import java.awt.*;
public class MainPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	private Timer timer = new Timer(30, e -> repaint());
	public MainPanel(){
//		timer.start();
		repaint();
	}
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(PerlinNoise2D.getNoiseImage(), 0, 0, this);
	}
}