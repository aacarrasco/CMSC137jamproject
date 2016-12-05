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
public class HowTo extends BasicGameState implements Constants{
	private Image image;
	
	public HowTo(int state) {
		
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		// TODO Auto-generated method stub
		image = new Image("assets/png/helpPage.png");
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.drawString("HOW TO PLAY", 50, 50);
		
		image.draw(300, 0, image.getWidth()/1.5f, image.getHeight()/1.5f);
		
		g.drawString("Go Back to Menu", 100, 300);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		Input input = gc.getInput();
		int xPos = Mouse.getX();
		int yPos = Mouse.getY();
		
		if((xPos > 95 && xPos < 245) && (yPos > 80 && yPos < 100)){
			if(input.isMouseButtonDown(0)){
				sbg.enterState(MENU);
			}
		}
	}

	@Override
	public int getID() {
		return HOW_TO;
	}
}
