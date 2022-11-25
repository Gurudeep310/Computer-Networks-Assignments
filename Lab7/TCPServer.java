//package ffile;
import java.io.*;
import java.net.*;
class TCPServer{
    public static void main(String argv[]) throws Exception{
        String clientSentence;
        String capitalizedSentence;
        // Creating server socket with port number. Note we should have same port number as mentioned in the Client code
        ServerSocket welcomeSocket = new ServerSocket(6789);
        System.out.println("Reading contents of file sent by client...");
        // Always accept the connection whenever the client requests
        while(true){
            // Accept the connection when connection requested
            Socket connectionSocket = welcomeSocket.accept();
            // Create an input stream to read from Client
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            // Create an output stream to send to client
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            // Read things sent by client
            clientSentence = inFromClient.readLine();
            // Convert the information read to captial case.
            capitalizedSentence = clientSentence.toUpperCase() + '\n';
            // Send the capitalised sentence to client thus telling receivied the message correctly.
            outToClient.writeBytes(capitalizedSentence);
        }
    }
}
