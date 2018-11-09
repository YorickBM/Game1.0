package Engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lwjgl2.Vector2f;
import lwjgl2.Vector3f;
import rendering.CameraLowPoly;
import rendering.LightLowPoly;
import rendering.RenderEngineLowPoly;

import org.lwjgl.glfw.GLFW;
import org.newdawn.slick.opengl.Texture;

import Configs.Configs;
import Entitys.Entity;
import Entitys.Player;
import Mainn.Agent;
import Mainn.Game;
import Mainn.Network;
import Models.ModelTexture;
import Models.OBJLoader;
import Models.RawModelLowPoly;
import Models.TexturedModel;
import Shader.StaticShaderLowPoly;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import generation.ColourGenerator;
import generation.PerlinNoise;
import guis.GuiRenderer;
import guis.GuiTexture;
import hybridTerrain.HybridTerrainGenerator;
import terrains.TerrainGenerator;
import terrains.TerrainLowPoly;
import water.WaterGenerator;
import water.WaterTileLowPoly;

public class Main {
	
	//Initialize HashMaps/ArrayLists
	public static List<Entity> entities = new ArrayList<Entity>();
	public static List<GuiTexture> guis = new ArrayList<GuiTexture>();
	public static List<GuiTexture> InvGui = new ArrayList<GuiTexture>();
	public static Map<String,Texture> textures = new HashMap<String,Texture>();
	
	//Initialize Multiplayer
	public static final String version = "1";
	public static Game game;
	public static Agent agent;
	//public static Network net = new Network("90.145.170.242", 25918);
	public static Network net = new Network("127.0.0.1", 25918);

	public static void main(String[] args) {
		
		Manager.init();
		
		long StartTime = System.nanoTime();
		 
		//Initialize Engine and Scene Objects
		RenderEngineLowPoly engine = new RenderEngineLowPoly(Configs.FPS_CAP, Configs.WIDTH, Configs.HEIGHT, false);
		GLFW.glfwMakeContextCurrent(engine.getWindow().getWindow());
		engine.ContinueRendering(Configs.WIDTH, Configs.HEIGHT);
		
		LoaderLowPoly loader = new LoaderLowPoly();
		StaticShaderLowPoly shader = new StaticShaderLowPoly();
		 
		CameraLowPoly camera = new CameraLowPoly();
		LightLowPoly light = new LightLowPoly(Configs.LIGHT_POS, Configs.LIGHT_COL, Configs.LIGHT_BIAS);
		
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		FontType BasicFont = new FontType(loader.loadTexture("Fonts/basicfont"), "Fonts/basicfont");
		
		TextMaster.init(loader);
		
		engine.createProjectionMatrix(camera);
		engine.initProjectionMatrix(shader);
		
		//Initialize Terrain/Water
		PerlinNoise noise = new PerlinNoise(Configs.OCTAVES, Configs.AMPLITUDE, Configs.ROUGHNESS);
		ColourGenerator colourGen = new ColourGenerator(Configs.TERRAIN_COLS, Configs.COLOUR_SPREAD);
		TerrainGenerator terrainGenerator = new HybridTerrainGenerator(noise, colourGen, "heightmapp");
		TerrainLowPoly terrain = terrainGenerator.generateTerrain(Configs.WORLD_SIZE);
		
		WaterTileLowPoly water = WaterGenerator.generate(Configs.WORLD_SIZE, Configs.WATER_HEIGHT);			
		 
		//Initialize Player
		RawModelLowPoly PlayerModel = OBJLoader.loadObjModel("Characters/Male", loader);
		TexturedModel Player = new TexturedModel(PlayerModel, new ModelTexture(loader.loadTexture("Characters/Male")));
					
		Player player = new Player(Player, new Vector3f(100, 0, 100), 0, 100, 0, 0.55f);
		 
		//Initialize player Inventory UI
		GuiTexture Inventory = new GuiTexture(loader.loadTexture("inventory/Inventory"), new Vector2f(0.15f, -0.8f), new Vector2f(0.58f, 0.53f));
		GuiTexture InventoryExtras = new GuiTexture(loader.loadTexture("Icons/InvExtra"), new Vector2f(0.15f, -0.8f), new Vector2f(0.58f, 0.53f));
		GuiTexture InventoryActive = new GuiTexture(loader.loadTexture("Icons/InvSelected"), new Vector2f(-0.06325f, -0.605f), new Vector2f(0.04f, 0.07f));
		 
		guis.add(Inventory);
		guis.add(InventoryExtras);
		guis.add(InventoryActive);
			
		//Initialize Items in Game
		GuiTexture Currency = new GuiTexture(loader.loadTexture("Icons/Currency"), new Vector2f(0f, 0f), new Vector2f(0.04f, 0.07f));
		
		player.addItem(Currency, Player, 0, 1);
		player.addItem(Currency, Player, 1, 10);
		 
		//Initialize texts
		GUIText UsernameText = new GUIText(1f, BasicFont, new Vector2f(0.435f,0.74f), 0.3f, false);
		GUIText HealthText = new GUIText(1f, BasicFont, new Vector2f(0.45f,0.8725f), 0.1f, false);
		GUIText ManaText = new GUIText(1f, BasicFont, new Vector2f(0.45f,0.9055f), 0.1f, false);
		GUIText SoulText = new GUIText(2f, BasicFont, new Vector2f(0.488f,0.725f), 0.3f, true);
		GUIText Leveltext = new GUIText(2f, BasicFont, new Vector2f(0.3725f,0.87f), 0.1f, true);
		
		UsernameText.setColour(255, 255, 255);
	 	HealthText.setColour(255, 255, 255);
		ManaText.setColour(255, 255, 255);
		SoulText.setColour(255, 255, 255);
		Leveltext.setColour(255, 255, 255);
		
		long estimatedTime = System.nanoTime() - StartTime;
		double seconds = (double)estimatedTime / 1_000_000_000.0;
		
		System.out.println(seconds + " second(s) past");
			
		//Initialize game code
		//game = new Game();
		//net.connect();
		
		while(!engine.getWindow().isCloseRequested()) {			
			UsernameText.drawText(player.getUsername());
			HealthText.drawText(player.getStats("Health") + "/" + player.getStats("MaxHealth"));
			ManaText.drawText(player.getStats("Mana") + "/" + player.getStats("MaxMana"));
			SoulText.drawText(player.getStats("Souls") + "");
			Leveltext.drawText(player.getLevel() + "");
				
			player.move(terrainGenerator, engine.getWindow());
			player.updateInventory(InventoryActive);
			player.renderItem();
			 
			engine.processEntity(player);
			engine.render(light, camera);
			 
			camera.move(player, engine.getWindow());			
			engine.render(terrain, water, camera, light);
			 
			guiRenderer.render(guis);
			guiRenderer.render(InvGui);
			
			TextMaster.render();
			 
			engine.update();
			Manager.update();
		 }
		 
		 loader.cleanUp();
		 guiRenderer.cleanUp();

		 TextMaster.cleanUp();
		 
		 water.delete();
		 terrainGenerator.cleanUp();
		 terrain.delete();
		
		 //net.disconnect();
		 engine.close();

	}

}
