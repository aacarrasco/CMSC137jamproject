package pac;

import java.net.InetAddress;

public class Player {
	private InetAddress address;
	private String direction;
	private int x;
	private int y;
	
	public Player(InetAddress address){
		this.address = address;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public String getDirection(){
		return direction;
	}
	
	public InetAddress getAddress(){
		return address;
	}
	
	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	public void setDirection(String direction){
		this.direction = direction;
	}
}
