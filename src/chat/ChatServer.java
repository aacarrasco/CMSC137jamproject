package chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import framework.Constants;

/**
 * Server for chat module
 * @author aacarrasco
 *
 */

public class ChatServer implements Runnable, Constants {
	private ServerSocket serverSocket;
	int numClients;

	public ChatServer(int numClients) throws IOException {
		
		serverSocket = new ServerSocket(CHAT_PORT);
		this.numClients = numClients;
		
		Thread t = new Thread(this);
		t.start();
	}

	public void run() {
		while(true) { // continuously waits for clients to connect
			try {
			System.out.println("CS: Waiting for clients on port " + serverSocket.getLocalPort() + "...");
			
				Socket client = serverSocket.accept();
	
				// Set serverListener and add new client to clientList
				ChatServerListener serverListener = new ChatServerListener(client);
				ChatServerListener.clientList.add(serverListener);

				Thread t = new Thread(serverListener);
				t.start();
				
				System.out.println("CS: Just connected to " + client.getRemoteSocketAddress());
				
			} catch(IOException e) {
				break;
			}
		} 
	}

}

