package Mainn;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.esotericsoftware.kryonet.Client;

import Engine.Main;
import Entitys.*;
import Models.TexturedModel;
import lwjgl2.Vector3f;
import rendering.RenderEngineLowPoly;

import static org.lwjgl.glfw.GLFW.*;


public class Game {

	static Player1 playerss;
	static Vector3f cameraPosition = new Vector3f(10, 0, 2);
	static Vector3f cameraRotation = new Vector3f();
	
	public static List<PolyVoxel> collidable = new ArrayList<PolyVoxel>();
	public static List<PolyFace> floor = new ArrayList<PolyFace>();
	public static List<Entity> players = new ArrayList<Entity>();
	
	TexturedModel Player = null;
	Player player = new Player(Player, new Vector3f(0, 0, 0), 0, 100, 0, 0.6f);
	
	int Delay = 0;
	
	public Game(){
		playerss = new Player1();
		playerss.vector = new Vector3f(0,2.4f,0);
	}
	
	public void update(Player player) {
		sendPlayerData(player);
		EscapeMenu();
	}
	
	private void EscapeMenu() {
		/*
		 * Mouse.isGrabbed doesn't work in LWJGL 3!!!
		 * 
		if(Mouse.isGrabbed() && Delay == 0 && Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			Delay = 20;
			Mouse.setGrabbed(false);
		}else if(!Mouse.isGrabbed() && Delay == 0 && Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			Delay = 20;
			Mouse.setGrabbed(true);
		}
		
		if(Delay > 0) {
			Delay--;
		}*/
	}
	
	public void sendPlayerData(Entity player){
		Client client = Main.net.client;
		if(player.getPosition().x != player.getPosition().x - 1.0f){
			PacketUpdateX packet = new PacketUpdateX();
			packet.x = player.getPosition().x;
			client.sendUDP(packet);
		}
		if(player.getPosition().y != player.getPosition().y - 1.0f){
			PacketUpdateY packet = new PacketUpdateY();
			packet.y = player.getPosition().y;
			client.sendUDP(packet);
		}
		if(player.getPosition().z != player.getPosition().z - 1.0f){
			PacketUpdateZ packet = new PacketUpdateZ();
			packet.z = player.getPosition().z;
			client.sendUDP(packet);
		}
	}
	
	public void render(TexturedModel Player, List<Entity> entities, RenderEngineLowPoly Renderer) {
		
		for(Agent agent : Network.agents){
			if(Network.agents.size() >= players.size()) {
				player.setModel(Player);
				player.setPosition(new Vector3f(agent.x, agent.y, agent.z));
				entities.add(player);
				players.add(player);
			}else{
				player.setPosition(new Vector3f(agent.x, agent.y, agent.z));
			}
		}
	}

	public void color(float f, float g, float h) {
		GL11.glColor3f(f, g, h);
	}
}
