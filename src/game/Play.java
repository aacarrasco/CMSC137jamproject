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
import org.newdawn.slick.geom.Circle;
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
	private boolean isCurrAlive;
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
	private ArrayList<Circle> playerBounds;
	private ArrayList<Float> xPos;
	private ArrayList<Float> yPos;
	
	private ArrayList<Image> food;
	private ArrayList<Image> powerUp;
	private ArrayList<Circle> powerBounds;
	private ArrayList<Float> xPower;
	private ArrayList<Float> yPower;
	//private ArrayList<Boolean> powerIsUp;
	
	// Animations
	private int[] normalDuration 	= {150, 150, 150, 150};
	//private int[] powerDuration		= {75, 75, 75, 75};	
	
	private Animation[] movingUp = new Animation[COLOR_COUNT];
	private Animation[] movingDown = new Animation[COLOR_COUNT];
	private Animation[] movingRight = new Animation[COLOR_COUNT];
	private Animation[] movingLeft = new Animation[COLOR_COUNT];
	
	private Animation[] movingUpPower = new Animation[COLOR_COUNT];
	private Animation[] movingDownPower = new Animation[COLOR_COUNT];
	private Animation[] movingRightPower = new Animation[COLOR_COUNT];
	private Animation[] movingLeftPower = new Animation[COLOR_COUNT];
	
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
		this.isCurrAlive = true;
		try {
			this.address = InetAddress.getByName(server);
			this.socket  = new DatagramSocket();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (Exception e1){
			e1.printStackTrace();
		}
		
			
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
						switch(Integer.parseInt(token[4])){
							case 1:
								map	= new TiledMap("assets/tmx/map3_4.tmx");
								break;
							case 3:	
							case 4:
								map	= new TiledMap("assets/tmx/map3_4.tmx");
								break;
							case 5:
							case 6:
								map	= new TiledMap("assets/tmx/map5_6.tmx");
								break;
						}
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
							playerBounds.add(new Circle(Math.round(xPos.get(i)), Math.round(yPos.get(i)), 20));
						}
						
					}
					// Gets the position of all players in the game.
					if(serverData.startsWith("PLAYER")){
						//message = serverData.substring(6);
						message = serverData;
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
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {		
		
		//map
		map	= new TiledMap("assets/tmx/map5_6.tmx");
		
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
		
		// Initialize chat module
		setChat(new ChatClient(this.name, this.server));
		client = chat.getClient();
			
		chat.setMessagesTF(messagesTF);
		
		// Animate players here
		Image[][] up = new Image[COLOR_COUNT][4];
		Image[][] down = new Image[COLOR_COUNT][4];
		Image[][] left = new Image[COLOR_COUNT][4];
		Image[][] right = new Image[COLOR_COUNT][4];
		
		Image[][] upPower = new Image[COLOR_COUNT][4];
		Image[][] downPower = new Image[COLOR_COUNT][4];
		Image[][] leftPower = new Image[COLOR_COUNT][4];
		Image[][] rightPower = new Image[COLOR_COUNT][4];
		
		for(int i=0; i<COLOR_COUNT; i++){
			for(int j=0; j<4; j++){
				String upPath = "assets/sprites/pakmen/U" + (i+1) + "-" + (j+1) + ".png";
				String downPath = "assets/sprites/pakmen/D" + (i+1) + "-" + (j+1) + ".png";
				String leftPath = "assets/sprites/pakmen/L" + (i+1) + "-" + (j+1) + ".png";
				String rightPath = "assets/sprites/pakmen/R" + (i+1) + "-" + (j+1) + ".png";
				
				String upPowerPath = "assets/sprites/pakmen/PowerU" + (i+1) + "-" + (j+1) + ".png";
				String downPowerPath = "assets/sprites/pakmen/PowerD" + (i+1) + "-" + (j+1) + ".png";
				String leftPowerPath = "assets/sprites/pakmen/PowerL" + (i+1) + "-" + (j+1) + ".png";
				String rightPowerPath = "assets/sprites/pakmen/PowerR" + (i+1) + "-" + (j+1) + ".png";
				
				up[i][j] = new Image(upPath);
				down[i][j] = new Image(downPath);
				left[i][j] = new Image(leftPath);
				right[i][j] = new Image(rightPath);
				
				upPower[i][j] = new Image(upPowerPath);
				downPower[i][j] = new Image(downPowerPath);
				leftPower[i][j] = new Image(leftPowerPath);
				rightPower[i][j] = new Image(rightPowerPath);
			}
		}
		
		for(int i=0; i<COLOR_COUNT; i++){
			movingUp[i] = new Animation(up[i], normalDuration, true);
			movingDown[i] = new Animation(down[i], normalDuration, true);
			movingLeft[i] = new Animation(left[i], normalDuration, true);
			movingRight[i] = new Animation(right[i], normalDuration, true);
			
			movingUp[i].start();
			movingDown[i].start();
			movingLeft[i].start();
			movingRight[i].start();
			
			movingUp[i].setLooping(true);
			movingDown[i].setLooping(true);
			movingRight[i].setLooping(true);
			movingLeft[i].setLooping(true);
			
			movingUpPower[i] = new Animation(upPower[i], normalDuration, true);
			movingDownPower[i] = new Animation(downPower[i], normalDuration, true);
			movingLeftPower[i] = new Animation(leftPower[i], normalDuration, true);
			movingRightPower[i] = new Animation(rightPower[i], normalDuration, true);
			
			movingUpPower[i].start();
			movingDownPower[i].start();
			movingLeftPower[i].start();
			movingRightPower[i].start();
			
			movingUpPower[i].setLooping(true);
			movingDownPower[i].setLooping(true);
			movingRightPower[i].setLooping(true);
			movingLeftPower[i].setLooping(true);
			
		}
				
		players = new ArrayList<Animation>();
		playerBounds = new ArrayList<Circle>();
		xPos 	= new ArrayList<Float>();
		yPos	= new ArrayList<Float>();
		
		powerUp	= new ArrayList<Image>();
		powerBounds = new ArrayList<Circle>();
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
			players.get(i).draw(xPos.get(i), yPos.get(i), 64f, 64f);
			
			g.draw(playerBounds.get(i));
		}
		
		for(int i = 0; i<powerUp.size(); i++){
			powerUp.get(i).draw(xPower.get(i), yPower.get(i), 64f, 64f);
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
				int playerNo = Integer.parseInt(playerInfo[1]);
				String pname = playerInfo[2].trim();
				float x = Float.parseFloat(playerInfo[3]);
				float y = Float.parseFloat(playerInfo[4]);
				String direction = playerInfo[5].trim();
				int score = Integer.parseInt(playerInfo[6]);
				boolean powerUp = Boolean.parseBoolean(playerInfo[7]);
				boolean isAlive = Boolean.parseBoolean(playerInfo[8]);
				//System.out.println("Player " + pname + " at " + x + " : " + y + " : " + direction + " : " + isAlive);
				//int playerNo = Integer.parseInt(playerInfo[8]);
				
				if(isAlive && !powerUp){
					switch(direction){
					case "UP":
						players.set(i, movingUp[playerNo]);
						break;
					case "DOWN":
						players.set(i, movingDown[playerNo]);
						break;
					case "LEFT":
						players.set(i, movingLeft[playerNo]);
						break;
					case "RIGHT":
						players.set(i, movingRight[playerNo]);
						break;
					}
				} else if(isAlive && powerUp){
					switch(direction){
					case "UP":
						players.set(i, movingUpPower[playerNo]);
						break;
					case "DOWN":
						players.set(i, movingDownPower[playerNo]);
						break;
					case "LEFT":
						players.set(i, movingLeftPower[playerNo]);
						break;
					case "RIGHT":
						players.set(i, movingRightPower[playerNo]);
						break;
					}
				}
				
				xPos.set(i, x * 8);
				yPos.set(i, y * 8);
				
				playerBounds.get(i).setCenterX(x * 8 + 32);
				playerBounds.get(i).setCenterY(y * 8 + 32);
				
			}
			

			
		} catch(Exception e){}
		
		try{
			
			String[] spawnsInfo = spawn.split(":");
			for(int i = 0; i < spawnsInfo.length; i++){
				
				String[] spawnInfo = spawnsInfo[i].split(" ");
				
				if(spawnInfo[0].equals("POWERUP") && !Boolean.parseBoolean(spawnInfo[3])){
					xPower.add(Float.parseFloat(spawnInfo[1]) * 8);
					yPower.add(Float.parseFloat(spawnInfo[2]) * 8);
					
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
		if(isCurrAlive){
			try{
				String newDirection = ((BasicCommand) command).getName();
				send("PLAYER " + name + " " + newDirection );
				
			} catch(Exception e){
				e.printStackTrace();
			}
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

	/**
	 * @return the isCurrAlive
	 */
	public boolean isCurrAlive() {
		return isCurrAlive;
	}

	/**
	 * @param isCurrAlive the isCurrAlive to set
	 */
	public void setCurrAlive(boolean isCurrAlive) {
		this.isCurrAlive = isCurrAlive;
	}
}
