package Engine;

import org.lwjgl.glfw.GLFW;

public class Manager {
	
	private static long lastFrameTime;
	private static float delta;
	
	public static void init() {
		lastFrameTime = getCurrentTime();
	}
	
	public static void update() {
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime) / 1000f;
		lastFrameTime = currentFrameTime;
	}
	
	public static float getFrameTimeSeconds() {
		return delta;
	}
	
	private static long getCurrentTime() {
		return (long)GLFW.glfwGetTime();
				
	}

}
