package window;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import framework.Constants;

public class ChatWindow extends JFrame implements ActionListener, Constants{
	private static final long serialVersionUID = 2170184640092050807L;
	
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
	private String clientAddress;
	private String name;
	
	public ChatWindow(Socket client) throws UnknownHostException, IOException {
		this.name = JOptionPane.showInputDialog(this, "Enter name: ");
		this.clientAddress = JOptionPane.showInputDialog(this, "Enter client address: ");
		this.client = new Socket(clientAddress, PORT);
		
		this.setTitle(this.name);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		c.setPreferredSize(new Dimension(300, 400));
		setChatPanel();
		c.add(chatPanel);
		this.setVisible(true);
		this.pack();
	}
	
	public String getName(){
		return this.name;
	}

	public Socket getClient(){
		return this.client;
	}
	
	public String getClientAddress(){
		return this.clientAddress;
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

	public JTextArea getMessageTextArea() {
		return messageTextArea;
	}
	
	public void setMessageTextArea(JTextArea messageTextArea) {
		this.messageTextArea = messageTextArea;
	}

	
}
