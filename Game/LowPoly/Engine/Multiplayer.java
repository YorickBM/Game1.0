package Engine;

import Mainn.Agent;
import Mainn.Game;
import Mainn.Network;

public class Multiplayer {
	
	//Initialize Multiplayer
	public static String version;
	public static Game game;
	public static Agent agent;
	public static Network net;
	
	@SuppressWarnings("static-access")
	public Multiplayer(String Version, String Ip, int poort) {
		this.setVersion(Version);
		this.createNetwork(new Network(Ip, poort)); 
	}
	
	
	private static void setVersion(String version) {
		Multiplayer.version = version;
	}
	private static void createNetwork(Network net) {
		Multiplayer.setNet(net);
	}

	
	public static Network getNet() {
		return net;
	}
	private static void setNet(Network net) {
		Multiplayer.net = net;
	}
}
