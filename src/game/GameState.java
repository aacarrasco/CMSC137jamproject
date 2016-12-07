/**
 * 
 */
package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

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
	
	
	public void setSpawnPoints(LinkedList<Integer> powerSpawn){
		while(!powerSpawn.isEmpty()){
			powerUps.add(new PowerUp(powerSpawn.pop(), powerSpawn.pop()));
		}
	}
	
	public boolean powerIsAt(int x, int y){
		for(int i = 0; i < powerUps.size(); i++){
			if(x == powerUps.get(i).getX() && y == powerUps.get(i).getY())
				return true;
		}
		
		return false;
	}
	
	public int powerAt(int x, int y){
		for(int i = 0; i < powerUps.size(); i++){
			if(x == powerUps.get(i).getX() && y == powerUps.get(i).getY())
				return i;
		}
		
		return -1;
	}
	
	public ArrayList<PowerUp> getPowerUps(){
		return powerUps;
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
