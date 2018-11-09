package Shader;

import shaders.ShaderProgramLowPoly;
import shaders.UniformFloat;
import shaders.UniformMatrix;
import shaders.UniformSampler;
import shaders.UniformVec3;
import utils.MyFile;

public class StaticShaderLowPoly extends ShaderProgramLowPoly {
	
	private static final MyFile VERTEX_SHADER = new MyFile("Shader", "VertexShader2.glsl");
	private static final MyFile FRAGMENT_SHADER = new MyFile("Shader", "FragmentShader2.glsl");
	
	protected UniformMatrix transformationMatrix = new UniformMatrix("transformationMatrix");
	protected UniformMatrix projectionMatrix = new UniformMatrix("projectionMatrix");
	protected UniformMatrix viewMatrix = new UniformMatrix("viewMatrix");
	
	protected UniformVec3 lightPosition = new UniformVec3("lightPosition");
	protected UniformVec3 lightColour = new UniformVec3("lightColour");
	
	protected UniformFloat shineDamper = new UniformFloat("shineDamper");
	protected UniformFloat reflectivity = new UniformFloat("reflectivity");

	protected UniformSampler modelTexture = new UniformSampler("modelTexture");

	public StaticShaderLowPoly() {
		super(VERTEX_SHADER, FRAGMENT_SHADER, "position", "textureCoordinates", "normal");
		super.storeAllUniformLocations(transformationMatrix, projectionMatrix, viewMatrix, lightPosition, lightColour, shineDamper, reflectivity);
	}
}
