package UI;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

public class DynamicText {

	private TrueTypeFont font;
	private Font awtFont;
	
	public DynamicText(String FontName, int size) {
		this.awtFont = new Font(FontName, Font.BOLD, size);
		this.font = new TrueTypeFont(this.awtFont, false);
	}
	
	public DynamicText() {
		this.awtFont = new Font("Times New Roman", Font.BOLD, 24);
		this.font = new TrueTypeFont(this.awtFont, false);
	}
	
	public void drawText(int x, int y, String text, Color color) {
		this.font.drawString(x, y, text, color);
	}
	
	public void drawText(int x, int y, String text) {
		this.font.drawString(x, y, text, Color.white);
	}
	
}
