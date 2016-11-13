import java.net.*;
import java.io.*;
import java.util.*;

public class ChatClient implements Runnable {
  Socket client;
  String name;
  Thread inThread;
  Thread outThread;

  public ChatClient(String name, String serverName, int port){
    
    try{
    
      this.name = name;
      this.client = new Socket(serverName, port);
      
      System.out.println("Connecting to " + serverName + " on port " + port);
      System.out.println("Just connected to " + this.client.getRemoteSocketAddress());

      OutputStream outToServer = client.getOutputStream();
      DataOutputStream out = new DataOutputStream(outToServer);
      out.writeUTF(name + " has joined the conversation.");

      initializeThreads();
    
    } catch(UnknownHostException e) {
      System.out.println("Unknown Host.");
    } catch(IOException e){
      System.out.println("Cannot find Server");
    } 
      
  }

  public void run(){
    
    this.inThread.start();
    this.outThread.start();
  
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
            System.out.println("\n" + in.readUTF());
            System.out.print(name + ": ");              
          }

        } catch(Exception e){
          //e.printStackTrace();
        }
      
      }
    
    };

    // For outgoing messages
    this.outThread = new Thread(){
    
      Scanner sc = new Scanner(System.in);
          
      public void run(){
    
        try{
    
          while(true){
    
            System.out.print(name + ": ");   
            String message = sc.nextLine();
            
            // Send data to the ServerSocket 
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            
            if(message.compareTo("/logout") == 0){
              out.writeUTF(name + " has logged out.");
              client.close();
              break;
            }

            out.writeUTF(name + ": " + message);
            
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
