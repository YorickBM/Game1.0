package InfoServer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import com.esotericsoftware.kryonet.Client;

import Engine.Main;

public class Server {
	
	static int FTPport = 25801;
	
	public void SendFileData(String FilePath){
		String CurrentFilePath = "";
		Client client = Main.net.client;
		
		if(!CurrentFilePath.equals(FilePath)) {
			PacketUpdateFile packet = new PacketUpdateFile();
			packet.PublicKeyPath = FilePath;
			CurrentFilePath = FilePath;
			client.sendUDP(packet);
		}
		
		/**if(player.getPosition().x != player.getPosition().x - 1.0f){
			PacketUpdateX packet = new PacketUpdateX();
			packet.x = player.getPosition().x;
			client.sendUDP(packet);
		}*/
	}
	
	
	@SuppressWarnings("unused")
	public void SendFile(String FilePath) throws IOException {
		Socket socket = new Socket("127.0.0.1", FTPport);
		
		File transferFile = new File (FilePath);
		byte [] bytearray  = new byte [(int)transferFile.length()];
		FileInputStream fin = new FileInputStream(transferFile);
		BufferedInputStream bin = new BufferedInputStream(fin);
		
		if(fin == null) {
			System.out.println("File you are trying to send not found!");
			System.exit(-1);
		}
		
		bin.read(bytearray,0,bytearray.length);
		OutputStream os = socket.getOutputStream();
		
		System.out.println("Sending Files...");
		os.write(bytearray,0,bytearray.length);
		os.flush();
		socket.close();
		System.out.println("File transfer complete");
		
		bin.close();
		socket.close();
	}
	
	@SuppressWarnings("unused")
	private void RetrieveFile() throws UnknownHostException, IOException {
		int filesize=1022386; 
		int bytesRead; 
		int currentTot = 0; 
		
		ServerSocket serverSocket = new ServerSocket(FTPport);
		Socket socket = serverSocket.accept();
		System.out.println("Accepted connection : " + socket);
		
		byte [] bytearray = new byte [filesize]; 
		InputStream is = socket.getInputStream(); 
		FileOutputStream fos = new FileOutputStream("copy.doc"); 
		BufferedOutputStream bos = new BufferedOutputStream(fos); 
		bytesRead = is.read(bytearray,0,bytearray.length); 
		currentTot = bytesRead; 
		
		do { bytesRead = is.read(bytearray, currentTot, (bytearray.length-currentTot)); 
			if(bytesRead >= 0) currentTot += bytesRead; 
			} while(bytesRead > -1); 
		bos.write(bytearray, 0 , currentTot); 
		bos.flush(); 
		bos.close(); 
		serverSocket.close();
	}

}
