package game;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Iterator;

import chat.ChatServer;
import framework.Constants;

public class GameServer implements Runnable, Constants{
	
	DatagramSocket serverSocket = null;
	Thread t;
	Thread updater;
	String data;
	HashMap<Integer, Player> players;

	public GameServer(){
		try {
            serverSocket = new DatagramSocket(GAME_PORT);
			serverSocket.setSoTimeout(100);
		} catch (IOException e) {
		} catch(Exception e){}
		
		players = new HashMap<Integer, Player>();
		
		t = new Thread(this);
		t.start();
		
		updater = new Thread(){
			public void run(){
				while(true){
					for(Iterator ite = players.keySet().iterator();ite.hasNext();){
						int port = (int) ite.next();
						switch(players.get(port).getDirection()){
							case "[Command=UP]":
								players.get(port).setY(players.get(port).getY() - 5);
								break;
							case "[Command=DOWN]":
								players.get(port).setY(players.get(port).getY() + 5);
								break;
							case "[Command=LEFT]":
								players.get(port).setX(players.get(port).getX() - 5);
								break;
							case "[Command=RIGHT]":
								players.get(port).setX(players.get(port).getX() + 5);
								break;
						}
						
						try {
							Thread.sleep(300);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					broadcast(players);
				}
			}
		};

		updater.start();
		
		
	}

	public void send(InetAddress address, int port, String msg){
		try{
			DatagramPacket packet;	
			byte buf[] = msg.getBytes();	
			packet = new DatagramPacket(buf, buf.length, address, port);
			serverSocket.send(packet);
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	public void broadcast(HashMap players){
		String message;

		//TO FIX
		System.out.println("ad");
			
		for(Iterator ite = players.keySet().iterator(); ite.hasNext();){
			int port = (int) ite.next();
			message = toString(players);
			try{
				send( ((Player)players.get(port)).getAddress(), port, message );
				
			}catch(Exception e){}
		}
	}
	
	public String toString(HashMap players){
		String string = players.size() + ":";
		
		for(Iterator ite = players.keySet().iterator(); ite.hasNext();){
			int port = (int) ite.next();
			string += (((Player)players.get(port)).getX());
			string += " ";
			string += (((Player)players.get(port)).getY());
			string += ":";
		}
		
		return string;
	}

	public void run(){
		while(true){			
			byte[] buf = new byte[256];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			 
			try{
     			serverSocket.receive(packet);
			}catch(Exception ioe){}
			
			if(packet.getAddress() != null){
				if(!players.containsKey(packet.getPort())){
					players.put(packet.getPort(), new Player(packet.getAddress()));
					
					System.out.println(packet.getAddress()+"/"+packet.getPort());
				}
				
				players.get(packet.getPort()).setDirection(new String(packet.getData(), packet.getOffset(), packet.getLength()));;
			}
		}
	}	
	
	public static void main(String args[]) throws IOException{
		new GameServer();
		new ChatServer();
		
	}
}
