/**
 * 
 */
package game;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import utilities.Constants;


/**
 * @author aacarrasco
 *
 */
public class Menu extends BasicGameState implements Constants{
	public String mouse = "No input yet!";
	private Image logo;
	
	public Menu(int state) {
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		// TODO Auto-generated method stub
		logo = new Image("assets/png/logoMin.png");
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.drawString(APP_NAME, 50, 50);
		g.drawString(mouse, 700, 25);
		logo.draw(100, 50);
		g.drawString("HOW TO PLAY", 50, 350);
		g.drawString("PLAY", 360, 350);
		g.drawString("EXIT", 650, 350);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		Input input = gc.getInput();
		int xPos = Mouse.getX();
		int yPos = Mouse.getY();
		
		mouse = "X: " + xPos + "\nY: " + yPos;
		if((xPos > 40 && xPos < 155) && (yPos > 30 && yPos < 50)){
			if(input.isMouseButtonDown(0)){
				sbg.enterState(HOW_TO);
			}
		}
		if((xPos > 350 && xPos < 400) && (yPos > 30 && yPos < 50)){
			if(input.isMouseButtonDown(0)){
				sbg.enterState(SETUP);
			}
		}
		
		if((xPos > 640 && xPos < 700) && (yPos > 30 && yPos < 50)){
			if(input.isMouseButtonDown(0)){
				System.exit(0);
			}
		}
	}

	@Override
	public int getID() {
		return MENU;
	}

}
