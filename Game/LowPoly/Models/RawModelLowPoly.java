package Models;

import openglObjects.*;

public class RawModelLowPoly {
	
	private final Vao vao;
	private final int vertexCount;
	
	public RawModelLowPoly(Vao vao, int vertexCount) {
		this.vao = vao;
		this.vertexCount = vertexCount;
	}

	public Vao getVao() {
		return vao;
	}

	public int getVertexCount() {
		return vertexCount;
	}

}
