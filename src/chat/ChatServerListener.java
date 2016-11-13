package chat;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class ChatServerListener implements Runnable {
  public static ArrayList<ChatServerListener> clientList = new ArrayList<ChatServerListener>();

  Socket socket;
  DataInputStream inputStream;
  DataOutputStream outputStream;

  public ChatServerListener(Socket socket) {
    try {
      // Set up input and output streams for broadcasting.
      this.socket = socket;
      this.inputStream = new DataInputStream(this.socket.getInputStream());
      this.outputStream = new DataOutputStream(this.socket.getOutputStream());
    } catch(Exception e) {

    }
      
  }

  public void run() {
    try {
      while(true){
        String message = inputStream.readUTF();
      
        // Broadcast each message sent by each client in the inputStream to each other clients on the client list.
        for(ChatServerListener listener: clientList){
          if(listener != this){
            listener.outputStream.writeUTF(message);
          }
        }

      }

    } catch(Exception e) {

    }
  }
}