package guis;

import lwjgl2.Matrix4f;

import shaders.ShaderProgramLowPoly;
import shaders.UniformMatrix;
import utils.MyFile;

public class GuiShader extends ShaderProgramLowPoly{
	
	private static final MyFile VERTEX_SHADER = new MyFile("guis", "guiVertexShader.txt");
	private static final MyFile FRAGMENT_SHADER = new MyFile("guis", "guiFragmentShader.txt");
	
	protected UniformMatrix transformationMatrix = new UniformMatrix("transformationMatrix");

	public GuiShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER, "position");
		super.storeAllUniformLocations(transformationMatrix);
	}
	
	public void loadTransformation(Matrix4f matrix){
		this.transformationMatrix.loadMatrix(matrix);
	}
}
