package TextfieldRenderer;

public class RawModelTextfield {
    
    private int vaoID;
    private int vertexCount;
     
    public RawModelTextfield(int vaoID, int vertexCount){
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
    }
 
    public int getVaoID() {
        return vaoID;
    }
 
    public int getVertexCount() {
        return vertexCount;
    }
     
     
 
}