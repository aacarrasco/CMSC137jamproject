/**
 * 
 */
package utilities;

/**
 * @author aacarrasco
 *
 */
public interface Constants {
	public static final String APP_NAME="Pac-Ganern!";
	public static final String SERVER_NAME = "127.0.0.1";
	public static final String MULTICAST_ADDRESS = "224.0.0.1";
	
	
	/*
	 * Window States
	 * */
	public static final int MENU = 0;
	public static final int PLAY = 1;
	public static final int SETUP = 2;
	public static final int HOW_TO = 4;
	
	
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
	public static final int CHAT_PORT=4444;
	public static final int GAME_PORT=1337;
	public static final int MULTICAST_PORT=1338;
	
	/*
	 * Error codes
	 * */
	public static final int ERROR_CONNECTION_FAILED = 1;
	public static final int ERROR_NAME_EXISTS = 2;
	
	/*
	 * Player Speeds
	 * */
	public static final int X_SPEED = 1;
	public static final int Y_SPEED = 1;
	
	/*
	 * UI
	 * */
	public static final int COLOR_COUNT = 2;
	
}
