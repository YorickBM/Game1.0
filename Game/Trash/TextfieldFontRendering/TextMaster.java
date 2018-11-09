package TextfieldFontRendering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import TextfieldFontMeshCreator.FontType;
import TextfieldFontMeshCreator.GUIText;
import TextfieldFontMeshCreator.TextMeshData;
import TextfieldRenderer.*;

public class TextMaster {

	private static Loader loader;
	private static Map<FontType, List<GUIText>> texts = new HashMap<FontType, List<GUIText>>();
	private static FontRenderer renderer;
	
	public static void init(Loader theLoader) {
		renderer = new FontRenderer();
		loader = theLoader;
		
	}
	
	public static void render() {
		renderer.render(texts);
	}
	
	public static void loadText(GUIText text) {
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text);
		
		int vao = loader.loadToVAO(data.getVertexPositions(), data.getTextureCoords());
		text.setMeshInfo(vao, data.getVertexCount());
		
		List<GUIText> textBatch = texts.get(font);
		if(textBatch == null) {
			textBatch = new ArrayList<GUIText>();
			texts.put(font, textBatch);
		}
		textBatch.add(text);
	}
	
	public static void removetext(TextfieldFontMeshCreator.GUIText guiText) {
		List<GUIText> textBatch = texts.get(guiText.getFont());
		textBatch.remove(guiText);
		if(textBatch.isEmpty()) {
			texts.remove(guiText.getFont());
		}
	}
	
	public static void cleanUp() {
		renderer.cleanUp();
	}
	
	
}
