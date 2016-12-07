package game;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JFrame;

import chat.ChatServer;
import utilities.CSVreader;
import utilities.Constants;

/**
 * The main game server. It just accepts the messages sent by one player to
 * another player
 * @author aacarrasco
 * @author mqcabailo
 *
 */
public class GameServer extends JFrame implements Runnable, Constants{
	
	private static final long serialVersionUID = -8519330717122141455L;
	
	// UDP setup
	DatagramSocket serverSocket = null;
	//DatagramSocket server;
	InetAddress address;
	String data;
	String playerData;
	
	// Game setup
	int playerCount = 0;
	GameState game;
	int gameStage = WAITING_FOR_PLAYERS;
	int numPlayers;
	boolean[][] collisions;
	LinkedList<Integer> playerSpawn; 
	int [][] spawnPoints;
	CSVreader csvreader = new CSVreader();
	
	// Threads
	Thread t = new Thread(this);
	Thread inThread;
	Thread outThread;
	Thread updater;
	Thread spawner;
	
	public GameServer(int numPlayers, String address){
		this.numPlayers = numPlayers;
		
		try {
			this.address = InetAddress.getByName(address);
		//	server = new DatagramSocket(GAME_PORT);
		} catch(IOException e){
			e.printStackTrace();
		}
		
		game = new GameState();
		
		switch(numPlayers){
			case 1:
				System.out.println(System.getProperty("user.dir"));
				collisions = csvreader.readCollision("src/assets/csv/map5_6_Walls.csv");
				game.setSpawnPoints(csvreader.powerSpawn("src/assets/csv/map5_6_power.csv"));
				playerSpawn = csvreader.playerSpawns("src/assets/csv/map5_6_Player5.csv");
				break;
			case 2:
			case 3:
				collisions = csvreader.readCollision("src/assets/csv/map3_4_TileLayer1.csv");
				game.setSpawnPoints(csvreader.powerSpawn("src/assets/csv/map3_4_power.csv"));
				playerSpawn = csvreader.playerSpawns("src/assets/csv/map3_4_Player3.csv");
			case 4:
				collisions = csvreader.readCollision("src/assets/csv/map3_4_TileLayer1.csv");
				game.setSpawnPoints(csvreader.powerSpawn("src/assets/csv/map3_4_power.csv"));
				playerSpawn = csvreader.playerSpawns("src/assets/csv/map3_4_Player4.csv");
				break;
			case 5:
				collisions = csvreader.readCollision("src/assets/csv/map5_6_TileLayer1.csv");
				game.setSpawnPoints(csvreader.powerSpawn("src/assets/csv/map5_6_power.csv"));
				playerSpawn = csvreader.playerSpawns("src/assets/csv/map5_6_Player5.csv");
				break;
			case 6:
				collisions = csvreader.readCollision("src/assets/csv/map5_6_Walls.csv");
				game.setSpawnPoints(csvreader.powerSpawn("src/assets/csv/map5_6_power.csv"));
				playerSpawn = csvreader.playerSpawns("src/assets/csv/map5_6_Player6.csv");
				break;
		}
		
		t.start();
		
		try {
			new ChatServer(numPlayers);
		} catch (IOException e) {
			e.printStackTrace();
		}
				
	}

	/**
	 * Helper method for broadcasting data to all players
	 * @param msg
	 */
	public void broadcast(DatagramSocket serverSocket, String msg){
		
		DatagramPacket msgPacket = new DatagramPacket(msg.getBytes(), msg.getBytes().length, address, MULTICAST_PORT);
		try {
			serverSocket.send(msgPacket);
			//System.out.println(msgPacket.getData());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Send a message to a player
	 * @param player
	 * @param msg
	 */
	public void send(NetPlayer player, String msg){
		DatagramPacket packet;	
		byte buf[] = msg.getBytes();		
		packet = new DatagramPacket(buf, buf.length, player.getAddress(),player.getPort());
		try{
			serverSocket.send(packet);
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}

	@Override
	public void run(){
		try{
			serverSocket = new DatagramSocket(GAME_PORT);
		} catch(IOException ioe){
			ioe.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}

		System.out.println("Game created. Waiting for " + numPlayers + " players.");
		
		inThread = new Thread(){
			public void run(){
				while(true){
					byte[] buf = new byte[256];
					DatagramPacket packet = new DatagramPacket(buf, buf.length);
					
					try{
						
						serverSocket.receive(packet);
						playerData = new String(buf);
						playerData = playerData.trim();
						
						if(gameStage == WAITING_FOR_PLAYERS){
							
							if (playerData.startsWith("CONNECT")){
								System.out.println("GS: Connecting... ");
								String tokens[] = playerData.split(" ");
								
								NetPlayer player = new NetPlayer(tokens[1],packet.getAddress(),packet.getPort(), playerCount, playerSpawn.pop(), playerSpawn.pop());
								
								if(!game.getPlayers().containsKey(tokens[1])){	
									System.out.println("GS: Player " + player.getName() + " just connected.");
									player.setX(player.getSpawnX());
									player.setY(player.getSpawnY());
									player.setAlive(true);
									player.setDirection("RIGHT");
									game.update(tokens[1].trim(),player);
									
									broadcast(serverSocket, "CONNECTED "+ player.getName() + " " + numPlayers + " " + player.getPlayerNo());
											
									playerCount++;
									System.out.println("GS: " + playerCount + " out of " + numPlayers + " connected.");
									if (playerCount==numPlayers){
										gameStage=GAME_START;
									} 
								} else {
									System.out.println("GS: ERROR: Player name already exists!");
								}
							}
						} 
						if(gameStage == IN_PROGRESS){
							
							if(playerData.startsWith("PLAYER")){
								String[] playerInfo = playerData.split(" ");
								String pname = playerInfo[1];
								String direction = playerInfo[2].trim();
								
								// Get player from gameState
								NetPlayer player = (NetPlayer)game.getPlayers().get(pname);
								player.setDirection(direction);
								
								// Update gameState
								game.update(pname, player);
								
							}
						}
					} catch(Exception ioe){
					//	ioe.printStackTrace();
					}
				}
			}
		};
		
		outThread = new Thread(){
			public void run(){
				while(true){
					//System.out.println(gameStage);
					if(gameStage == GAME_START){
						System.out.println("GS: Game State: START");
						broadcast(serverSocket, "START " + numPlayers);
						gameStage = IN_PROGRESS;
						broadcast(serverSocket, "INITSPAWN " + game.spawnPointsToString());
						runUpdate();
						runSpawner();
					}
					if(gameStage == IN_PROGRESS){
						// Send to all the updated gameState
						//broadcast(serverSocket, game.toString());
					}
					try{
						Thread.sleep(300);
					}catch(Exception e){}
				}
			}
		};
		
		inThread.start();
		outThread.start();
		
		
	}	
	
	public void runUpdate(){
		updater = new Thread(){
			public void run(){
				while(true){
					
					for(Iterator<String> ite=game.getPlayers().keySet().iterator();ite.hasNext();){
						String name=(String)ite.next();
						NetPlayer player=(NetPlayer)game.getPlayers().get(name);	
						//System.out.println(player.getY() + " : " + player.getX());
						switch(player.getDirection()){
						case "UP":
							if((player.getY() - Y_SPEED) >= 0 )
								if(!collisions[(player.getY() - Y_SPEED)/4][player.getX()/4] && !collisions[(player.getY() - Y_SPEED)/4][(player.getX() + 6)/4])
									player.setY(player.getY() - Y_SPEED);
							break;
						case "DOWN":
							if((player.getY() + Y_SPEED) + 6 < 120)
								if(!collisions[((player.getY() + Y_SPEED + 6)/4)][(player.getX() + 6)/4] && !collisions[((player.getY() + Y_SPEED + 6)/4)][(player.getX())/4])
									player.setY(player.getY() + Y_SPEED);
							break;
						case "LEFT":
							if((player.getX() - X_SPEED) >= 0)
								if(!collisions[player.getY()/4][(player.getX() - X_SPEED)/4] && !collisions[(player.getY() + 6)/4][(player.getX() - X_SPEED)/4] && !collisions[(player.getY() + 3)/4][(player.getX() - X_SPEED)/4])
									player.setX(player.getX() - X_SPEED);
							break;
						case "RIGHT":
							if((player.getX() + X_SPEED) + 6 < 120)
								if(!collisions[player.getY()/4][(player.getX() + X_SPEED + 6)/4] && !collisions[(player.getY() + 6)/4][(player.getX() + X_SPEED)/4] && !collisions[(player.getY() + 3)/4][(player.getX() + X_SPEED + 3)/4])
									player.setX(player.getX() + X_SPEED);
							break;
						}
						
						if(game.powerIsAt(player.getX(), player.getY()) ){
							if(game.getPowerUps().get(game.powerAt(player.getX(), player.getY())).isUp()){
								player.setPoweredUp(true);
								player.setScore(player.getScore() + 1000);
								game.getPowerUps().get(game.powerAt(player.getX(), player.getY())).setUp(false);;
							}
							broadcast(serverSocket, "DESPAWN " + game.powerAt(player.getX(), player.getY()));
						}
						
						game.update(name, player);
					}
					
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					// Send to all the updated gameState
					broadcast(serverSocket, game.toString());
					
				}
			}
		};
		
		updater.start();
	}
	
	public void runSpawner(){
		spawner = new Thread(){
			public void run(){
				while(true){
					for(int i = 0; i < game.getPowerUps().size(); i++){
						game.getPowerUps().get(i).setUp(true);
					}
					broadcast(serverSocket, "SPAWN");
					try{
						Thread.sleep(20000);
					}catch(Exception e){	
					}
					
				}
			}
		};
		
		spawner.start();
	}
	
}
