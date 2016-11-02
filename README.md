# CMSC137jamproject
Project in Cmsc 137 JAM Project

cd CMSC137jamproject

Chat
  compile:
    javac -d bin src/chat/*.java
  run:
    cd bin
    terminal 1
      java ChatServer <port no.>                      // Run chat server
    
      Ctrl+c to terminate server.
    terminal n
      java ChatClient <ip address> <port no.> <name>  // Run chat client
    
      Enter "/logout" to sign off in chat.
