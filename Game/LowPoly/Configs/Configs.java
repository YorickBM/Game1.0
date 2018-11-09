package Configs;

import lwjgl2.Vector2f;
import lwjgl2.Vector3f;

import utils.Colour;

public class Configs {

	public static final int FPS_CAP = 120;
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	
	public static final int MAX_ENTITIES = 200;

	public static final float COLOUR_SPREAD = 0.45f;
	public static final Colour[] TERRAIN_COLS = new Colour[] { new Colour(120, 120, 120, true), new Colour(120, 120, 120, true)};
	public static final float MAX_PIXEL_HEIGHT = 256 * 256 * 256;
	
	public static Vector3f LIGHT_POS = new Vector3f(0.3f, -1f, 0.5f);
	public static Colour LIGHT_COL = new Colour(1f, 0.95f, 0.95f);
	public static Vector2f LIGHT_BIAS = new Vector2f(0.3f, 0.8f);

	public static final int WORLD_SIZE = 200;
	public static final int SEED = 10164313;

	public static final float AMPLITUDE = 30; // 10
	public static final float ROUGHNESS = 0.4f; // 1.8
	public static final int OCTAVES = 5; // 2
	
	public static final float WATER_HEIGHT = -1; // 7
	public static final boolean HEIGHTMAP = false;
	
}
