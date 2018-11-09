package Textfied;

public class TextfieldMainSetup {
	
	private boolean Created = false;
	KeyboardControll Keyboard = null;
	
	public void createTextfield(Textfield textfield) {
		if(!this.Created) {
			this.Created = true;
			Keyboard = new KeyboardControll();
		}
		
		textfield.update(textfield, Keyboard);
		
	}

}
