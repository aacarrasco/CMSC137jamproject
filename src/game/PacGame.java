package game;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.command.BasicCommand;
import org.newdawn.slick.command.Command;
import org.newdawn.slick.command.InputProvider;
import org.newdawn.slick.command.InputProviderListener;
import org.newdawn.slick.command.KeyControl;
import org.newdawn.slick.geom.Circle;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class PacGame extends BasicGame implements InputProviderListener, Runnable{
	DatagramSocket socket;
	String server = "localhost";
	
	private InputProvider input;
	private Circle ball; 
	private ArrayList<Circle> balls;
	
	private Command moveUP 		= new BasicCommand("UP");
	private Command moveDOWN 	= new BasicCommand("DOWN");
	private Command moveLEFT 	= new BasicCommand("LEFT");
	private Command moveRIGHT 	= new BasicCommand("RIGHT");
    
	String message = "";
	String serverData;
	
	public PacGame(String gamename){
		super(gamename);
	}
	
	public void init(GameContainer gc) throws SlickException {
		ball = new Circle(0,0,20);
		balls = new ArrayList<Circle>();
		input = new InputProvider(gc.getInput());
		input.addListener(this);
		
		input.bindCommand(new KeyControl(Input.KEY_W), moveUP);
		input.bindCommand(new KeyControl(Input.KEY_S), moveDOWN);
		input.bindCommand(new KeyControl(Input.KEY_A), moveLEFT);
		input.bindCommand(new KeyControl(Input.KEY_D), moveRIGHT);
		
		try{
			socket = new DatagramSocket();
		}catch(Exception e){}
		
		Thread receiver = new Thread(){
			public void run(){
				while(true){
					byte[] buf = new byte[256];
					DatagramPacket packet = new DatagramPacket(buf, buf.length);
					
					try{
			 			socket.receive(packet);
					}catch(Exception ioe){}
						
					if(packet != null){
						serverData = new String(packet.getData(), packet.getOffset(), packet.getLength());
						message = serverData;
					}
				}
			}
		};
		
		receiver.start();
	}
	
	public void render(GameContainer gc, Graphics g) throws SlickException{
		g.setColor(Color.yellow);
		//g.fill(ball);
		
		g.drawString(message,100,150);
		
		for(Circle b : balls){
			g.fill(b);
		}
	}
	
	public void update(GameContainer arg0, int delta) throws SlickException {
		
		try{
			String[] players = message.split(":");
			int size = Integer.parseInt(players[0]);
			
			if(balls.size() < size){
				System.out.println(balls.size());
				for(int i = balls.size(); i < size; i+=1){
					balls.add(new Circle(0,0,20));
				}
			}
			
			for(int i = 0; i < size; i+=1){
				String[] info = players[i+1].split(" ");

				balls.get(i).setCenterX(Float.parseFloat(info[0]));
				balls.get(i).setCenterY(Float.parseFloat(info[1]));
			}
			
		}catch(Exception e){}
		
	}

	public void controlPressed(Command command) {
		try{
			byte[] buf = (""+command).getBytes();
        	InetAddress address = InetAddress.getByName(server);
        	DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 1337);
        	socket.send(packet);
        }catch(Exception e){}

	}
	
	public void controlReleased(Command command) {	
	}
	
	public void run(){
		byte[] buf = new byte[256];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		
		try{
 			socket.receive(packet);
		}catch(Exception ioe){}
			
		if(packet.getAddress() != null){
			serverData = new String(buf);
			message = serverData;
			System.out.println("zxc");
		}
	};
	
	public static void main(String[] args){
		try{
			AppGameContainer appgc;
			appgc = new AppGameContainer(new PacGame("Pac Ganern"));
			appgc.setDisplayMode(640, 480, false);
			appgc.start();
			
		}
		catch (SlickException ex){
			Logger.getLogger(PacGame.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
