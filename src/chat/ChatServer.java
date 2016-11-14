package chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import framework.Constants;

public class ChatServer implements Runnable, Constants {
  private ServerSocket serverSocket;

  public ChatServer() throws IOException {
		
		serverSocket = new ServerSocket(PORT);
		Thread t = new Thread(this);
		t.start();
	}

  public void run() {
    while(true) { // continuously waits for clients to connect
      try {
        System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
        
        Socket client = serverSocket.accept();
  
        // Set serverListener and add new client to clientList
        ChatServerListener serverListener = new ChatServerListener(client);
        ChatServerListener.clientList.add(serverListener);

        Thread t = new Thread(serverListener);
        t.start();
        
        System.out.println("Just connected to " + client.getRemoteSocketAddress());
        
      } catch(IOException e) {
        System.out.println("Usage: java ChatServer <port no.>");
        break;
      }
    } 
  }

  /*public static void main(String [] args) { 
    try {
       int port = Integer.parseInt(args[0]);
       Thread t = new ChatServer(port);
       t.start();
    } catch(IOException e) {
       System.out.println("Usage: java ChatServer <port no.>");
    } catch(ArrayIndexOutOfBoundsException e) {
       System.out.println("Usage: java ChatServer <port no.> ");
    }
  }*/
}

