package chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.newdawn.slick.gui.TextField;

import utilities.Constants;

/**
 * Client for chat module
 * @author aacarrasco
 *
 */
public class ChatClient implements Runnable, Constants {

	Thread inThread;
	TextField messagesTF;
	
	private Socket client;
	private String name;
	private String address;
	
	public ChatClient(String name, String address) {	
		this.name = name;
		//this.address = address;
		this.address = "127.0.0.1";
		
		try{
			this.client = new Socket(this.address, CHAT_PORT);
		} catch(IOException e){
			e.printStackTrace();
		}
		
		System.out.println("CC: " + this.name + " is connecting to " + SERVER_NAME + " on port " + CHAT_PORT);
		System.out.println("CC: Just connected to " + this.client.getRemoteSocketAddress());

		OutputStream outToServer = null;
		try {
			outToServer = this.client.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		DataOutputStream out = new DataOutputStream(outToServer);
		try {
			out.writeUTF(this.name + " has joined the conversation.");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Thread t = new Thread(this);
		t.start();
	}

	public void setMessagesTF(TextField messagesTF){
		this.messagesTF = messagesTF;
	}
	
	public void run(){
		
		try{
			Thread.sleep(1000);
			// Receive data from the ServerSocket
			InputStream inFromServer = client.getInputStream();
			DataInputStream in = new DataInputStream(inFromServer);

			while(true){
				String message = in.readUTF();
				//chatWindow.getMessageTextArea().append("\n" + message);	
				messagesTF.setText(messagesTF.getText() + "\n" + message);
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	public void initializeThreads(){
		
		// For incoming messages
		this.inThread = new Thread(){
		
			public void run(){
			
				try{
					
					// Receive data from the ServerSocket
					InputStream inFromServer = client.getInputStream();
					DataInputStream in = new DataInputStream(inFromServer);
		
					while(true){
						String message = in.readUTF();
						messagesTF.setText(messagesTF.getText() + "\n" + message);
					}
		
				} catch(Exception e){
					//e.printStackTrace();
				}
			
			}
	
		};
	
	}


	/**
	 * @return the address
	 */
	public String getClientAddress() {
		return address;
	}
	
	/**
	 * @param address the address to set
	 */
	public void setClientAddress(String address) {
		this.address = address;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the client
	 */
	public Socket getClient(){
		return client;
	}
 
}
