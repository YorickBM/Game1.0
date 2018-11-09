package Engine;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import Models.RawModelLowPoly;
import openglObjects.Vao;
import openglObjects.Vbo;

public class LoaderLowPoly {
	
	private List<Vao> vaos = new ArrayList<Vao>();
	private List<Vbo> vbos = new ArrayList<Vbo>();
	private List<Integer> textures = new ArrayList<Integer>();
	
	public RawModelLowPoly loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
		Vao vao = Vao.create();
		vaos.add(vao);
		
		vao.bind();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		storeDataInAttributeList(2, 3, normals);
		vao.unbind();
		
		return new RawModelLowPoly(vao, indices.length);
	}
	
	public RawModelLowPoly loadToVAO(float[] positions, int dimensions) {
		Vao vao = Vao.create();
		vaos.add(vao);
		
		vao.bind();
		storeDataInAttributeList(0, dimensions, positions);
		vao.unbind();
		
		return new RawModelLowPoly(vao, positions.length / dimensions);
	}
	
	public int loadToVAO(float[] positions, float[] textureCoords) {
		Vao vao = Vao.create();
		vaos.add(vao);
		
		vao.bind();
		storeDataInAttributeList(0, 2, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		vao.unbind();
		
		return vao.id;
	}
	
	public int loadTexture(String fileName) {
		Texture texture = null;
		try {
			texture = TextureLoader.getTexture("PNG", Class.class.getResourceAsStream("/res/" + fileName + ".png"));
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
					GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, 0);
			if(GL.getCapabilities().GL_EXT_texture_filter_anisotropic) {
				float amount = Math.min(4f, GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));
				GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, amount);
				
			}else {
				System.out.println("Not supported");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Tried to load texture " + fileName + ".png , didn't work");
			System.exit(-1);
		}
		textures.add(texture.getTextureID());
		return texture.getTextureID();
	}
	
	private void bindIndicesBuffer(int[] indices) {
		Vbo vbo = Vbo.create(GL15.GL_ELEMENT_ARRAY_BUFFER, GL15.GL_STATIC_DRAW);
		vbos.add(vbo);
		
		IntBuffer buffer = storeDataInIntBuffer(indices);
		vbo.bind();
		vbo.storeBufferData(buffer);
		//vbo.unbind();
	}
	
	private IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
		Vbo vbo = Vbo.create(GL15.GL_ARRAY_BUFFER, GL15.GL_STATIC_DRAW);
		FloatBuffer buffer = vbo.storeDataInFloatBuffer(data);
		vbos.add(vbo);
		
		vbo.bind();
		vbo.storeBufferData(buffer);
		vbo.vertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		vbo.unbind();
	}
	
	public void cleanUp() {
		for(Vbo vbo:vbos) {
			vbo.delete();
		}
		for(Vao vao: vaos) {
			vao.delete(true);
		}
		for (int texture : textures) {
			GL11.glDeleteTextures(texture);
		}
	}
}
