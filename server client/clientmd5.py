# client.py
import socket

# Create a socket object
s = socket.socket()          

# Define the port on which you want to connect
port = 65434                

# Connect to the server on local computer
s.connect(('127.0.0.1', port))

# Send a message to the server
message = str(input())
s.send(message.encode('utf-8'))

# Receive the response from the server
print(s.recv(1024).decode('utf-8'))

# Close the connection
s.close()
