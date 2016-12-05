/**
 * 
 */
package game;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import utilities.Constants;

/**
 * @author aacarrasco
 * @author mqcabailo
 *
 */
public class PacGame extends StateBasedGame implements Constants{
	
	public PacGame(){
		super(APP_NAME);
		
	}
	
	@Override
	public void initStatesList(GameContainer gc) throws SlickException {	
		this.addState(new Menu(MENU));
		this.addState(new Setup(SETUP));
		//this.addState(new Play(PLAY));
		this.addState(new HowTo(HOW_TO));
		this.enterState(MENU);
		
		/*this.getState(MENU).init(gc, this);
		this.getState(PLAY).init(gc, this);
		this.getState(HOW_TO).init(gc, this);
		this.getState(CLIENT_SETUP).init(gc, this);
		this.enterState(MENU);*/
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AppGameContainer appgc;
		
		try{
			appgc = new AppGameContainer(new PacGame());
			appgc.setDisplayMode(800, 400, false);
			appgc.start();
		} catch(SlickException e){
			e.printStackTrace();
		}
	}

	

}
