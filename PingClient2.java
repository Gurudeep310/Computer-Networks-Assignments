import java.io.*;
import java.net.*;
import java.util.*;

public class PingClient2{
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

        //Create a datagram socket for receiving and sending UDP packets through the port provided on the command line.
        DatagramSocket clientSocket = new DatagramSocket(CLIENT_PORT);
        clientSocket.setSoTimeout(TIMEOUT);

        // Request ping after 1 second
        Timer timer = new Timer();
        ExecuteTask executeTask = new ExecuteTask(MAX_REQUESTS, clientSocket, IPAddress, serverPort);
        timer.schedule(executeTask, 0, 1000);
    }
   
    //Request/Reply ping.

    public static void ping(DatagramSocket clientSocket, int sequence_number, InetAddress IPAddress, int serverPort) {
       // Creating Server response Datagram Packet
        DatagramPacket response = new DatagramPacket(new byte[1024], 1024);

        // Setting A timer
        Date date = new Date();
        long timestamp = date.getTime();

        // Creating a message which will be sent to the server
        String sendMessage = "PING " + sequence_number + " " + timestamp + " \r\n";

        // Converting message to bytes
        byte[] sendData = new byte[1024];
        sendData = sendMessage.getBytes();

        // Sending Datagram request packet to the server
        DatagramPacket pingRequest = new DatagramPacket(sendData, sendData.length, IPAddress, serverPort);

        try {
            clientSocket.send(pingRequest);

            // Try
            clientSocket.receive(response);

            // If received response from server caculate time taken
            date = new Date();
            long delayReceived = date.getTime() - timestamp;
            System.out.print("Delay " + delayReceived + " ms: ");
            printData(response);
        } catch (SocketTimeoutException e) {
            System.out.print("Packet Lost. Lost Packet: " + sendMessage);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
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

class ExecuteTask extends TimerTask {
    private int maxPingRequests;
    private int sequence_number = -1;
    private DatagramSocket clientSocket;
    private InetAddress IPAddress;
    private int serverPort;
   

    public ExecuteTask(int maxPingRequests, DatagramSocket clientSocket, InetAddress IPAddress, int serverPort) {
        this.maxPingRequests = maxPingRequests;
        this.clientSocket = clientSocket;
        this.IPAddress = IPAddress;
        this.serverPort = serverPort;
    }

    public void run(){
        if (++this.sequence_number < this.maxPingRequests) 
        {
            PingClient2.ping(this.clientSocket, this.sequence_number, this.IPAddress, this.serverPort);
        } else 
        {
            System.exit(0);
        }
    }
}
