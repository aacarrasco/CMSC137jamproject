package chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import framework.Constants;
import window.ChatWindow;

public class ChatClient implements Runnable, Constants {
   
  Socket client;
  String name;
  Thread inThread;
  ChatWindow chatWindow;
  int port;
  
  public ChatClient() throws UnknownHostException, IOException{
		//this.client = new Socket(SERVER_NAME, PORT);
		this.chatWindow = new ChatWindow(this.client);
		this.client = this.chatWindow.getClient();
			
		this.name = this.chatWindow.getName();
		this.port = PORT;
		
		System.out.println(this.name + " is connecting to " + SERVER_NAME + " on port " + this.port);
		System.out.println("Just connected to " + this.client.getRemoteSocketAddress());

		OutputStream outToServer = this.client.getOutputStream();
		DataOutputStream out = new DataOutputStream(outToServer);
		out.writeUTF(this.name + " has joined the conversation.");

		initializeThreads();
		
		Thread t = new Thread(this);
		t.start();
	}


  public void run(){
    
    this.inThread.start();
    
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
          	chatWindow.getMessageTextArea().append("\n" + message);  	
          }
    
        } catch(Exception e){
          //e.printStackTrace();
        }
      
      }
  
    };
  
  }
      

 /* public static void main(String [] args) {

    try {
    
      String serverName = args[0];
      int port = Integer.parseInt(args[1]); 
      String name = args[2]; 

      ChatClient chatClient = new ChatClient(name, serverName, port);
      Thread t = new Thread(chatClient);
      t.start();
    
      
    } catch(ArrayIndexOutOfBoundsException e) {
      System.out.println("Usage: java chatClientent <server ip> <port no.> <name>");
    } catch(Exception e){
      System.out.println("Usage: java ChatClient <server ip> <port no.> <name>");
    }

  }*/
}
