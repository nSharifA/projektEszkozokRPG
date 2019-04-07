import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

public class Main {
	public static void main(String[] args) throws IOException {
		BufferedImage noiseImage = null;
		if (args.length > 0) {
			int wTileCount = Integer.valueOf(args[0]);
			int hTileCount = Integer.valueOf(args[1]);
			int freq = Integer.valueOf(args[2]);
			int seed = Integer.valueOf(args[3]);
			PerlinNoise2D gen = new PerlinNoise2D(wTileCount, hTileCount, freq, seed);
			noiseImage = gen.getNoiseImage(false, true);
			if (noiseImage != null) {
				File outputfile = new File("image_seed_" + seed + "freq_" + freq + ".jpg");
				ImageIO.write(noiseImage, "jpg", outputfile);
			}
		} else {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					new MainWindow();
				}
			});
		}
	}
}