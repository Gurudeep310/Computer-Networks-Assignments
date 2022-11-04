import java.io.*;
import java.net.*;
import java.util.*;

public class PingClient{
    private static final int TIMEOUT = 1000; // milliseconds
    private static final int MAX_REQUESTS = 10; // Number of Requests
    private static final int CLIENT_PORT = 5000;

    public static void main(String[] arg) throws Exception {

        if (arg.length != 2) {
            System.out.println("Required arguments: host and port");
            return;
        }

        // Getting server host name
        String servername = arg[0];
        String portnumber = arg[1];
        InetAddress IPAddress = InetAddress.getByName(servername);
        int serverPort = Integer.parseInt(portnumber);

        // Create a datagram socket for receiving and sending UDP packets through the port provided on the command line.
        DatagramSocket clientSocket = new DatagramSocket(CLIENT_PORT);
        clientSocket.setSoTimeout(TIMEOUT);
        for(int sequence_number = 0; sequence_number<MAX_REQUESTS; sequence_number++){
            // Creating Server response Datagram Packet
            DatagramPacket response = new DatagramPacket(new byte[1024], 1024);

            // Setting A timer
            Date date = new Date();
            long timestamp = date.getTime();  // Gets the time

            // Creating a message which will be sent to the server
            String sendMessage = "PING " + sequence_number + " " + timestamp + "\r\n";

            // Converting message to bytes
            byte[] sendData = new byte[1024];
            sendData = sendMessage.getBytes();

            // Sending Datagram request packet to the server
            DatagramPacket pingRequest = new DatagramPacket(sendData, sendData.length, IPAddress, serverPort);
            clientSocket.send(pingRequest);

            // Receiving Response from the server
            try {
                // Try
                clientSocket.receive(response);

                // If received response from server caculate time taken
                date = new Date();
                long delayReceived = date.getTime() - timestamp;

                System.out.print("Delay " + delayReceived + " ms: ");
                printData(response);
            }
            catch (SocketTimeoutException e) {
                System.out.println("Packet Lost. Lost Packet: " + sendMessage);
            }
        }
    }

    //Print ping data to the standard output stream.
    private static void printData(DatagramPacket request) throws Exception {

        // Get the request data
        byte[] receiveData = request.getData();

        // Wrap the bytes in a byte array input stream,so that you can read the data as a stream of bytes.
        ByteArrayInputStream bais = new ByteArrayInputStream(receiveData);

        // Wrap the byte array output stream in an input stream reader,so you can read the data as a stream of characters.
        InputStreamReader isr = new InputStreamReader(bais);

        // Wrap the input stream reader in a bufferred reader,so you can read the character data a line at a time.
        BufferedReader br = new BufferedReader(isr);

        // The message data is contained in a single line, so read this line.
        String line = br.readLine();

        // Print host address and data received from it.
        System.out.println("Received from " + request.getAddress().getHostAddress() + ": " + new String(line));
    }
 }