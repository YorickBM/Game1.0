package display;

import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.Callbacks.*;

public class Window {
	
	@SuppressWarnings("unused")
	private final int fpsCap;
	private boolean fullScreen;
	private long window;
	
	private boolean[] keys = new boolean[GLFW_KEY_LAST];
	private boolean[] mouseButtons = new boolean[GLFW_MOUSE_BUTTON_LAST];
	
	public Window(WindowBuilder settings) {
		//Initialize Variables
		this.fpsCap = settings.getFpsCap();
		this.fullScreen = settings.isFullScreen();
		
		Display.setWidth(settings.getWidth());
		Display.setHeight(settings.getHeight());
		
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();
				
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");
		
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay Visible after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
		
		// Create the window
		window = glfwCreateWindow(settings.getWidth(), settings.getHeight(), settings.getTitle(), 0, 0); // first false is Fullscreen
		if(window == 0)
			throw new RuntimeException("Failed to create the GLFW window");
		
		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
		});
		
		// Get the resolution of the primary monitor
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

		// Center the window
		glfwSetWindowPos(
			window,
			(vidmode.width() - settings.getWidth()) / 2,
			(vidmode.height() - settings.getHeight()) / 2
		);
		
		if(settings.hasIcon())
			glfwSetWindowIcon(window, settings.getIcon());
	
		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(window);
	}
	
	public long getWindow() {
		return window;
	}
	
	public boolean isFullScreen(){
		return fullScreen;
	}
	
	public void update() {
		for(int i = 32; i < GLFW_KEY_LAST; i++) keys[i] = isKeyDown(i);
		for(int i = 0; i < GLFW_MOUSE_BUTTON_LAST; i++) mouseButtons[i] = isMouseDown(i);
		
		glfwPollEvents();
	}
	
	public void swapBuffers() {
		glfwSwapBuffers(window);
	}
	
	public boolean isCloseRequested() {
		return glfwWindowShouldClose(window);
	}
	
	public void destroy() {
		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	
	public static WindowBuilder newWindow(int width, int height, int fpsCap) {
		return new WindowBuilder(width, height, fpsCap);
	}

	
	/*
	 * Mouse Handler
	 */
	public boolean isMouseDown(int mouseButton) {
		return glfwGetMouseButton(window, mouseButton) == 1;
	}
	public boolean isMouseReleased(int keyCode) {
		return isMouseDown(keyCode) && !mouseButtons[keyCode];
	}
	public boolean isMousePressed(int keyCode) {
		return !isMouseDown(keyCode) && mouseButtons[keyCode];
	}
	
	public double getMouseX() {
		DoubleBuffer buffer = BufferUtils.createDoubleBuffer(1);
		glfwGetCursorPos(window, buffer, null);
		return buffer.get(0);
	}
	public double getMouseY() {
		DoubleBuffer buffer = BufferUtils.createDoubleBuffer(1);
		glfwGetCursorPos(window, null, buffer);
		return buffer.get(0);
	}
	
	/*
	 * Key Handler
	 */
	public boolean isKeyDown(int keyCode) {
		return glfwGetKey(window, keyCode) == 1;
	}
	public boolean isKeyReleased(int keyCode) {
		return !isKeyDown(keyCode) && keys[keyCode];
	}
	public boolean isKeyPressed(int keyCode) {
		return isKeyDown(keyCode) && !keys[keyCode];
	}
}
