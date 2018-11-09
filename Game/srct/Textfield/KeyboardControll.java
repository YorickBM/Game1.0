package Textfield;

import java.util.Optional;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import toolbox.Texts;
import fontMeshCreator.GUIText;

public class KeyboardControll {
	private String Text;
	private int FrameDelay = 0;
	
	public void inputManager(Textfield textfield) {
		float size = 2f;
		
		float Times = size - 1f;
		float xOffset = (textfield.getScale().x / 1.33f);
		float yOffset = (textfield.getScale().y / 1.63f);
		
		float x = textfield.getPosition().x + xOffset - 0.12f      ;
		float y = textfield.getPosition().y + (yOffset - (Times * 0.025f));
		Vector2f TextPosition = new Vector2f(x, y);

		GUIText text = Texts.createText(textfield.getFont(), textfield.getText(), size, TextPosition, 1f, false);
    	text.setColour(255, 255, 255);
		
		while(Keyboard.next()) {
			Text = textfield.getText();
			Texts.removeText(text);
			char CurrentKey = Keyboard.getEventCharacter();
			
			//If Backspace is pressed remove last character from String List
			if(Keyboard.isKeyDown(Keyboard.KEY_BACK)) {
				if(!Text.equals("")) {textfield.setText(removeLastChar(Text));}
			}
			
			//Adding Keys and Reseting FrameDelay
			if(FrameDelay == 0 && !Keyboard.isKeyDown(Keyboard.KEY_BACK) && !Keyboard.isKeyDown(Keyboard.KEY_CAPITAL)) { 
				textfield.addText(CurrentKey);
			}
			
			//Checking if SHIFT is used.
			if(Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				if(FrameDelay == 1) {
					textfield.addText(String.valueOf(CurrentKey));
				}
			}
			if(FrameDelay == 0) FrameDelay = 2;
			FrameDelay--;
		}
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
	}
	
	public static String removeLastChar(String s) {
	    return Optional.ofNullable(s)
	      .filter(str -> str.length() != 0)
	      .map(str -> str.substring(0, str.length() - 1))
	      .orElse(s);
  	}
}
