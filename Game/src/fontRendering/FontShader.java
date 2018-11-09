package fontRendering;
 
import shaders.ShaderProgramLowPoly;
import shaders.UniformVec2;
import shaders.UniformVec3;
import utils.MyFile;
 
public class FontShader extends ShaderProgramLowPoly{
    
    private static final MyFile VERTEX_SHADER = new MyFile("fontRendering", "fontVertex.txt");
	private static final MyFile FRAGMENT_SHADER = new MyFile("fontRendering", "fontFragment.txt");
	
	protected UniformVec3 colour = new UniformVec3("colour");
	protected UniformVec2 translation = new UniformVec2("translation");
     
    public FontShader() {
        super(VERTEX_SHADER, FRAGMENT_SHADER, "position", "textureCoords");
        super.storeAllUniformLocations(colour, translation);
    } 
 
}