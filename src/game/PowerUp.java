/**
 * 
 */
package game;

/**
 * @author mqcabailo
 *
 */
public class PowerUp {
	private Boolean isUp;
	private int x;
	private int y;
	
	public PowerUp(int x, int y){
		this.x = x;
		this.y = y;
		this.isUp = false;
	}
	
	public void setUp(Boolean isUp){
		this.isUp = isUp;
	}
	
	public boolean isUp() {
		return isUp;
	}
	
	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}
	
	public String toString(){
		String retval = "POWERUP ";
		retval += x + " ";
		retval += y + " ";
		retval += isUp;
		
		return retval;
	}
}
