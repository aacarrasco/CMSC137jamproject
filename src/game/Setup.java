package game;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import utilities.Constants;

public class Setup extends BasicGameState implements Constants{
	TextField numPlayersTF;
	TextField addressTF;
	TextField nameTF;
	TextField clientAddressTF;
	TextField hostAddressTF;
	
	private int numPlayers;
	private String address = MULTICAST_ADDRESS;
	private String name;
	private String clientAddress = MULTICAST_ADDRESS;
	private String hostAddress = "127.0.0.1";
	String mouse = "No input yet!";
	String status = "";
	
	
	public Setup(int state) {
		
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		// TODO Auto-generated method stub
		numPlayersTF = new TextField(gc, gc.getDefaultFont(), 100, 100, 200, 20);
		numPlayersTF.setBackgroundColor(Color.white);
		numPlayersTF.setText("1");
		numPlayersTF.setTextColor(Color.lightGray);
		numPlayersTF.setAcceptingInput(true);
		
		addressTF = new TextField(gc, gc.getDefaultFont(), 100, 160, 200, 20);
		addressTF.setBackgroundColor(Color.white);
		addressTF.setText(MULTICAST_ADDRESS);
		addressTF.setTextColor(Color.lightGray);
		addressTF.setAcceptingInput(true);
		
		nameTF = new TextField(gc, gc.getDefaultFont(), 500, 100, 200, 20);
		nameTF.setBackgroundColor(Color.white);
		nameTF.setText("Player");
		nameTF.setTextColor(Color.lightGray);
		nameTF.setAcceptingInput(true);
		name = nameTF.getText();
		
		clientAddressTF = new TextField(gc, gc.getDefaultFont(), 500, 160, 200, 20);
		clientAddressTF.setBackgroundColor(Color.white);
		clientAddressTF.setText(MULTICAST_ADDRESS);
		clientAddressTF.setTextColor(Color.lightGray);
		clientAddressTF.setAcceptingInput(true);
		
		hostAddressTF = new TextField(gc, gc.getDefaultFont(), 500, 220, 200, 20);
		hostAddressTF.setBackgroundColor(Color.white);
		hostAddressTF.setText("127.0.0.1");
		hostAddressTF.setTextColor(Color.lightGray);
		hostAddressTF.setAcceptingInput(true);
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.drawString(mouse, 700, 25);
		
		g.drawString("Server Setup", 50, 50);
		g.drawString("Enter Number of Players: " + numPlayers, 50, 80);
		numPlayersTF.render(gc, g);
		g.drawString("Enter Group Address: " + address, 50, 140);
		addressTF.render(gc, g);
		g.drawString("Host Game", 100, 200);
		g.drawString(status, 100, 250);		
		
		g.drawLine(400, 400, 400, 0);
		g.drawString("Player Setup", 450, 50);
		g.drawString("Enter Name: " + name, 450, 80);
		nameTF.render(gc, g);
		g.drawString("Enter Group Address: " + clientAddress, 450, 140);
		clientAddressTF.render(gc, g);
		g.drawString("Enter Host Address: " + hostAddress, 450, 200);
		hostAddressTF.render(gc, g);
		
		g.drawString("Join Game", 500, 300);
		
		g.drawString("Go Back to Menu", 100, 300);
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		
		Input input = gc.getInput();
		int xPos = Mouse.getX();
		int yPos = Mouse.getY();
		
		mouse = "X: " + xPos + "\nY: " + yPos;
		if((xPos > 95 && xPos < 185) && (yPos > 180 && yPos < 200)){
			if(input.isMouseButtonDown(0)){
				numPlayers = Integer.parseInt(numPlayersTF.getText());
				address = addressTF.getText();
				
				new GameServer(numPlayers, address);
				status = "Game hosted at " + address;
				
			}
		}
		if((xPos > 495 && xPos < 585) && (yPos > 80 && yPos < 100)){
			if(input.isMouseButtonDown(0)){
				numPlayers = Integer.parseInt(numPlayersTF.getText());
				clientAddress = addressTF.getText();
				name = nameTF.getText();
				hostAddress = hostAddressTF.getText();
				
				sbg.addState(new Play(PLAY, name, clientAddress, hostAddress));
				sbg.getState(PLAY).init(gc, sbg);
				sbg.enterState(PLAY);
			}
		}
		if((xPos > 95 && xPos < 245) && (yPos > 80 && yPos < 100)){
			if(input.isMouseButtonDown(0)){
				sbg.enterState(MENU);
			}
		}
	}
	
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @return the numPlayers
	 */
	public int getNumPlayers() {
		return numPlayers;
	}

	/**
	 * @param numPlayers the numPlayers to set
	 */
	public void setNumPlayers(int numPlayers) {
		this.numPlayers = numPlayers;
	}
	
	@Override
	public int getID() {
		return SETUP;
	}
}
