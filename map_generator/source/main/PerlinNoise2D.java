package main;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Maga a térkép generátor
 *
 * @author sharif
 */
public class PerlinNoise2D {

	double time = 0;

	static int terrTypeCount = 4;

	int XLIMIT = 100;

	int YLIMIT = 100;

	int frequency = 52;

	public PerlinNoise2D(int tileWidthCount, int tileHeightCount, int frequency, double seed) {
		this.XLIMIT = tileWidthCount;
		this.YLIMIT = tileHeightCount;
		this.frequency = frequency;
		this.time = seed;
	}

	public BufferedImage getNoiseImage(boolean viewGeneratedMap, boolean generateJSONFile) {
		Map<Integer, List<BufferedImage>> tilesets = new HashMap<Integer, List<BufferedImage>>();
		Integer[][] noiseMatrix = new Integer[XLIMIT + 2][YLIMIT + 2];
		Integer[][] tilesMatrixByIDs = new Integer[XLIMIT][YLIMIT];
		BufferedImage img = loadImage();
		if (img == null) {
			return null;
		}

		createTilesetMap(img, tilesets);
		calcAndCreateNoiseMatrix(noiseMatrix);
		removeLonelyTiles(noiseMatrix);
		BufferedImage image = pairImageToNoise(tilesets, noiseMatrix, tilesMatrixByIDs);

		if (generateJSONFile) {
			try {
				genJSONfile(tilesets, noiseMatrix, tilesMatrixByIDs);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return image;
	}

	/**
	 * Betölti a warcraft.png-t. Amik a tile-okat tartamazzák.
	 *
	 * @return BufferedImage betöltött kép, vagy ha az nincs akkor null.
	 * @author sharif
	 */
	private BufferedImage loadImage() {
		try {
			return ImageIO.read(new File("warcraft.png"));
		} catch (IOException e) {
			System.err.println(e.toString());
			return null;
		}
	}

	private void createTilesetMap(BufferedImage img, Map<Integer, List<BufferedImage>> tilesets) {
		for (int i = 0; i < terrTypeCount; ++i) {
			List<BufferedImage> temp = new ArrayList<BufferedImage>();
			for (int j = 0; j < 13; ++j) {
				temp.add(img.getSubimage(32 * i, 32 * j, 32, 32));
			}
			tilesets.put(i, temp);
		}
	}

	private BufferedImage pairImageToNoise(Map<Integer, List<BufferedImage>> tilesets, Integer[][] noiseMatrix,
			Integer[][] tilesMatrixByIDs) {
		BufferedImage image = new BufferedImage(XLIMIT * 32, YLIMIT * 32, BufferedImage.TYPE_INT_RGB);
		Graphics2D createGraphics = image.createGraphics();

		for (int y = 0; y < YLIMIT; y++) {
			for (int x = 0; x < XLIMIT; x++) {
				double noise = noiseMatrix[x + 1][y + 1];
				BufferedImage subimage = null;

				if (noise <= -1) {
					subimage = getImage(y + 1, x + 1, 1, 0, true, tilesets, noiseMatrix, tilesMatrixByIDs);
				} else if (noise == 0) {
					subimage = tilesets.get(2).get(0);
					subimage = getImage(y + 1, x + 1, 0, 1, true, tilesets, noiseMatrix, tilesMatrixByIDs);
					if (subimage.equals(tilesets.get(0).get(0))) {
						subimage = tilesets.get(2).get(0);
						tilesMatrixByIDs[x][y] = 3;
					}
				} else if (noise == 1 || noise == 2 || noise == 3) {
					subimage = tilesets.get(0).get(0);
					tilesMatrixByIDs[x][y] = 1;
				} else {
					subimage = getImage(y + 1, x + 1, 3, 3, false, tilesets, noiseMatrix, tilesMatrixByIDs);
				}

				if (subimage != null) {
					createGraphics.drawImage(subimage, x * 32, y * 32, null);
				}
			}
		}

		return image;
	}

	private void removeLonelyTiles(Integer[][] noiseMatrix) {
		for (int i = 0; i < 2; ++i) {
			removeLonelyHorizontalTiles(noiseMatrix);
			removeLonelyVerticalTiles(noiseMatrix);
		}
	}

	private void calcAndCreateNoiseMatrix(Integer[][] noiseMatrix) {
		for (int y = 0; y < YLIMIT + 2; y++) {
			for (int x = 0; x < XLIMIT + 2; x++) {
				noiseMatrix[x][y] = calcNoise(x, y).intValue();
			}
		}
	}

	/**
	 * Létrehozza a JSON filet és elmenti.
	 *
	 * @author sharif
	 */
	private void genJSONfile(Map<Integer, List<BufferedImage>> tilesets, Integer[][] noiseMatrix,
			Integer[][] tilesMatrixByIDs) throws JSONException {
		JSONObject json = new JSONObject();
		json.put("height", YLIMIT);
		json.put("width", XLIMIT);
		json.put("infinite", false);
		json.put("nextlayerid", 6);
		json.put("nextobjectid", 2);
		json.put("orientation", "orthogonal");
		json.put("renderorder", "right-down");
		json.put("tiledversion", "1.2.2");
		json.put("tileheight", 32);
		json.put("tilewidth", 32);
		json.put("type", "map");
		json.put("version", 1.2);

		createLayerJSONObject(json, noiseMatrix, tilesMatrixByIDs);
		createTileSets(json, tilesets);
		FileWriter file;
		try {
			file = new FileWriter("file_seed" + time + "freq" + frequency + ".json");
			file.write(json.toString());
			file.flush();
			file.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Létrehozza a JSON TileSet Objektumokat.
	 *
	 * @author sharif
	 */
	private void createTileSets(JSONObject json, Map<Integer, List<BufferedImage>> tilesets) throws JSONException {
		JSONObject tileset = new JSONObject();
		json.append("tilesets", tileset);
		tileset.accumulate("columns", 4);
		tileset.accumulate("firstgid", 1);
		String a = "..\\/..\\/..\\/warcraft.png";
		tileset.append("image", a);
		tileset.accumulate("imageheight", "416");
		tileset.accumulate("imagewidth", "128");
		tileset.accumulate("margin", 0);
		tileset.accumulate("name", "warcraft");
		tileset.accumulate("spacing", 0);
		tileset.accumulate("tilecount", 13 * 4);
		tileset.accumulate("tileheight", 32);
		tileset.accumulate("tilewidth", 32);
		createTiles(tileset, tilesets);

	}
	
	/**
	 * Létrehozza a JSON Tile Objektumokat.
	 *
	 * @author sharif
	 */
	private static void createTiles(JSONObject tileset, Map<Integer, List<BufferedImage>> tilesets)
			throws JSONException {
		for (int y = 0; y < tilesets.get(0).size(); y++) {
			JSONObject tile = new JSONObject();
			tile.accumulate("id", (y * 4));
			tileset.append("tiles", tile);
		}
		for (int y = 0; y < tilesets.get(1).size(); y++) {
			JSONObject tile = new JSONObject();
			tile.accumulate("id", (y * 4) + 1);
			JSONObject property = new JSONObject();
			property.accumulate("name", "collides");
			property.accumulate("type", "bool");
			property.accumulate("value", true);
			tile.append("properties", property);
			tileset.append("tiles", tile);
		}
		JSONObject tile3 = new JSONObject();
		tile3.accumulate("id", 2);
		tileset.append("tiles", tile3);
		for (int y = 1; y < tilesets.get(2).size(); y++) {
			JSONObject tile = new JSONObject();
			tile.accumulate("id", (y * 4) + 2);
			JSONObject property = new JSONObject();
			property.accumulate("name", "collides");
			property.accumulate("type", "bool");
			property.accumulate("value", true);
			tile.append("properties", property);
			tileset.append("tiles", tile);
		}
		for (int y = 0; y < tilesets.get(3).size(); y++) {
			JSONObject tile = new JSONObject();
			tile.accumulate("id", 3 + (y * 4));
			JSONObject property = new JSONObject();
			property.accumulate("name", "collides");
			property.accumulate("type", "bool");
			property.accumulate("value", true);
			tile.append("properties", property);
			tileset.append("tiles", tile);
		}
	}
	
	/**
	 * Létrehozza a JSON Layer Objektumokat.
	 *
	 * @author sharif
	 */
	private void createLayerJSONObject(JSONObject json, Integer[][] noiseMatrix, Integer[][] tilesMatrixByIDs)
			throws JSONException {
		JSONObject below_player = new JSONObject();
		JSONObject player_world = new JSONObject();
		JSONObject above_player = new JSONObject();
		JSONObject drawMode = new JSONObject();

//		JSONArray array = new JSONArray();
//		array.put(below_player);
//		array.put(player_world);
//		array.put(above_player);
//		array.put(drawMode);

		json.append("layers", below_player);
		json.append("layers", player_world);
		json.append("layers", above_player);
		json.append("layers", drawMode);

		drawMode.accumulate("draworder", "topdown");
		drawMode.accumulate("id", 5);
		drawMode.accumulate("name", "Objects");
		createObjects(drawMode, tilesMatrixByIDs);
		drawMode.accumulate("opacity", 1);
		drawMode.accumulate("type", "objectgroup");
		drawMode.accumulate("visible", true);
		drawMode.accumulate("x", 0);
		drawMode.accumulate("y", 0);

		for (Iterator<JSONObject> iterator = Arrays.asList(below_player, player_world, above_player)
				.iterator(); iterator.hasNext();) {
			JSONObject jsonObject = iterator.next();
			jsonObject.accumulate("height", YLIMIT);
			jsonObject.accumulate("width", XLIMIT);
			jsonObject.accumulate("opacity", 1);
			jsonObject.accumulate("type", "tilelayer");
			jsonObject.accumulate("visible", true);
			jsonObject.accumulate("x", 0);
			jsonObject.accumulate("y", 0);
		}

		below_player.accumulate("id", 1);
		player_world.accumulate("id", 2);
		above_player.accumulate("id", 3);

		below_player.accumulate("name", "below_player");
		player_world.accumulate("name", "player_world");
		above_player.accumulate("name", "above_player");

		for (int y = 1; y < YLIMIT + 1; y++) {
			for (int x = 1; x < XLIMIT + 1; x++) {
				Integer noise = noiseMatrix[x][y];
				if (noise == 0 || noise == 1 || noise == 2 || noise == 3) {
					below_player.append("data", tilesMatrixByIDs[x - 1][y - 1]);
					player_world.append("data", 0);
					above_player.append("data", 0);
				} else {
					below_player.append("data", 0);
					player_world.append("data", tilesMatrixByIDs[x - 1][y - 1]);
					above_player.append("data", 0);
				}
			}
		}
	}
	
	/**
	 * Létrehozza a JSONObjektumokat.
	 *
	 * @author sharif
	 */
	private void createObjects(JSONObject drawMode, Integer[][] tilesMatrixByIDs) throws JSONException {
		JSONObject spawnPoint = new JSONObject();
		drawMode.append("objects", spawnPoint);
		spawnPoint.accumulate("height", 0);
		spawnPoint.accumulate("id", 1);
		spawnPoint.accumulate("name", "Spawn Point");
		spawnPoint.accumulate("point", "true");
		spawnPoint.accumulate("rotation", 0);
		spawnPoint.accumulate("type", "");
		spawnPoint.accumulate("visible", true);
		spawnPoint.accumulate("width", 0);
		int randX = new Random().nextInt(XLIMIT);
		int randY = new Random().nextInt(YLIMIT);
		Integer value = tilesMatrixByIDs[randX][randY];
		while (value != 0 && value != 2) {
			randX = new Random().nextInt(XLIMIT);
			randY = new Random().nextInt(YLIMIT);
			value = tilesMatrixByIDs[randX][randY];
		}
		spawnPoint.accumulate("x", (32 * randX) - 16);
		spawnPoint.accumulate("y", (32 * randY) - 16);
	}

	/**
	 * Eltünteni azokat a koordináatértékeket horizontálisan, amikhez nem létezik tileImage
	 *
	 * @author sharif
	 */
	private void removeLonelyHorizontalTiles(Integer[][] noiseMatrix) {
		for (int y = 0; y < YLIMIT + 2; y++) {
			for (int x = 3; x < XLIMIT + 2; x++) {
				if (noiseMatrix[x - 3][y].equals(noiseMatrix[x][y])) {
					if (!noiseMatrix[x][y].equals(noiseMatrix[x - 1][y]) || !noiseMatrix[x][y].equals(noiseMatrix[x - 2][y])) {
						noiseMatrix[x - 1][y] = noiseMatrix[x][y];
						noiseMatrix[x - 2][y] = noiseMatrix[x][y];
					}
				}
			}
		}
	}
	
	/**
	 * Eltünteni azokat a koordináatértékeket vertikálisan, amikhez nem létezik tileImage
	 *
	 * @author sharif
	 */
	private void removeLonelyVerticalTiles(Integer[][] noiseMatrix) {
		for (int y = 0; y < YLIMIT + 2; y++) {
			for (int x = 3; x < XLIMIT + 2; x++) {
				if (noiseMatrix[y][x - 3].equals(noiseMatrix[y][x])) {
					if (!noiseMatrix[y][x].equals(noiseMatrix[y][x - 1]) || !noiseMatrix[y][x].equals(noiseMatrix[y][x - 2])) {
						noiseMatrix[y][x - 1] = noiseMatrix[y][x];
						noiseMatrix[y][x - 2] = noiseMatrix[y][x];
					}
				}
			}
		}
	}
	
	/**
	 * Megkeresi a megfelelő tile image-t az adott koordinátaértékhez.
	 *
	 * @return tile image
	 * @author sharif
	 */
	private BufferedImage getImage(int y, int x, int key, int i, boolean less,
			Map<Integer, List<BufferedImage>> tilesets, Integer[][] noiseMatrix, Integer[][] tilesMatrixByIDs) {
		BufferedImage subimage = null;
		double lu = noiseMatrix[x - 1][y - 1];
		double rd = noiseMatrix[x + 1][y + 1];
		double ru = noiseMatrix[x + 1][y - 1];
		double ld = noiseMatrix[x - 1][y + 1];
		double l = noiseMatrix[x - 1][y];
		double r = noiseMatrix[x + 1][y];
		double u = noiseMatrix[x][y - 1];
		double d = noiseMatrix[x][y + 1];
		subimage = tilesets.get(key).get(0);
		int value = 0;
		if (less ? (lu >= i) : (lu <= i)) {
			subimage = tilesets.get(key).get(4);
			value = 4;
		}
		if (less ? (rd >= i) : (rd <= i)) {
			subimage = tilesets.get(key).get(1);
			value = 1;
		}
		if (less ? (ru >= i) : (ru <= i)) {
			subimage = tilesets.get(key).get(3);
			value = 3;
		}
		if (less ? (ld >= i) : (ld <= i)) {
			subimage = tilesets.get(key).get(2);
			value = 2;
		}
		if (less ? (l >= i) : (l <= i)) {
			subimage = tilesets.get(key).get(8);
			value = 8;
		}
		if (less ? (r >= i) : (r <= i)) {
			subimage = tilesets.get(key).get(7);
			value = 7;
		}
		if (less ? (u >= i) : (u <= i)) {
			subimage = tilesets.get(key).get(6);
			value = 6;
		}
		if (less ? (d >= i) : (d <= i)) {
			subimage = tilesets.get(key).get(5);
			value = 5;
		}
		if ((less ? (l >= i) : (l <= i)) && (less ? (u >= i) : (u <= i))) {
			subimage = tilesets.get(key).get(9);
			value = 9;
		}
		if ((less ? (r >= i) : (r <= i)) && (less ? (d >= i) : (d <= i))) {
			subimage = tilesets.get(key).get(12);
			value = 12;
		}
		if ((less ? (r >= i) : (r <= i)) && (less ? (u >= i) : (u <= i))) {
			subimage = tilesets.get(key).get(11);
			value = 11;
		}
		if ((less ? (l >= i) : (l <= i)) && (less ? (d >= i) : (d <= i))) {
			subimage = tilesets.get(key).get(10);
			value = 10;
		}
		tilesMatrixByIDs[x - 1][y - 1] = key + (value * 4) + 1;
		return subimage;
	}
	
	/**
	 * A zajgenerátor minden számpárhoz hozzárendel egy értéket.
	 *
	 * @param x koordináat
	 * @param y koordináat
	 * @return számárhoz tartozó érték
	 * @author sharif
	 */
	private Double calcNoise(int x, int y) {
		double dx = (double) x / (20 * 32);
		double dy = (double) y / (20 * 32);
		return noise((dx * frequency) + time, (dy * frequency) + time);
	}

	static double min = 0;
	static double max = 0;

	private double noise(double x, double y) {
		int xi = (int) Math.floor(x) & 255;
		int yi = (int) Math.floor(y) & 255;
		int g1 = p[p[xi] + yi];
		int g2 = p[p[xi + 1] + yi];
		int g3 = p[p[xi] + yi + 1];
		int g4 = p[p[xi + 1] + yi + 1];

		double xf = x - Math.floor(x);
		double yf = y - Math.floor(y);

		double d1 = grad(g1, xf, yf);
		double d2 = grad(g2, xf - 1, yf);
		double d3 = grad(g3, xf, yf - 1);
		double d4 = grad(g4, xf - 1, yf - 1);

		double u = fade(xf);
		double v = fade(yf);

		double x1Inter = lerp(u, d1, d2);
		double x2Inter = lerp(u, d3, d4);
		double yInter = lerp(v, x1Inter, x2Inter) * 2;

		if (min > yInter) {
			min = yInter;
		}
		if (max < yInter) {
			max = yInter;
		}
		return (yInter * 4) + 1;

	}

	private double lerp(double amount, double left, double right) {
		return ((1 - amount) * left + amount * right);
	}

	private double fade(double t) {
		return t * t * t * (t * (t * 6 - 15) + 10);
	}

	private double grad(int hash, double x, double y) {
		switch (hash & 3) {
		case 0:
			return x + y;
		case 1:
			return -x + y;
		case 2:
			return x - y;
		case 3:
			return -x - y;
		default:
			return 0;
		}
	}

	static final int p[] = new int[512], permutation[] = { 151, 160, 137, 91, 90, 15, 131, 13, 201, 95, 96, 53, 194,
			233, 7, 225, 140, 36, 103, 30, 69, 142, 8, 99, 37, 240, 21, 10, 23, 190, 6, 148, 247, 120, 234, 75, 0, 26,
			197, 62, 94, 252, 219, 203, 117, 35, 11, 32, 57, 177, 33, 88, 237, 149, 56, 87, 174, 20, 125, 136, 171, 168,
			68, 175, 74, 165, 71, 134, 139, 48, 27, 166, 77, 146, 158, 231, 83, 111, 229, 122, 60, 211, 133, 230, 220,
			105, 92, 41, 55, 46, 245, 40, 244, 102, 143, 54, 65, 25, 63, 161, 1, 216, 80, 73, 209, 76, 132, 187, 208,
			89, 18, 169, 200, 196, 135, 130, 116, 188, 159, 86, 164, 100, 109, 198, 173, 186, 3, 64, 52, 217, 226, 250,
			124, 123, 5, 202, 38, 147, 118, 126, 255, 82, 85, 212, 207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42,
			223, 183, 170, 213, 119, 248, 152, 2, 44, 154, 163, 70, 221, 153, 101, 155, 167, 43, 172, 9, 129, 22, 39,
			253, 19, 98, 108, 110, 79, 113, 224, 232, 178, 185, 112, 104, 218, 246, 97, 228, 251, 34, 242, 193, 238,
			210, 144, 12, 191, 179, 162, 241, 81, 51, 145, 235, 249, 14, 239, 107, 49, 192, 214, 31, 181, 199, 106, 157,
			184, 84, 204, 176, 115, 121, 50, 45, 127, 4, 150, 254, 138, 236, 205, 93, 222, 114, 67, 29, 24, 72, 243,
			141, 128, 195, 78, 66, 215, 61, 156, 180 };
	static {
		for (int i = 0; i < 256; i++)
			p[256 + i] = p[i] = permutation[i];
	}

}