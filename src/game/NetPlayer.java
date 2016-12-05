/**
 * 
 */
package game;

import java.net.InetAddress;

/**
 * This class encapsulates a network player
 * @author aacarrasco
 *
 */
public class NetPlayer {
	private InetAddress address;
	private int port;
	private String name;
	private int x, y;
	private int score = 0;
	private boolean isAlive = true;
	private int playerNo;
	private String direction;
	private boolean poweredUp = false;

	/**
	 * Constructor
	 * @param name
	 * @param address
	 * @param port
	 */
	public NetPlayer(String name,InetAddress address, int port){
		this.setAddress(address);
		this.setPort(port);
		this.setName(name);
		this.setPlayerNo(playerNo);
	}

	/**
	 * @return the address
	 */
	public InetAddress getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(InetAddress address) {
		this.address = address;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * String representation. used for transfer over the network
	 */
	public String toString(){
		String retval="";
		retval += "PLAYER ";
		retval += name + " ";
		retval += x + " ";
		retval += y + " ";
		retval += direction + " ";
		retval += score + " ";
		retval += poweredUp + " ";
		retval += isAlive;
		/*retval += isAlive + " ";
		retval += address + " ";
		retval += port;
		*/
		return retval;
	}

	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * @param score the score to set
	 */
	public void setScore(int score) {
		this.score = score;
	}

	/**
	 * @return the isAlive
	 */
	public boolean isAlive() {
		return isAlive;
	}

	/**
	 * @param isAlive the isAlive to set
	 */
	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	/**
	 * @return the playerNo
	 */
	public int getPlayerNo() {
		return playerNo;
	}

	/**
	 * @param playerNo the playerNo to set
	 */
	public void setPlayerNo(int playerNo) {
		this.playerNo = playerNo;
	}

	/**
	 * @return the direction
	 */
	public String getDirection() {
		return direction;
	}

	/**
	 * @param direction the direction to set
	 */
	public void setDirection(String direction) {
		this.direction = direction;
	}
}
