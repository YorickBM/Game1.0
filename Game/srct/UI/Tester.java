package UI;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

import renderEngine.DisplayManager;
  
public class Tester {
	public static void fixOpenGl() {
		DisplayManager.createDisplay();
		int width = DisplayManager.getWidth();
		int height = DisplayManager.getHeight();
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(GL11.GL_SMOOTH);        
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LIGHTING);                    
  
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);                
        GL11.glClearDepth(1);                                       
  
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
  
        GL11.glViewport(0,0,width,height);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
  
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, width, height, 0, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
    public static void main(String[] argv) {
    	fixOpenGl();
    	
    	//Setup DynamicText
    	DynamicText health = new DynamicText();
    	
    	while(!Display.isCloseRequested()) {
    		 GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
    		 health.drawText(100, 50, "IETS", Color.yellow);
    		 DisplayManager.updateDisplay();
    	}
    	
    	DisplayManager.closeDisplay();
    }
}