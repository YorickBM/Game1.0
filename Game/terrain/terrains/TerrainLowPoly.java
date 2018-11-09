package terrains;

import lwjgl2.Vector4f;

import openglObjects.Vao;
import rendering.ICamera;
import rendering.LightLowPoly;
import rendering.TerrainRendererLowPoly;

public class TerrainLowPoly {
	
	private final Vao vao;
	private final int vertexCount;
	private final TerrainRendererLowPoly renderer;
	
	public TerrainLowPoly(Vao vao, int vertexCount, TerrainRendererLowPoly renderer){
		this.vao = vao;
		this.vertexCount = vertexCount;
		this.renderer = renderer;
	}
	
	public int getVertexCount(){
		return vertexCount;
	}
	
	public Vao getVao(){
		return vao;
	}
	
	public void render(ICamera camera, LightLowPoly light, Vector4f clipPlane){
		renderer.render(this, camera, light, clipPlane);
	}
	
	public void delete(){
		vao.delete(true);
	}

}
