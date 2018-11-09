package terrains;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import generation.ColourGenerator;
import generation.PerlinNoise;
import utils.Colour;
import Configs.Configs;

/**
 * Generates a terrain. This is in charge of creating the VAO for the terrain,
 * and providing the renderer to the terrain. In this abstract class the heights
 * and colours for the terrain are generated. Child classed have to implement
 * the createTerrain method which creates the mesh for the terrain and loads it
 * up to a VAO.
 * 
 * @author Karl
 *
 */
public abstract class TerrainGenerator {
	
	private float[][] heights;
	@SuppressWarnings("unused")
	private float x;
    @SuppressWarnings("unused")
	private float z;

	private final PerlinNoise perlinNoise;
	private final ColourGenerator colourGen;
	private final BufferedImage heightMap;

	public TerrainGenerator(PerlinNoise perlinNoise, ColourGenerator colourGen, String filePath) {
		this.perlinNoise = perlinNoise;
		this.colourGen = colourGen;
		
		BufferedImage image = null;
        try {
            image = ImageIO.read(new File("res/" + filePath + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		System.out.println(filePath);
	
		this.heightMap = image;
		this.x = -1 * Configs.WORLD_SIZE;
        this.z = 0 * Configs.WORLD_SIZE;
	}
	
	/**
	 * 
	 * Return the correct Height of the Terrain for the
	 * Handler thats calculated form the HeightMap.
	 * 
	 */
	
	public float getHeightNormal(float x, float z) {		
		float gridSquareSize = Configs.WORLD_SIZE / ((float) heights.length - 1);
        int gridX = (int) Math.floor(x / gridSquareSize);
        int gridZ = (int) Math.floor(z / gridSquareSize);
        
        if(gridX < 0 || gridZ < 0 || gridX >= heightMap.getHeight() || gridZ >= heightMap.getHeight()) return 0;
		
        float height = heightMap.getRGB(gridX, gridZ);
		height += Configs.MAX_PIXEL_HEIGHT/2f;
		height /= Configs.MAX_PIXEL_HEIGHT/2f;
		height *= Configs.AMPLITUDE;
		return height;
	}
	
	public float getHeight(int x, int z) {
		if(x < 0 || z < 0 || x >= heightMap.getHeight() || z >= heightMap.getHeight()) return 0;
		
		float height = heightMap.getRGB(x, z);
		height += Configs.MAX_PIXEL_HEIGHT/2f;
		height /= Configs.MAX_PIXEL_HEIGHT/2f;
		height *= Configs.AMPLITUDE;
		return height;
	}
	
	/**
	 * Generates a terrain. First the heights and colours of all the vertices
	 * are generated.
	 * 
	 * @param gridSize
	 *            - The number of grid squares along one side of the terrain.
	 * @return The generated terrain.
	 */
	public TerrainLowPoly generateTerrain(int gridSize) {
		float[][] heights = generateHeights(gridSize, perlinNoise);
		Colour[][] colours = colourGen.generateColours(heights, perlinNoise.getAmplitude());
		return createTerrain(heights, colours);
	}

	/**
	 * For use when the app closes.
	 */
	public abstract void cleanUp();

	/**
	 * Generates the terrain mesh data, loads it up to a VAO, and initializes
	 * the terrain.
	 * 
	 * @param heights
	 *            - The heights of all the vertices in the terrain.
	 * @param colours
	 *            - The colours of all the vertices.
	 * @return The new terrain.
	 */
	protected abstract TerrainLowPoly createTerrain(float[][] heights, Colour[][] colours);

	/**
	 * Uses the perlin noise generator (which might actually not be using the
	 * Perlin Noise algorithm - I'm not quite sure if it is or isn't) to
	 * generate heights for all of the terrain's vertices.
	 * 
	 * @param gridSize - The number of grid squares along one edge of the terrain.
	 * @param perlinNoise - The heights generator.
	 * @return All the heights for the vertices.
	 */
	private float[][] generateHeights(int gridSize, PerlinNoise perlinNoise) {
		float heights[][] = new float[gridSize + 1][gridSize + 1];
		for (int z = 0; z < heights.length; z++) {
			for (int x = 0; x < heights[z].length; x++) {
				if(Configs.HEIGHTMAP) heights[z][x] = getHeight(x, z);
				if(!Configs.HEIGHTMAP) heights[z][x] = perlinNoise.getPerlinNoise(x, z);
			}
		}
		this.heights = heights;
		return heights;
	}

	public BufferedImage getHeightMap() {
		return heightMap;
	}
}
