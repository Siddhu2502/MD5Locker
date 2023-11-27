# server.py
import socket
import md5algorithm as md5algorithm

def md5_hash(string):
    result = md5algorithm.md5(string)
    return result

# Create a socket object
s = socket.socket()          

# Define the port on which you want to connect
port = 65434                

# Bind to the port
s.bind(('', port))         

# Put the socket into listening mode
s.listen(5)                 
print("Server is listening")

while True:
    # Establish a connection with the client
    # here c is the client socket
    # addr is the address bound to the socket on the other end of the connection
    c, addr = s.accept()     
    print('Got connection from', addr)

    # Receive the message from the client
    msg = c.recv(1024).decode('utf-8')
    print('Received message:', msg)

    # Perform MD5 hash on the message
    hash_result = md5_hash(msg)

    # Send the hash result back to the client
    c.send(hash_result.encode('utf-8'))

    # Close the connection with the client
    c.close()
