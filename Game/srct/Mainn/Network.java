package Mainn;
import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;


public class Network extends Listener {

	Client client;
	String ip;
	int port;
	static List<Agent> agents = new ArrayList<Agent>();
	
	public Network(String ip, int port){
		this.ip = ip;
		this.port = port;
	}
	
	public void connect() {
		try{
			client = new Client();
			client.getKryo().register(PacketUpdateX.class);
			client.getKryo().register(PacketUpdateY.class);
			client.getKryo().register(PacketUpdateZ.class);
			client.getKryo().register(PacketNewAgent.class);
			client.getKryo().register(PacketRemoveAgent.class);

			new Thread(client).start();
			client.connect(5000, ip, port, port);
			client.addListener(this);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void disconnect() {
		client.stop();
	}
	
	public void received(Connection c, Object o){
		if(o instanceof PacketUpdateX){
			PacketUpdateX packet = (PacketUpdateX) o;
			getAgentByID(packet.id).x = packet.x;
		}else if(o instanceof PacketUpdateY){
			PacketUpdateY packet = (PacketUpdateY) o;
			getAgentByID(packet.id).y = packet.y;
		}else if(o instanceof PacketUpdateZ){
			PacketUpdateZ packet = (PacketUpdateZ) o;
			getAgentByID(packet.id).z = packet.z;
		}
		
		if(o instanceof PacketNewAgent){
			PacketNewAgent packet = (PacketNewAgent) o;
			Agent agent = new Agent();
			agent.username = "unset";
			agent.id = packet.id;
			agent.x = packet.x;
			agent.y = packet.y;
			agent.z = packet.z;
			agents.add(agent);
			
		}else if(o instanceof PacketRemoveAgent){
			PacketRemoveAgent packet = (PacketRemoveAgent) o;
			agents.remove(getAgentByID(packet.id));			
		}
	}
	
	public static Agent getAgentByID(int id){
		for(Agent agent : agents){
			if(agent.id == id) return agent;
		}
		return null;
	}
}
