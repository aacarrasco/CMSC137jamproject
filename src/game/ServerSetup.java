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

public class ServerSetup extends BasicGameState implements Constants{
	TextField numPlayersTF;
	TextField addressTF;
	private int numPlayers;
	private String address;
	String mouse = "No input yet!";
	String status = "";
	
	
	public ServerSetup(int state) {
		
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		// TODO Auto-generated method stub
		numPlayersTF = new TextField(gc, gc.getDefaultFont(), 100, 100, 300, 20);
		numPlayersTF.setBackgroundColor(Color.white);
		numPlayersTF.setText("1");
		numPlayersTF.setTextColor(Color.lightGray);
		numPlayersTF.setAcceptingInput(true);
		
		addressTF = new TextField(gc, gc.getDefaultFont(), 100, 160, 300, 20);
		addressTF.setBackgroundColor(Color.white);
		addressTF.setText("224.0.0.1");
		addressTF.setTextColor(Color.lightGray);
		addressTF.setAcceptingInput(true);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.drawString(mouse, 450, 25);

		g.drawString("Server Setup", 50, 50);
		g.drawString("Enter Number of Players: " + numPlayers, 50, 80);
		numPlayersTF.render(gc, g);
		g.drawString("Enter IP Address: " + address, 50, 140);
		addressTF.render(gc, g);
		g.drawString("Host Game", 100, 200);
		g.drawString(status, 100, 250);		
		g.drawString("Go Back to Menu", 100, 300);
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		
		Input input = gc.getInput();
		int xPos = Mouse.getX();
		int yPos = Mouse.getY();
		
		mouse = "X: " + xPos + " Y: " + yPos;
		if((xPos > 95 && xPos < 185) && (yPos > 180 && yPos < 200)){
			if(input.isMouseButtonDown(0)){
				numPlayers = Integer.parseInt(numPlayersTF.getText());
				address = addressTF.getText();
				
				new GameServer(numPlayers, address);
				status = "Game hosted at " + address;
				
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
		return SERVER_SETUP;
	}
}
