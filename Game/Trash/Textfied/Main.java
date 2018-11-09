package Textfied;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;

public class Main {
	
	private static int test = 10;

	public static void main(String[] args) {
    	
    	Textfield textfield = new Textfield(5f, 0.5f, 1, new Vector2f(0.5f, 0f), new Vector3f(255, 255, 255), "", 0.3f, 0.3f, new Vector3f(50,205,50));
		KeyboardControll Keyboard = new KeyboardControll();
		
		DisplayManager.createDisplay();
		DisplayManager.setDisplayName("Textfield tester");
    
		
        while(!Display.isCloseRequested()){
            //Textfield Logic (Render Textfield and Load textfield InputManager)
        	textfield.update(textfield, Keyboard);
            
        	TextfieldDisplay(textfield);
            DisplayManager.updateDisplay();         
        }
        textfield.cleanUp(textfield.getLoader(), textfield.getShader());
        DisplayManager.closeDisplay();
 
    }
    
    public static void TextfieldDisplay(Textfield textfield) {
    	 if(test == 0) {
				System.out.println(textfield.getText());
				test = 5;
			}
			test--;
    }

}
