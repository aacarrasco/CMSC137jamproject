package chat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ChatWindow extends JFrame implements ActionListener{
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
  JTextArea messageTextArea = new JTextArea();
  
  Socket client;
  String name;
  
  public ChatWindow(String name, Socket client) {
  	this.client = client;
  	this.name = name;
  	
  	this.setTitle(name);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    c.setPreferredSize(new Dimension(300, 400));
    setChatPanel();
    c.add(chatPanel);
    this.setVisible(true);
    this.pack();
  }

  public void setChatPanel(){
    messageTextArea.setLineWrap(true);
    messageTextArea.setEditable(false);
    inputTextArea.setLineWrap(true);
    messageScrollPane.setViewportView(messageTextArea);
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

  
}
