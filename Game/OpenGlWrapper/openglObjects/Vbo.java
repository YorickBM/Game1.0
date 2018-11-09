package openglObjects;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

public class Vbo {
	
	private final int vboId;
	private final int type;
	private final int usage;
	
	private Vbo(int vboId, int type, int usage){
		this.vboId = vboId;
		this.type = type;
		this.usage = usage;
		this.bind();
	}
	
	private Vbo(int vboId, int type){
		this.vboId = vboId;
		this.type = type;
		this.usage = 0;
		this.bind();
	}
	
	public static Vbo create(int type, int usage){
		int id = GL15.glGenBuffers();
		return new Vbo(id, type, usage);
	}
	
	public static Vbo create(int type){
		int id = GL15.glGenBuffers();
		return new Vbo(id, type);
	}
	
	public void bind(){
		GL15.glBindBuffer(type, vboId);
	}
	
	public void unbind(){
		GL15.glBindBuffer(type, 0);
	}
	
	public void allocateData(long sizeInBytes){
		GL15.glBufferData(type, sizeInBytes, usage);
	}
	
	public void storeData(long startInBytes, IntBuffer data){
		GL15.glBufferSubData(type, startInBytes, data);
	}
	
	public void storeData(long startInBytes, FloatBuffer data){
		GL15.glBufferSubData(type, startInBytes, data);
	}
	
	public void storeData(long startInBytes, ByteBuffer data){
		GL15.glBufferSubData(type, startInBytes, data);
	}
	
	public void storeBufferData(FloatBuffer buffer) {
		GL15.glBufferData(this.type, buffer, this.usage);
	}
	
	public void storeBufferData(IntBuffer buffer) {
		GL15.glBufferData(this.type, buffer, this.usage);
	}
	
	public FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	public void vertexAttribPointer(int attributeNumber, int vertexSize, int type, boolean normalized, int x, int y) {
		GL20.glVertexAttribPointer(attributeNumber, vertexSize, type, normalized, x, y);
	}
	
	public void delete(){
		GL15.glDeleteBuffers(vboId);
	}

}
