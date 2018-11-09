package Models;

public class TexturedModel {
	
	private RawModelLowPoly rawModel;
	private ModelTexture texture;
	
	public TexturedModel(RawModelLowPoly model, ModelTexture texture) {
		this.rawModel = model;
		this.texture = texture;
	}

	public RawModelLowPoly getRawModel() {
		return rawModel;
	}

	public ModelTexture getTexture() {
		return texture;
	}

}
