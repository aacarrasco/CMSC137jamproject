package chat;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import utilities.Constants;

/**
 * Client for chat module
 * @author aacarrasco
 *
 */
public class ChatClient extends JFrame implements Runnable, Constants, ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8257368193064792037L;
	Container c = getContentPane();
	JPanel chatPanel = new JPanel(new BorderLayout());
	JPanel inputPanel = new JPanel();
	JPanel messagePanel = new JPanel();
	JButton sendButton = new JButton("Send");
	boolean isPressed;
	
	JScrollPane inputScrollPane = new JScrollPane();
	JTextArea inputTextArea = new JTextArea(2, 15);
	
	JScrollPane messageScrollPane = new JScrollPane();
	private JTextArea messageTextArea = new JTextArea();
	
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
		
		this.setTitle(this.name);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		c.setPreferredSize(new Dimension(300, 400));
		setChatPanel();
		c.add(chatPanel);
		this.setVisible(true);
		this.pack();
	}
	
	public void setChatPanel(){
		getMessageTextArea().setLineWrap(true);
		getMessageTextArea().setEditable(false);
		inputTextArea.setLineWrap(true);
		messageScrollPane.setViewportView(getMessageTextArea());
		inputScrollPane.setViewportView(inputTextArea);
		
		sendButton.addActionListener(this);
		
		inputPanel.add(inputScrollPane);
		inputPanel.add(sendButton);
		
		chatPanel.add(messageScrollPane, BorderLayout.CENTER);
		chatPanel.add(inputPanel, BorderLayout.SOUTH);
		
	}
	
	public JTextArea getMessageTextArea() {
		return messageTextArea;
	}
	
	public void setMessageTextArea(JTextArea messageTextArea) {
		this.messageTextArea = messageTextArea;
	}

	
	
	public void actionPerformed(ActionEvent ae){
		try{
			if(ae.getSource() == sendButton){
				String message = inputTextArea.getText().trim();
				if(message.length()!=0) {
					// Send data to the ServerSocket 
					OutputStream outToServer = client.getOutputStream();
					DataOutputStream out = new DataOutputStream(outToServer);
					
					System.out.println(this.name + ": " + message);
					out.writeUTF(this.name + ": " + message);
					
				}
				
				inputTextArea.setText("");
		
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	
	public void run(){
		
		try{
			Thread.sleep(1000);
			// Receive data from the ServerSocket
			InputStream inFromServer = client.getInputStream();
			DataInputStream in = new DataInputStream(inFromServer);

			while(true){
				String message = in.readUTF();
				
				messageTextArea.append("\n" + message);
				
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		
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
