package main;

import java.awt.image.BufferedImage;
import java.io.*;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

public class Main {
	public static void main(String[] args) throws IOException {
		if (args.length == 4) {
			runFromCommandLine(args);
		} else if( args.length == 0 ){
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					new MainWindow();
				}
			});
		} else {
			throw new IOException();
		}
	}

	public static void runFromCommandLine(String[] args) throws IOException {
		Integer wTileCount = Integer.valueOf(args[0]);
		Integer hTileCount = Integer.valueOf(args[1]);
		Integer freq = Integer.valueOf(args[2]);
		Integer seed = Integer.valueOf(args[3]);
		if (wTileCount > 1000 || wTileCount < 20 || hTileCount > 1000 || hTileCount < 20 || freq < 30 || freq > 100
				|| seed < 0) {
			throw new IOException();
		}
		PerlinNoise2D gen = new PerlinNoise2D(wTileCount, hTileCount, freq, seed);
		BufferedImage noiseImage = gen.getNoiseImage(false, true);
		if (noiseImage != null) {
			File outputfile = new File("image_seed_" + seed + "freq_" + freq + ".jpg");
			Writer w = new OutputStreamWriter(new FileOutputStream(outputfile), "UTF-8");
			PrintWriter pw = new PrintWriter(w);
			pw.close();
		}
	}
}