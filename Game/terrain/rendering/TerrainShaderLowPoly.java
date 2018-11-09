package rendering;

import shaders.ShaderProgramLowPoly;
import shaders.UniformMatrix;
import shaders.UniformVec2;
import shaders.UniformVec3;
import shaders.UniformVec4;
import utils.MyFile;

/**
 * Represents the shader program that is used for rendering the terrain.
 * 
 * @author Karl
 *
 */
public class TerrainShaderLowPoly extends ShaderProgramLowPoly {

	protected UniformMatrix projectionViewMatrix = new UniformMatrix("projectionViewMatrix");
	protected UniformVec3 lightDirection = new UniformVec3("lightDirection");
	protected UniformVec3 lightColour = new UniformVec3("lightColour");
	protected UniformVec2 lightBias = new UniformVec2("lightBias");
	protected UniformVec4 plane = new UniformVec4("plane");

	public TerrainShaderLowPoly(MyFile vertexFile, MyFile fragmentFile) {
		super(vertexFile, fragmentFile);
		super.storeAllUniformLocations(projectionViewMatrix, lightDirection, lightColour, lightBias, plane);
	}

	public TerrainShaderLowPoly(MyFile vertexFile, MyFile geometryFile, MyFile fragmentFile) {
		super(vertexFile, geometryFile, fragmentFile);
		super.storeAllUniformLocations(projectionViewMatrix, lightDirection, lightColour, lightBias, plane);
	}

}
