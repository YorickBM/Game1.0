package toolbox;

import org.lwjgl.util.vector.Vector2f;

import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import renderEngine.Loader;

public class Texts {
	
	public static int addTexts(String text, int Chars, GUIText DisplayText) {
		if(text.length() > Chars) {
			setText(DisplayText, text);
			Chars = text.length();
		}
		return Chars;
	}
	
	public static FontType CreateFont(Loader loader, String Location) {
		FontType FontStyle = new FontType(loader.loadTexture(Location), Location);
		return FontStyle;
	}
	
	public static GUIText createText(FontType fontType, String Text, float size, Vector2f location, float number, boolean centered) {
		GUIText text = new GUIText(size, fontType, location, number, centered);
		return text;
	}
	
	public static void setText(GUIText text, String newText) {
		text.setText(newText);
	}
	
	public static void removeText(GUIText text) {
		text.remove();		
	}
	
	public static void clearEntry(String entry, String newText, int EntryChars, GUIText text) {
		entry = "";
		EntryChars = 0;
		
		text.setText(newText);
	}
}