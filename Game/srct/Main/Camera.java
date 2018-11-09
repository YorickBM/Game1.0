package Main;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import entities.Player;
import utils.SmoothFloat;

public class Camera {
	
	private float yaw = 0;
	private SmoothFloat pitch = new SmoothFloat(10, 10);
	private SmoothFloat angleAroundPlayer = new SmoothFloat(0, 10);
	private SmoothFloat distanceFromPlayer = new SmoothFloat(10, 5);
	
	private static final float PITCH_SENSITIVITY = 0.3f;
	private static final float YAW_SENSITIVITY = 0.3f;
	private static final float MAX_PITCH = 90;

	
	private Vector3f position = new Vector3f(0, 0, 0);
	private float roll;
	
	private Player player;
	
	public Camera(Player player){
		this.player = player;
	}
	
	public void move(){
		if(this.player != null) {
			calculateZoom();
			calculatePitch();
			calculateAngleAroundPlayer();
			float horizontalDistance = calculateHorizontalDistance();
			float verticalDistance = calculateVerticalDistance();
			calculateCameraPosition(horizontalDistance, verticalDistance);
			this.yaw = 180 - (player.getRotY() + angleAroundPlayer.get());
			yaw%=360;
		}else {
	        calculatePitch();
	        calculateAngleAroundPlayer();
	        float horizontalDistance = calculateHorizontalDistance();
	        float verticalDistance = calculateVerticalDistance();
	        calculateCameraPosition(horizontalDistance, verticalDistance);
	        this.yaw =360- angleAroundPlayer.get();
	        yaw%=360;
		}
	}
	
	//public void invertPitch(){
	//	this.pitch.x = -pitch.;
	//}

	public Vector3f getPosition() {
		return position;
	}

	public double getPitch() {
		return pitch.get();
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
	
	private void calculateCameraPosition(float horizDistance, float verticDistance){
		if(this.player != null) {
			float theta = player.getRotY() + angleAroundPlayer.get();
			float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
			float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));
			
			position.x = player.getPosition().x - offsetX;
			position.z = player.getPosition().z - offsetZ;
			position.y = player.getPosition().y + verticDistance + 4;
		}else {
	        float theta = angleAroundPlayer.get();
	        float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
	        float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));
	        
			position.x = offsetX + 5;
	        position.z = offsetZ + 5;
	        position.y = verticDistance;
		}
	}
	
	private float calculateHorizontalDistance() {
		return (float) (distanceFromPlayer.get() * Math.cos(Math.toRadians(pitch.get())));
	}
	
	private float calculateVerticalDistance() {
		return (float) (distanceFromPlayer.get() * Math.sin(Math.toRadians(pitch.get())));
	}
	
	private void calculateZoom() {
		float targetZoom = distanceFromPlayer.getTarget();
		float zoomLevel = Mouse.getDWheel() * 0.0008f * targetZoom;
		targetZoom -= zoomLevel;
		if (targetZoom < 1) {
			targetZoom = 1;
		}
		distanceFromPlayer.setTarget(targetZoom);
		distanceFromPlayer.update(0.01f);
	}
	
	private void calculatePitch() { //Mouse grabbed < Edited
		if (Mouse.isGrabbed() || Mouse.isButtonDown(1)) {
			float pitchChange = Mouse.getDY() * PITCH_SENSITIVITY;
			pitch.increaseTarget(-pitchChange);
			clampPitch();
		}
		pitch.update(1f / 60);
	}
	
	private void calculateAngleAroundPlayer() { //Mouse grabbed < Edited
		if (Mouse.isGrabbed() || Mouse.isButtonDown(1)) {
			float angleChange = Mouse.getDX() * YAW_SENSITIVITY;
			angleAroundPlayer.increaseTarget(-angleChange);
		}
		angleAroundPlayer.update(1f / 60);
	}

	private void clampPitch() {
		if (pitch.getTarget() < 0) {
			pitch.setTarget(0);
		} else if (pitch.getTarget() > MAX_PITCH) {
			pitch.setTarget(MAX_PITCH);
		}
	}

}
