package rendering;

import lwjgl2.Matrix4f;
import lwjgl2.Vector3f;

/**
 * Represents a camera in the scene.
 * 
 * @author Karl
 *
 */
public interface ICamera {

	public Matrix4f getViewMatrix();

	public Vector3f getPosition();

	public Matrix4f getProjectionMatrix();

	public Matrix4f getProjectionViewMatrix();

	public float getNearPlane();

	public float getFarPlane();
	
	public float getYaw();
	
	public double getPitch();

	/**
	 * Inverts the camera for rendering a reflection texture. The y position
	 * needs to be changed to move it under the water (the same distance under
	 * the water as it was above the water) and the pitch is made negative.
	 */
	public void reflect();

}
