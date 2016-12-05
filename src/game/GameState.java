/**
 * 
 */
package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author aacarrasco
 * @author mqcabailo
 *
 */
public class GameState {
	private HashMap<String, NetPlayer> players = new HashMap<String, NetPlayer>();
	private ArrayList<PowerUp> powerUps = new ArrayList<PowerUp>();
	public GameState(){}
	
	/**
	 * Update the game state. Called when player moves
	 * @param name
	 * @param player
	 */
	public void update(String name, NetPlayer player){
		players.put(name,player);
	}
	
	/**
	 * String representation of this object. Used for data transfer
	 * over the network
	 */
	public String toString(){
		String retval="";
		for(Iterator<String> ite=players.keySet().iterator();ite.hasNext();){
			String name=(String)ite.next();
			NetPlayer player=(NetPlayer)players.get(name);
			retval+=player.toString()+" :";
		}
		return retval;
	}
	
	/**
	 * Returns the map
	 * @return
	 */
	public HashMap<String, NetPlayer> getPlayers(){
		return players;
	}
	
	
	public void setSpawnPoints(int[][] spawnPoints){
		for(int i = 0; i < 30; i++){
			for(int j = 0; j < 30; j++){
				if(spawnPoints[i][j] == 1){
					powerUps.add(new PowerUp(i, j));
				}
			}
		}
	}
	
	public String spawnPointsToString(){
		String retval = "";
		for(int i = 0; i < powerUps.size(); i++){
			retval += powerUps.get(i).toString();
			retval += " :";
			
			powerUps.get(i).setUp(true);
		}
		return retval;
	}
}
