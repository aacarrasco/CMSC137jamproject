/**
 * 
 */
package game;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.command.BasicCommand;
import org.newdawn.slick.command.Command;
import org.newdawn.slick.command.InputProvider;
import org.newdawn.slick.command.InputProviderListener;
import org.newdawn.slick.command.KeyControl;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

import chat.ChatClient;
import utilities.Constants;

/**
 * @author aacarrasco
 * @author mqcabailo
 */
public class Play extends BasicGameState implements Constants, InputProviderListener, Runnable{
	private TextField messagesTF;
	private TextField inputTF;
	private ChatClient chat;
	private String name;
	Socket client;
	
	DatagramSocket socket;
	private String server;
	private InetAddress address;
	String message = "";
	String spawn = "";
	String serverData;
	boolean connected = false;
	int port;
	int numPlayers = 0;
	int error = 0;
	
	Thread t = new Thread(this);
	
	private InputProvider input;
	
	private TiledMap map;
	
	private ArrayList<Animation> players;
	//private ArrayList<Circle> playerBounds;
	private ArrayList<Float> xPos;
	private ArrayList<Float> yPos;
	
	private ArrayList<Image> food;
	private ArrayList<Image> powerUp;
	private ArrayList<Float> xPower;
	private ArrayList<Float> yPower;
	//private ArrayList<Boolean> powerIsUp;
	
	private Animation movingUp, movingDown, movingRight, movingLeft;
	private int[] normalDuration 	= {150, 150, 150, 150};
	//private int[] powerDuration		= {75, 75, 75, 75};	
	
	private Command moveUP = new BasicCommand("UP");
	private Command moveDOWN = new BasicCommand("DOWN");
	private Command moveLEFT = new BasicCommand("LEFT");
	private Command moveRIGHT = new BasicCommand("RIGHT");
	
	//test
	private String test = "";
	
	public Play(int state){
		
	}
	
	public Play(int state, String name, String address) {
		this.name = name;
		this.server = address;
		try {
			this.address = InetAddress.getByName(server);
			this.socket  = new DatagramSocket();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (Exception e1){
			e1.printStackTrace();
		}
		
		// Initialize chat module
		setChat(new ChatClient(this.name, this.server));
		client = chat.getClient();
		
	}
	
	@Override
	public void run() {
		try(MulticastSocket clientSocket = new MulticastSocket(MULTICAST_PORT)){
			
			// Join the Multicast group.
			clientSocket.joinGroup(address);
			
			send("CONNECT " + name);
			
			while(true){
				byte[] buf = new byte[1024];
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				
				clientSocket.receive(packet);
				serverData = new String(buf, 0, buf.length);
				serverData = serverData.trim();
				//test = serverData;
				
				if(!serverData.equals("")){
					//System.out.println("GC: ServerData: " + serverData);
				}
				
				String[] token = serverData.split(" ");
				if(!connected){
					
					if(serverData.startsWith("ERROR")){
						System.out.println(serverData);
						if(Integer.parseInt(token[1]) == ERROR_NAME_EXISTS){
							System.out.println("GC: Name already Exists");	
						}
					}
					if(serverData.startsWith("CONNECTED")){
						connected = true;
						System.out.println("GC: Player " + token[1] + " is connected.");
						//currXPos = Integer.parseInt(token[2]);
						//currYPos = Integer.parseInt(token[3]);
					}
					System.out.println("GC: CONNECT " + name + " " + connected);
					
				
				} else {
					//System.out.println("GC: ServerData: "+ serverData);
					if(serverData.startsWith("START")){
						numPlayers = Integer.parseInt(token[1]);
						System.out.println("GAME STARTED with " + numPlayers + " players.");
		
						// Animate each player
						for(int i = 0; i<numPlayers; i++){
							xPos.add(0f);
							yPos.add(0f);
							players.add(new Animation());	
							players.get(i).start();
						}
						
					}
					// Gets the position of all players in the game.
					if(serverData.startsWith("PLAYER")){
						message = serverData.substring(6);
					}
					// Spawns the food and powerups
					if(serverData.startsWith("SPAWN")){
						spawn = serverData.substring(5);
						serverData = "";
						//test = spawn;
					}
				}
			}
		} catch(IOException ioe){
			ioe.printStackTrace();
		}
		
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {		
		
		//map
		map	= new TiledMap("assets/tmx/map3_4.tmx");
		
		//resize container
		((AppGameContainer)gc).setDisplayMode((map.getWidth()*16) + 200, map.getHeight()*16, false);
		
		// Setup chatbox
		messagesTF = new TextField(gc, gc.getDefaultFont(), map.getWidth()*16, 0, 200, (map.getHeight()*16)-100);
		messagesTF.setBackgroundColor(Color.white);
		messagesTF.setTextColor(Color.lightGray);
		messagesTF.setBorderColor(Color.gray);
		messagesTF.setAcceptingInput(false);
		
		inputTF = new TextField(gc, gc.getDefaultFont(), map.getWidth()*16, (map.getHeight()*16) - 100, 200, 100);
		inputTF.setBackgroundColor(Color.white);
		inputTF.setBorderColor(Color.gray);
		inputTF.setTextColor(Color.black);
		inputTF.setCursorVisible(true);
		inputTF.setAcceptingInput(true);
		
		chat.setMessagesTF(messagesTF);
		
		// Animate players here
		Image[] up 		= {new Image("assets/sprites/bluePak/blueOpenU.png"), new Image("assets/sprites/bluePak/blueSemiU.png"), new Image("assets/sprites/bluePak/blueCloseU.png"), new Image("assets/sprites/bluePak/blueSemiU.png")};
		Image[] down 	= {new Image("assets/sprites/bluePak/blueOpenD.png"), new Image("assets/sprites/bluePak/blueSemiD.png"), new Image("assets/sprites/bluePak/blueCloseD.png"), new Image("assets/sprites/bluePak/blueSemiD.png")};
		Image[] left	= {new Image("assets/sprites/bluePak/blueOpenL.png"), new Image("assets/sprites/bluePak/blueSemiL.png"), new Image("assets/sprites/bluePak/blueCloseL.png"), new Image("assets/sprites/bluePak/blueSemiL.png")};
		Image[] right 	= {new Image("assets/sprites/bluePak/blueOpenR.png"), new Image("assets/sprites/bluePak/blueSemiR.png"), new Image("assets/sprites/bluePak/blueCloseR.png"), new Image("assets/sprites/bluePak/blueSemiR.png")};
		
		movingUp 		= new Animation(up		, normalDuration	, true);
		movingDown		= new Animation(down	, normalDuration	, true);
		movingLeft		= new Animation(left	, normalDuration	, true);
		movingRight		= new Animation(right	, normalDuration	, true);
		
		movingUp.start();
		movingDown.start();
		movingLeft.start();
		movingRight.start();

		movingUp.setLooping(true);
		movingDown.setLooping(true);
		movingRight.setLooping(true);
		movingLeft.setLooping(true);
		
		players = new ArrayList<Animation>();
		xPos 	= new ArrayList<Float>();
		yPos	= new ArrayList<Float>();
		
		powerUp	= new ArrayList<Image>();
		xPower	= new ArrayList<Float>();
		yPower	= new ArrayList<Float>();
		
		// Initialize commands for Player
		input = new InputProvider(gc.getInput());
		input.addListener(this);
		input.bindCommand(new KeyControl(Input.KEY_UP), moveUP);
		input.bindCommand(new KeyControl(Input.KEY_DOWN), moveDOWN);
		input.bindCommand(new KeyControl(Input.KEY_LEFT), moveLEFT);
		input.bindCommand(new KeyControl(Input.KEY_RIGHT), moveRIGHT);
	
		/*// Start connection to GameServer
		try{
			socket = new DatagramSocket();
		} catch(Exception e) {}
	*/
		t.start();
				
	}
	

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.scale(.5f, .5f);
		map.render(0, 0);
		for(int i = 0; i<players.size(); i++){
			players.get(i).draw(xPos.get(i), yPos.get(i), 32f, 32f);
		}
		
		for(int i = 0; i<powerUp.size(); i++){
			powerUp.get(i).draw(xPower.get(i), yPower.get(i), 32f, 32f);
		}
		
		g.scale(2f, 2f);
		
		g.drawString(test, 0, 20);
		
		// Render Chat Module
		messagesTF.render(gc, g);
		inputTF.render(gc, g);

		
	}

	@SuppressWarnings("unused")
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		
		// For Chat module, listens to ENTER key to send messages.
		if(gc.getInput().isKeyPressed(Input.KEY_ENTER)){
			String message = inputTF.getText().trim();
			if(message.length()!=0) {
				// Send data to the ServerSocket 
				OutputStream outToServer = null;
				try {
					outToServer = client.getOutputStream();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				DataOutputStream out = new DataOutputStream(outToServer);		
				try {
					out.writeUTF(this.name + ": " + message);
				} catch (IOException e) {
					e.printStackTrace();
				}	
			}
			inputTF.setText("");
		}
		
		try{

			// Get player's info from messages, 
			String[] playersInfo = message.split(":");
			for(int i=0; i<playersInfo.length; i++){
				String[] playerInfo = playersInfo[i].split(" ");
				String pname = playerInfo[1].trim();
				//int x = Integer.parseInt(playerInfo[2]);
				float x = Float.parseFloat(playerInfo[2]);
				//int y = Integer.parseInt(playerInfo[3]);
				float y = Float.parseFloat(playerInfo[3]);
				String direction = playerInfo[4].trim();
				boolean isAlive = Boolean.parseBoolean(playerInfo[5]);
				//System.out.println("Player " + pname + " at " + x + " : " + y + " : " + direction + " : " + isAlive);

				switch(direction){
					case "UP":
						players.set(i, movingUp);
						break;
					case "DOWN":
						players.set(i, movingDown);
						break;
					case "LEFT":
						players.set(i, movingLeft);
						break;
					case "RIGHT":
						players.set(i, movingRight);
						break;
				}
				
				xPos.set(i, x * 8);
				yPos.set(i, y * 8);
				
			}
			

			
		} catch(Exception e){}
		
		try{
			
			String[] spawnsInfo = spawn.split(":");
			for(int i = 0; i < spawnsInfo.length; i++){

				String[] spawnInfo = spawnsInfo[i].split(" ");
				
				if(spawnInfo[0].equals("POWERUP") && !Boolean.parseBoolean(spawnInfo[3])){
					xPower.add(Float.parseFloat(spawnInfo[2])*32);
					yPower.add(Float.parseFloat(spawnInfo[1])*32);
					powerUp.add(new Image("assets/sprites/objects/powerUp.png"));
				}
				if(spawnInfo[0].trim() == "FOOD"){
					food.add(new Image("assets/sprites/objects/points.png"));
				}
				
				spawn = new String("");
			}
			

			//test = ""+powerUp.size();
		}catch(Exception e){
			
		}
		
	}

	

	@Override
	public void controlPressed(Command command) {
		try{
			
			String newDirection = ((BasicCommand) command).getName();
			/*
			prevX = currXPos; 
			prevY = currYPos;
			prevDirection = currDirection;
			
			// Changes position every time there is a command, change this to change position every time it changed direction
			switch(newDirection){
			case "UP":
				currYPos -= Y_SPEED;
				break;
			case "DOWN":
				currYPos += Y_SPEED;
				break;
			case "LEFT":
				currXPos -= X_SPEED;
				break;
			case "RIGHT":
				currXPos += X_SPEED;
				break;
			}
			
			currDirection = newDirection;
			*/
			//if(prevX != currXPos || prevY != currYPos){
				//send("PLAYER " + name + " " + currXPos + " " + currYPos + " " + currDirection + " " + currIsAlive);
				send("PLAYER " + name + " " + newDirection );
			//}
			
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Helper method for sending data to server
	 * @param msg
	 */
	public void send(String msg){
		try{
			
			byte[] buf = msg.getBytes();
        	InetAddress address = InetAddress.getByName("127.0.0.1");
        	DatagramPacket packet = new DatagramPacket(buf, buf.length, address, GAME_PORT);
        	
        	//if(packet!=null)
        		socket.send(packet);
        	
        }catch(Exception e){
        	//e.printStackTrace();
        }
		
	}
	
	@Override
	public void controlReleased(Command arg0) {
		
	}

	/**
	 * @return the chat
	 */
	public ChatClient getChat() {
		return chat;
	}

	/**
	 * @param chat the chat to set
	 */
	public void setChat(ChatClient chat) {
		this.chat = chat;
	}
	
	@Override
	public int getID() {
		return PLAY;
	}
}
