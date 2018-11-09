package Shader;

import lwjgl2.Matrix4f;
import rendering.CameraLowPoly;
import rendering.LightLowPoly;
import Entitys.Camera;
import toolbox.Maths;

public class CombineShaderWithRenderer {
	
	private static StaticShaderLowPoly shader;
	
	public static void setShader(StaticShaderLowPoly NewShader) {
		shader = NewShader;
	}
	
	public static void loadTransFormationMatrix(Matrix4f transformationMatrix) {
		shader.transformationMatrix.loadMatrix(transformationMatrix);
	}
	
	public static void loadViewMatrix(Camera camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		shader.viewMatrix.loadMatrix(viewMatrix);
	}
	
	public static void loadViewMatrix(Matrix4f viewMatrix) {
		shader.viewMatrix.loadMatrix(viewMatrix);
	}
	
	public static void loadViewMatrix(CameraLowPoly camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		shader.viewMatrix.loadMatrix(viewMatrix);
	}
	
	public static void loadProjectionMatrix(Matrix4f projectionMatrix) {
		shader.start();
		shader.projectionMatrix.loadMatrix(projectionMatrix);
		shader.stop();
	}
	
	public static void loadLight(LightLowPoly light) {
		shader.lightPosition.loadVec3(light.getDirection());
		shader.lightColour.loadVec3(light.getColour().getVector());
	}
	
	public static void loadShineVariables(float damper, float reflectivity) {
		shader.shineDamper.loadFloat(damper);
		shader.reflectivity.loadFloat(reflectivity);
	}
}
