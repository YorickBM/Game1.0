package TextfieldShaders;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class StaticShader extends ShaderProgram {
	
	private static final String VERTEX_FILE = "src/TextfieldShaders/vertexShader.txt";
	private static final String FRAGMENT_FILE = "src/TextfieldShaders/fragmentShader.txt";
	
	private int location_color;
	private int location_TextfieldPosition;
	private int location_textured;

	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}
	
	@Override
	protected void getAllUniformLocations() {
		location_color = super.getUniformLocation("color");
		location_TextfieldPosition = super.getUniformLocation("TextfieldPosition");
		location_textured = super.getUniformLocation("textured");
	}
	
	public void setColor(Vector3f Color) {
		super.loadVector(location_color, Color);
	}
	
	public void setPosition(Vector2f Position) {
		super.loadVector(location_TextfieldPosition, Position);
	}
	
	public void setTextured(Vector2f textured) {
		super.loadVector(location_textured, textured);
	}

}
