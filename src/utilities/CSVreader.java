/**
 * 
 */
package utilities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

/**
 * @author mqcabailo
 *
 */
public class CSVreader {
	private BufferedReader br;
	private String line;
	
	public CSVreader(){
	}
	
	public boolean[][] readCollision(String file){
		boolean[][] bool= new boolean[30][30];
		try {
			br = new BufferedReader(new FileReader(file));
			
			for(int i = 0;(line = br.readLine()) != null;i++) {
				
				String[] values = line.split(",");

				for(int j = 0; j < values.length ; j++){
					if( Integer.parseInt(values[j]) == - 1 ){
						bool[i][j] = false;
					}else{
						bool[i][j] = true;
					}
				}
				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return bool;
	}
	
	public LinkedList<Integer> powerSpawn(String file){
		LinkedList<Integer> powerSpawn = new LinkedList<Integer>();
		try {
			br = new BufferedReader(new FileReader(file));
			
			for(int i = 0;(line = br.readLine()) != null;i++) {
				
				String[] values = line.split(",");

				for(int j = 0; j < values.length ; j++){

					powerSpawn.add(Integer.parseInt(values[j]));
				
				}
				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return powerSpawn;
	}
	
	public LinkedList<Integer> playerSpawns(String file){
		LinkedList<Integer> playerSpawn = new LinkedList<Integer>();
		
		try {
			br = new BufferedReader(new FileReader(file));
			
			for(int i = 0;(line = br.readLine()) != null;i++) {
				
				String[] values = line.split(",");

				for(int j = 0; j < values.length ; j++){
					playerSpawn.add(Integer.parseInt(values[j]));
				}
				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return playerSpawn;
	}
	
}
