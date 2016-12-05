/**
 * 
 */
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

/**
 * @author aacarrasco
 *
 */
public class ClientSetup extends BasicGameState implements Constants{
	TextField nameTF;
	TextField addressTF;
	private String name;
	private String address;
	String mouse = "No input yet!";
	
	
	public ClientSetup(int state) {
		
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		// TODO Auto-generated method stub
		nameTF = new TextField(gc, gc.getDefaultFont(), 100, 100, 300, 20);
		nameTF.setBackgroundColor(Color.white);
		nameTF.setText("Player");
		nameTF.setTextColor(Color.lightGray);
		nameTF.setAcceptingInput(true);
		
		addressTF = new TextField(gc, gc.getDefaultFont(), 100, 160, 300, 20);
		addressTF.setBackgroundColor(Color.white);
		addressTF.setText("224.0.0.1");
		addressTF.setTextColor(Color.lightGray);
		addressTF.setAcceptingInput(true);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.drawString(mouse, 450, 25);

		g.drawString("Player Setup", 50, 50);
		g.drawString("Enter Name: " + name, 50, 80);
		nameTF.render(gc, g);
		g.drawString("Enter IP Address: " + address, 50, 140);
		addressTF.render(gc, g);
		g.drawString("Join Game", 100, 200);
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
				name = nameTF.getText();
				address = addressTF.getText();
				
				sbg.addState(new Play(PLAY, name, address));
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	@Override
	public int getID() {
		return CLIENT_SETUP;
	}
}
