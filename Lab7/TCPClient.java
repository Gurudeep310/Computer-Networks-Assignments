//package ffile;
	import java.io.*;
	import java.net.*;
	import java.util.Scanner;

	class TCPClient{
		public static void main(String args[]) throws Exception{
			String sentence;
			String modifiedSentence;
			// Reading from file
			File myObj = new File("ClientMessage.txt");
			// Accessing contents of file
			Scanner inFromUser = new Scanner(myObj);  
			System.out.println("Sending file to server...");
			// Accessing contents of file line by line
			while(inFromUser.hasNextLine()){
				// Storing a line in curline from file
				String curline = inFromUser.nextLine();
				// Initialising a clientSocket with desired port and ip address
				Socket clientSocket = new Socket("127.0.0.1",6789);
				// Want to send to the server from client
				DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
				// Want to read from server by client
				BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				sentence = curline;
				// Writing it to server
				outToServer.writeBytes(sentence + '\n');
				// Reading the servers meesage 
				modifiedSentence = inFromServer.readLine();
				// Printing the servers message
				System.out.println("From Server: " + modifiedSentence);
				// Closing the client socket
				clientSocket.close();
			}   
		}
	}
