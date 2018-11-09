package Textfied;

import java.util.Optional;

import org.lwjgl.input.Keyboard;

public class KeyboardControll {
	private String Text;
	private int FrameDelay = 0;
	
	public void inputManager(Textfield textfield) {
		
		while(Keyboard.next()) {
			Text = textfield.getText();
			
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
	}
	
	public static String removeLastChar(String s) {
	    return Optional.ofNullable(s)
	      .filter(str -> str.length() != 0)
	      .map(str -> str.substring(0, str.length() - 1))
	      .orElse(s);
  	}
}
