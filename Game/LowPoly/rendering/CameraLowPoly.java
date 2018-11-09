package rendering;

import lwjgl2.Matrix4f;
import lwjgl2.Vector3f;

import Configs.Configs;
import Entitys.Player;
import display.Display;
import display.Window;
import utils.Maths;
import utils.SmoothFloat;

/**
 * Represents the in-game camera. This class is in charge of keeping the
 * projection-view-matrix updated. It allows the user to alter the pitch and yaw
 * with the left mouse button.
 *
 */
public class CameraLowPoly implements ICamera {

	private static final float PITCH_SENSITIVITY = 0.3f;
	private static final float YAW_SENSITIVITY = 0.3f;
	private static final float MAX_PITCH = 90;

	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.4f;
	private static final float FAR_PLANE = 2500;

	private static final float Y_OFFSET = 3;

	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix = new Matrix4f();
	private Matrix4f reflectedMatrix = new Matrix4f();
	
	private boolean reflected = false;

	private Vector3f position = new Vector3f(-10, 7, -20);

	private float yaw = 0;
	private SmoothFloat pitch = new SmoothFloat(10, 10);
	private SmoothFloat angleAroundPlayer = new SmoothFloat(0, 10);
	private SmoothFloat distanceFromPlayer = new SmoothFloat(10, 5);

	public CameraLowPoly() {
		this.projectionMatrix = createProjectionMatrix();
	}

	public void move(Player player, Window window) {
		calculatePitch(window);
		calculateAngleAroundPlayer(window);
		calculateZoom();
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance, verticalDistance, player);
		this.yaw = 180 - (player.getRotY() + angleAroundPlayer.get());
		yaw%=360;
		updateViewMatrices();
	}


	@Override
	public Vector3f getPosition() {
		return position;
	}

	@Override
	public Matrix4f getViewMatrix() {
		return viewMatrix;
	}

	@Override
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
	
	@Override
	public void reflect(){
		this.reflected = !reflected;
	}
	
	@Override
	public double getPitch() {
		return pitch.get();
	}

	@Override
	public float getYaw() {
		return yaw;
	}

	@Override
	public Matrix4f getProjectionViewMatrix() {
		if(reflected){
			return Matrix4f.mul(projectionMatrix, reflectedMatrix, null);
		}else{
			return Matrix4f.mul(projectionMatrix, viewMatrix, null);
		}
	}

	@Override
	public float getNearPlane() {
		return NEAR_PLANE;
	}

	@Override
	public float getFarPlane() {
		return FAR_PLANE;
	}

	private void updateViewMatrices() {
		Maths.updateViewMatrix(viewMatrix, position.x, position.y, position.z, pitch.get(), yaw);
		float posY = position.y - (2 * (position.y - Configs.WATER_HEIGHT));
		float pitchReflect = -pitch.get();
		Maths.updateViewMatrix(reflectedMatrix, position.x, posY, position.z, pitchReflect, yaw);
	}

	private static Matrix4f createProjectionMatrix() {
		Matrix4f projectionMatrix = new Matrix4f();
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
		return projectionMatrix;
	}

	private void calculateCameraPosition(float horizDistance, float verticDistance, Player player) {
		float theta = player.getRotY() + angleAroundPlayer.get();
		float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));
		
		position.x = player.getPosition().x - offsetX;
		position.z = player.getPosition().z - offsetZ;
		position.y = player.getPosition().y + verticDistance + 1.8f;
	}

	/**
	 * @return The horizontal distance of the camera from the origin.
	 */
	private float calculateHorizontalDistance() {
		return (float) (distanceFromPlayer.get() * Math.cos(Math.toRadians(pitch.get())));
	}

	/**
	 * @return The height of the camera from the aim point.
	 */
	private float calculateVerticalDistance() {
		return (float) (distanceFromPlayer.get() * Math.sin(Math.toRadians(pitch.get())));
	}

	/**
	 * Calculate the pitch and change the pitch if the user is moving the mouse
	 * up or down with the LMB pressed.
	 */
	private void calculatePitch(Window window) {
		if (window.isMouseDown(0)) {
			float pitchChange = (float) (window.getMouseY() * PITCH_SENSITIVITY);
			pitch.increaseTarget(-pitchChange);
			clampPitch();
		}
		pitch.update(1f / 60);
	}

	private void calculateZoom() {
		float targetZoom = distanceFromPlayer.getTarget();
		float zoomLevel = 1 * 0.0008f * targetZoom;
		targetZoom -= zoomLevel;
		if (targetZoom < 0) {
			targetZoom = 0;
		}
		distanceFromPlayer.setTarget(targetZoom);
		distanceFromPlayer.update(0.01f);
	}

	/**
	 * Calculate the angle of the camera around the player (when looking down at
	 * the camera from above). Basically the yaw. Changes the yaw when the user
	 * moves the mouse horizontally with the LMB down.
	 */
	private void calculateAngleAroundPlayer(Window window) {
		if (window.isMouseDown(0)) {
			float angleChange = (float) (window.getMouseY() * PITCH_SENSITIVITY);
			angleAroundPlayer.increaseTarget(-angleChange);
		}
		angleAroundPlayer.update(1f / 60);
	}

	/**
	 * Ensures the camera's pitch isn't too high or too low.
	 */
	private void clampPitch() {
		if (pitch.getTarget() < 0) {
			pitch.setTarget(0);
		} else if (pitch.getTarget() > MAX_PITCH) {
			pitch.setTarget(MAX_PITCH);
		}
	}

	public static float getyOffset() {
		return Y_OFFSET;
	}

}
