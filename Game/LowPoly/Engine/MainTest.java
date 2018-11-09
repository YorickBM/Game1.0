package Engine;
import org.lwjgl.*;
import org.lwjgl.glfw.GLFW;
import org.newdawn.slick.opengl.Texture;

import Configs.Configs;
import Entitys.Entity;
import Mainn.Agent;
import Mainn.Game;
import Mainn.Network;
import guis.GuiRenderer;
import guis.GuiTexture;
import lwjgl2.Vector2f;
import rendering.RenderEngineLowPoly;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainTest {
	
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
	
	private static long window;
	
	public static void BindContent() {
		GLFW.glfwMakeContextCurrent(window);
	}
	
	public static void UnbindContent() {
		GLFW.glfwMakeContextCurrent(0);
	}
	
	static Thread test = new Thread() {
	    public void run() {	 
	    	System.out.println("Starting Test Thread");
	    	try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	    	System.out.println("Finished Test Thread");
	    }
	};
	
	static Thread SplashScreen = new Thread() {	    	
	    public void run() {	
	    	System.out.println("Running SplashScreen Thread");
	    	GLFW.glfwMakeContextCurrent(window);
	    	
	    	LoaderLowPoly loader = new LoaderLowPoly();
			GuiRenderer guiRenderer = new GuiRenderer(loader);
			GuiTexture SplashScreenTexture = new GuiTexture(loader.loadTexture("inventory/Inventory"), new Vector2f(0.15f, -0.8f), new Vector2f(0.58f, 0.53f));
	    	
			guis.clear();
	    	guis.add(SplashScreenTexture);
	    	guiRenderer.render(guis);
	    	GLFW.glfwMakeContextCurrent(0);
	    	
	    	System.out.println("Finished SplashScreen Thread");
	    }
	};	

	public void run() {
		
	}

	public static void main(String[] args) {
		System.out.println("LWJGL Version" + Version.getVersion());
		Manager.init();
		long StartTime = System.nanoTime();

		//Initialize Engine and Scene Objects
		RenderEngineLowPoly engine = new RenderEngineLowPoly(Configs.FPS_CAP, Configs.WIDTH, Configs.HEIGHT, false);	
		LoaderLowPoly loader = new LoaderLowPoly();
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		window = engine.getWindow().getWindow();
		//GLFW.glfwMakeContextCurrent(0);
		
		long estimatedTime = System.nanoTime() - StartTime;
		double seconds = (double)estimatedTime / 1_000_000_000.0;
		
		System.out.println(seconds + " second(s) past");
		
		/**
		 * Splash Screen
		 */		
		
		
		SplashScreen.start();
		try {
			try {
				SplashScreen.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		System.out.println("Continuing to Game Code");
		
		/**
		 * In-Game Code
		 */
		GLFW.glfwMakeContextCurrent(window);
		engine.prepare();
		
		while ( !engine.getWindow().isCloseRequested() ) {
			engine.clearBuffers();
			engine.swapBuffers();
			
			if(engine.getWindow().isKeyPressed(GLFW_KEY_A)) {
				System.out.println("Key A is pressed");
			}
			if(engine.getWindow().isKeyReleased(GLFW_KEY_A)) {
				System.out.println("Key A is released");
			}
			
			engine.update();
		}
		
		engine.close();
	}

}