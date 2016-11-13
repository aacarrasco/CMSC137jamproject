package chat;

import java.net.*;
import java.io.*;

public class ChatClient implements Runnable {
   
  Socket client;
  String name;
  Thread inThread;
  ChatWindow chatWindow;
  
  public ChatClient(String name, String serverName, int port){
    
    try{
    
      this.name = name;
      this.client = new Socket(serverName, port);
      
      System.out.println(this.name + " is connecting to " + serverName + " on port " + port);
      System.out.println("Just connected to " + this.client.getRemoteSocketAddress());

      OutputStream outToServer = client.getOutputStream();
      DataOutputStream out = new DataOutputStream(outToServer);
      out.writeUTF(name + " has joined the conversation.");

      initializeThreads();
      
      chatWindow = new ChatWindow(name, this.client);
     
    } catch(UnknownHostException e) {
      System.out.println("Unknown Host.");
    } catch(IOException e){
      System.out.println("Cannot find Server");
    } 
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
          	chatWindow.messageTextArea.append("\n" + message);  	
          }
    
        } catch(Exception e){
          //e.printStackTrace();
        }
      
      }
  
    };
  
  }
      

  public static void main(String [] args) {

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

  }
}
