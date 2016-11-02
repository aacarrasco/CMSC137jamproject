# CMSC137jamproject
# Project in Cmsc 137 JAM Project

cd CMSC137jamproject

# Chat
  # compile:
  javac -d bin src/chat/*.java
  
  # run:
  cd bin
      
  # terminal 1, run chat server.
  java ChatServer port_no
    # Ctrl+c to terminate server.


  # terminal n, run chat client
  java ChatClient ip_address port_no name
    # Enter "/logout" to sign off in chat.
