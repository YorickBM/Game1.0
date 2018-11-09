package Textfied;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import TextfieldRenderer.DisplayManager;
 
public class Main2 {
	
	private static int test = 10;
	
	/*
	 * Y van de Muis moet -y worden zodat die overeen komt met de Textfield Y
	 * 
	 */
	
	public static List<Textfield> TextFields = new ArrayList<Textfield>();
 
    public static void main(String[] args) {
    	Textfield Username = new Textfield(5f, 0.5f, 1, new Vector2f(0.5f, -0.5f), new Vector3f(255, 255, 255), "", 0.3f, 0.3f, new Vector3f(50,205,50), "box", "crate");
    	Textfield Password = new Textfield(2.5f, 1f, 1, new Vector2f(0.5f, 0.5f), new Vector3f(255, 255, 255), "", 0.3f, 0.3f, new Vector3f(50,205,50), "crate", "box");
    	
    	TextFields.add(Username);
    	TextFields.add(Password);
    	
    	KeyboardControll UsernameKeyboard = new KeyboardControll();
    	KeyboardControll PasswordKeyboard = new KeyboardControll();
    	
		DisplayManager.createDisplay();
		DisplayManager.setDisplayName("Textfield tester");
    
		
        while(!Display.isCloseRequested()){
            //Create the TextField and run updater's
        	//Create Keyboard manager for the text field
        	
        	Username.update(UsernameKeyboard, TextFields);
        	Password.update(PasswordKeyboard, TextFields);
        	
        	//System.out.println(TextFields);
            
        	//Render text from text field to Console
        	//and update Display
        	TextfieldDisplay(Username);
          	TextfieldDisplay(Password);
            DisplayManager.updateDisplay();         
        }
        Username.cleanUp(Username.getLoader(), Username.getShader());
        Password.cleanUp(Password.getLoader(), Password.getShader());
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
