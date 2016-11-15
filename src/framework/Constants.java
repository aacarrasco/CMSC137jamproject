/**
 * 
 */
package framework;

/**
 * @author aacarrasco
 *
 */
public interface Constants {
	public static final String APP_NAME="Pac-Ganern!";
	public static final String SERVER_NAME = "127.0.0.1";
	/**
	 * Game states.
	 */
	public static final int GAME_START=0;
	public static final int IN_PROGRESS=1;
	public final int GAME_END=2;
	public final int WAITING_FOR_PLAYERS=3;
	
	/**
	 * Game port
	 */
	public static final int PORT=4444;
	public static final int GAME_PORT=1337;
}
