package engineTester;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import org.newdawn.slick.opengl.Texture;

import LowPolyMain.Configs;
import Main.Camera;
import Mainn.Agent;
import Mainn.Game;
import Mainn.Network;
import MySql.MySql;
import Textfield.KeyboardControll;
import Textfield.Textfield;
import buttons.Button;
import buttons.IButton;
import entities.Entity;
import entities.Light;
import entities.Player;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.RawModel;
import models.TexturedModel;
import normalMappingObjConverter.NormalMappedObjLoader;
import objConverter.OBJFileLoader;
import particles.ParticleMaster;
import particles.ParticleSystem;
import particles.ParticleTexture;
import postProcessing.Fbo;
import postProcessing.PostProcessing;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import rendering.RenderEngineLowPoly;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.MousePicker;
import toolbox.Texts;
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;

public class MainGameLoop {
	
	//Error list:
	// 37 > A Null error
	// 38 > A not fatal Null error
	// 39 > An prevented Crash because of Null Error
	
	public static final String version = "1";
	public static Game game;
	public static Agent agent;
	public static Texts texts;
	
	public static List<Entity> entities = new ArrayList<Entity>();
	public static List<GuiTexture> guis = new ArrayList<GuiTexture>();
	public static List<GuiTexture> InvGui = new ArrayList<GuiTexture>();
	
	public static Map<String,Texture> textures = new HashMap<String,Texture>();
	public static Network net = new Network("90.145.170.242", 25918);
	
	public static boolean CloseRequested = false;
	public static boolean isLoggedIn = false;
	
	public static boolean CreatedGameConnection = false;

	public static void main(String[] args) throws IOException {
		
		//Initialize Engine
		RenderEngineLowPoly engine = new RenderEngineLowPoly(Configs.FPS_CAP, Configs.WIDTH, Configs.HEIGHT);
		//Initialize MySql
			MySql.setConnectionUser("Yorick");
			MySql.setConnectionPassword("EPfmS9BB");

		//Other Engine Code
		Loader loader = new Loader();
		
		RawModel PlayerModel = OBJLoader.loadObjModel("Characters/Male", loader);
		TexturedModel Player = new TexturedModel(PlayerModel, new ModelTexture(
				loader.loadTexture("Characters/Male")));
		Player player = new Player(Player, new Vector3f(300, 5, -400), 0, 100, 0, 20f);
		
		Camera camera = new Camera(player);
		MasterRenderer renderer = new MasterRenderer(loader, camera);
		
		TextMaster.init(loader);
		ParticleMaster.init(loader, renderer.getProjectionMatrix());
		
		FontType BasicFont = new FontType(loader.loadTexture("Fonts/basicfont"), "Fonts/basicfont");
		
		
		// *********TASKBAR ICON STUFF**********
		
		//Display.setIcon(arg0)

		// *********TERRAIN TEXTURE STUFF**********
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy2"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture,
				gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

		// *****************************************
		
		//Official Nature Models
		TexturedModel TempTree = new TexturedModel(OBJFileLoader.loadOBJ("TempNature/TempTree", loader),
				new ModelTexture(loader.loadTexture("TempNature/TempTree")));
		
		//Official Weapon Models
		TexturedModel HealingPotion = new TexturedModel(OBJFileLoader.loadOBJ("Weapons/Potion", loader),
				new ModelTexture(loader.loadTexture("Weapons/Potion1")));
		
		TexturedModel Staff = new TexturedModel(OBJFileLoader.loadOBJ("Weapons/MageWand", loader),
				new ModelTexture(loader.loadTexture("Weapons/MageWand")));
		
		TexturedModel Blade = new TexturedModel(OBJFileLoader.loadOBJ("Weapons/IronBlade", loader),
				new ModelTexture(loader.loadTexture("Weapons/IronBlade")));
		
		//Terrain Model (May not be used in end)
		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "dump");
		List<Terrain> terrains = new ArrayList<Terrain>();
		terrains.add(terrain);
		
		
		//Trashy Test Models
		TexturedModel cherryModel = new TexturedModel(OBJFileLoader.loadOBJ("cherry", loader),
				new ModelTexture(loader.loadTexture("cherry")));
		cherryModel.getTexture().setHasTransparency(true);
		cherryModel.getTexture().setShineDamper(10);
		cherryModel.getTexture().setReflectivity(0.5f);
		cherryModel.getTexture().setSpecularMap(loader.loadTexture("cherryS"));
		
		TexturedModel lanternModel = new TexturedModel(OBJFileLoader.loadOBJ("lantern", loader),
				new ModelTexture(loader.loadTexture("lantern")));
		lanternModel.getTexture().setHasTransparency(true);
		lanternModel.getTexture().setSpecularMap(loader.loadTexture("lanternS"));

		TexturedModel lamp = new TexturedModel(OBJLoader.loadObjModel("lamp", loader),
				new ModelTexture(loader.loadTexture("lamp")));
		lamp.getTexture().setUseFakeLighting(true);
		
		ModelTexture fernTextureAtlas = new ModelTexture(loader.loadTexture("fern"));
		fernTextureAtlas.setNumberOfRows(2);

		TexturedModel fern = new TexturedModel(OBJFileLoader.loadOBJ("fern", loader),
				fernTextureAtlas);
		fern.getTexture().setHasTransparency(true);

		//Model/Entity/gui list
		List<Entity> normalMapEntities = new ArrayList<Entity>();
		
		//******************NORMAL MAP MODELS************************
		
		//Trashy Test Models 
		TexturedModel barrelModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("barrel", loader),
				new ModelTexture(loader.loadTexture("barrel")));
		barrelModel.getTexture().setNormalMap(loader.loadTexture("barrelNormal"));
		barrelModel.getTexture().setShineDamper(10);
		barrelModel.getTexture().setReflectivity(0.5f);
		
		TexturedModel crateModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("crate", loader),
				new ModelTexture(loader.loadTexture("crate")));
		crateModel.getTexture().setNormalMap(loader.loadTexture("crateNormal"));
		crateModel.getTexture().setShineDamper(10);
		crateModel.getTexture().setReflectivity(0.5f);
		
		TexturedModel boulderModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("boulder", loader),
				new ModelTexture(loader.loadTexture("boulder")));
		boulderModel.getTexture().setNormalMap(loader.loadTexture("boulderNormal"));
		boulderModel.getTexture().setShineDamper(10);
		boulderModel.getTexture().setReflectivity(0.5f);
		
		
		//************ENTITIES*******************
		
		//Entity entity = new Entity(barrelModel, new Vector3f(75, 10, -75), 0, 0, 0, 1f); << Not used
		//Entity entity2 = new Entity(boulderModel, new Vector3f(85, 10, -75), 0, 0, 0, 1f); << Not used
		//Entity entity3 = new Entity(crateModel, new Vector3f(65, 10, -75), 0, 0, 0, 0.04f); << Not used
		//normalMapEntities.add(entity);
		//normalMapEntities.add(entity2);
		//normalMapEntities.add(entity3);
		
		Random random = new Random(5666778);
		for (int i = 0; i < Configs.MAX_ENTITIES; i++) {
			
			if (i % 2 == 0) {

				float x = random.nextFloat() * 800;
				float z = random.nextFloat() * -800;
				if ((x > 50 && x < 100) || (z < -50 && z > -100)) {

				} else {
					float y = terrain.getHeightOfTerrain(x, z);
					entities.add(new Entity(TempTree, 1, new Vector3f(x, y, z), 0,
							random.nextFloat() * 360, 0, random.nextFloat() * 0.6f + 1.2f));
				}
			}
		}
		
		//*******************OTHER SETUP***************

		List<Light> lights = new ArrayList<Light>();
		Light sun = new Light(new Vector3f(1000000, 1500000, -1000000), new Vector3f(1.3f, 1.3f, 1.3f));
		lights.add(sun);

		//entities.add(player);
		List<GuiTexture> guiTextures = new ArrayList<GuiTexture>();
		List<GuiTexture> LoginGuiTextures = new ArrayList<GuiTexture>();
		
		//GuiTexture shadowMap = new GuiTexture(renderer.getShadowMapTexture(), new Vector2f(0.5f, 0.5f), new Vector2f(0.5f, 0.5f));
		//guiTextures.add(shadowMap);
		
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain);
	
		//**********Water Renderer Set-up************************
		
		WaterFrameBuffers buffers = new WaterFrameBuffers();
		WaterShader waterShader = new WaterShader();
		WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), buffers);
		List<WaterTile> waters = new ArrayList<WaterTile>();
		WaterTile water = new WaterTile(75, -75, -22);
		//waters.add(water);
		
		//******************** GUIS **************************
		
		//EMPTY
			
		
		//**********Button Testing Here************************
		
		Button pathButton = new Button(loader, "path", new Vector2f(0,0), new Vector2f(0.2f,0.2f)) {
			
			@Override
			public void onClick(IButton button) {
				CloseRequested = true;
			}
			
			@Override
			public void whileHover(IButton button) {
			}
			
			@Override
			public void startHover(IButton button) {
				button.playHoverAnimation(0.092f);
			}
			
			@Override
			public void stopHover(IButton button) {
				button.resetScale();
			}
			
			@Override
			public void resetScale() {
			}
			
			@Override
			public void setScale() {
			}
		};
		
		//**********Particle Renderer************************
		
		ParticleTexture particleTextureFire = new ParticleTexture(loader.loadTexture("particles/particleAtlas"), 4, false);
		
		ParticleSystem systemFire = new ParticleSystem(particleTextureFire, 300, 10, 1, 1, 1.6f);
		//system.setDirection(new Vector3f(0, 1, 0), 0.1f);
		systemFire.setLifeError(0.1f);
		systemFire.setSpeedError(0.25f);
		systemFire.setScaleError(0.5f);
		systemFire.randomizeRotation();
		
		Fbo multisampleFbo = new Fbo(Display.getWidth(), Display.getHeight());
		Fbo outputFbo = new Fbo(Display.getWidth(), Display.getHeight(), Fbo.DEPTH_TEXTURE);
		Fbo outputFbo2 = new Fbo(Display.getWidth(), Display.getHeight(), Fbo.DEPTH_TEXTURE);
		
		
		boolean Blur = false;
		boolean Bloom = true;
		
		PostProcessing.init(loader, 8, Blur, Bloom);
		
		//***************Text Fields Below********************
		KeyboardControll KeyboardManager = new KeyboardControll();
		
		Textfield Username = new Textfield(loader, KeyboardManager, "box", new Vector2f(0f,-0.7f), new Vector2f(0.5f,0.1f), "Username");
		Textfield UsernameBorder = new Textfield("crate", new Vector2f(0.2f,0.2f), Username);
		
		Textfield Password = new Textfield(loader, KeyboardManager, "box", new Vector2f(0f,0.3f), new Vector2f(0.5f,0.1f), "Password");
		
		List<Textfield> Textfields = new ArrayList<Textfield>();
		
		Textfields.add(Username);
		Textfields.add(Password);
		
		//*****************UI Setups Below*********************
		GuiTexture Inventory = new GuiTexture(loader.loadTexture("inventory/Inventory"), new Vector2f(0.15f, -0.8f), new Vector2f(0.58f, 0.53f));
		GuiTexture InventoryExtras = new GuiTexture(loader.loadTexture("Icons/InvExtra"), new Vector2f(0.15f, -0.8f), new Vector2f(0.58f, 0.53f));
		// width slot 0 = -0.06325f wifth slot 1 = -0.00325
		 GuiTexture InventoryActive = new GuiTexture(loader.loadTexture("Icons/InvSelected"), new Vector2f(-0.06325f, -0.605f), new Vector2f(0.04f, 0.07f));
		guis.add(Inventory);
		guis.add(InventoryExtras);
		guis.add(InventoryActive);
		
		//***************Game Item Textures********************
		GuiTexture Currency = new GuiTexture(loader.loadTexture("Icons/Currency"), new Vector2f(0f, 0f), new Vector2f(0.04f, 0.07f));
		
		player.addItem(Currency, Staff, 0, 1);
		player.addItem(Currency, Staff, 1, 10);
		
		//***************Dynamic Text Below********************
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
		
		//****************Game Loop Below*********************
		pathButton.hide(guiTextures);
		
		
		UsernameBorder.show(LoginGuiTextures);
		Username.show(LoginGuiTextures);
		Password.show(LoginGuiTextures);
		
		//Creating Texts
		Username.setFont(BasicFont);
		Password.setFont(BasicFont);
		
		//while(isLoggedIn && !Display.isCloseRequested() && !CloseRequested) {
		//	Username.update(Textfields);
		//	Password.update(Textfields);
		//	
		//	//Update Screen/Texts/Display
		//	guiRenderer.render(LoginGuiTextures);
		//	TextMaster.render(); 
		//	DisplayManager.updateDisplay();	
		//}
		
		game = new Game();
		net.connect();
		Mouse.setGrabbed(true);

		while (!Display.isCloseRequested() && !CloseRequested) {
			
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			UsernameText.drawText(player.getUsername());
			HealthText.drawText(player.getStats("Health") + "/" + player.getStats("MaxHealth"));
			ManaText.drawText(player.getStats("Mana") + "/" + player.getStats("MaxMana"));
			SoulText.drawText(player.getStats("Souls") + "");
			Leveltext.drawText(player.getLevel() + "");
			
			game.update(player);
			game.render(Player, entities, renderer);
		
			player.move(terrain);	
			player.updateInventory(InventoryActive);
			player.renderItem();
			
			camera.move();
			picker.update();
			ParticleMaster.update(camera);
			
			//Buttons Adding/Controlling
			while(Keyboard.next())
				if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
					if(pathButton.hidden())
						pathButton.show(guis);
					else
						pathButton.hide(guis);
			
			//Buttons updating
			pathButton.update();
			
			//systemFire.generateParticles(new Vector3f(player.getPosition().x, player.getPosition().y + 20, player.getPosition().z));
			//entity.increaseRotation(0, 1, 0);
			
			renderer.renderShadoMap(entities, sun);
			GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
			
			//render reflection texture
			buffers.bindReflectionFrameBuffer();
			float distance = 2 * (camera.getPosition().y - water.getHeight());
			camera.getPosition().y -= distance;
			renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, 1, 0, -water.getHeight() + 1));
			camera.getPosition().y += distance;
			
			//render Refraction texture
			buffers.bindRefractionFrameBuffer();
			renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, -1, 0, water.getHeight()));
			
			// render to screen
			GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
			buffers.unbindCurrentFrameBuffer();
			
			multisampleFbo.bindFrameBuffer();
			
			renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, 1, 0, -water.getHeight() + 1));
			waterRenderer.render(waters, camera, sun);
			
			multisampleFbo.unbindFrameBuffer();
			multisampleFbo.resolveToFbo(GL30.GL_COLOR_ATTACHMENT0, outputFbo);
			multisampleFbo.resolveToFbo(GL30.GL_COLOR_ATTACHMENT1, outputFbo2);
			PostProcessing.doPostProcessing(outputFbo.getColourTexture(), outputFbo2.getColourTexture());
			
			guiRenderer.render(guis);
			guiRenderer.render(InvGui);
			TextMaster.render();

			DisplayManager.updateDisplay();
		}
		
		//*********Update Data Below**************
		
		if(isLoggedIn) {
			float x = player.getLocation().x;
			float y = player.getLocation().y;
			float z = player.getLocation().z;
			
			int health = player.getStats("health");
			int mana = player.getStats("mana");
			int stamina = player.getStats("stamina");
			int souls = player.getStats("souls");
			
			int money = player.getStats("money");
			int xp = player.getStats("xp");
			int quest = player.getQuest();
			
			MySql.updateData("LocationX", x, "Username" , player.getUsername());
			MySql.updateData("LocationY", y, "Username" , player.getUsername());
			MySql.updateData("LocationZ", z, "Username" , player.getUsername());
		
			MySql.updateData("Health", health, "Username" , player.getUsername());
			MySql.updateData("Mana", mana, "Username" , player.getUsername());
			MySql.updateData("Stamina", stamina, "Username" , player.getUsername());
			MySql.updateData("Souls", souls, "Username" , player.getUsername());
			
			MySql.updateData("MaxHealth", health, "Username" , player.getUsername());
			MySql.updateData("MaxMana", mana, "Username" , player.getUsername());
			
			MySql.updateData("Money", money, "Username" , player.getUsername());
			MySql.updateData("Experience", xp, "Username" , player.getUsername());
			MySql.updateData("ActiveQuest", quest, "Username" , player.getUsername());
		}

		//*********Clean Up Below**************
		
		PostProcessing.cleanUp();
		multisampleFbo.cleanUp();
		outputFbo.cleanUp();
		outputFbo2.cleanUp();

		TextMaster.cleanUp();
		buffers.cleanUp();
		waterShader.cleanUp();
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		
		net.disconnect();
		ParticleMaster.cleanUp();
		
		engine.close();

	}
	
	public static void clearDepthBuffer() {
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	public static void clearColorBuffer() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
	}

}
