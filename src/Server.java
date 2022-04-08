
// A Java program for a Server
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Server {
    // initialize socket and input stream
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream in = null;

    // constructor with port
    public Server(int port) {
        // starts server and waits for a connection
        try {
            server = new ServerSocket(port);
            System.out.println("Server started");

            System.out.println("Waiting for a client ...");

            socket = server.accept();
            System.out.println("Client accepted");

            // takes input from the client socket
            in = new DataInputStream((socket.getInputStream()));

            String line = "";
            // reads message from client until "Over" is sent
            while (!line.equals("Over")) {
                try {

                    int length = in.readInt(); // length of data

                    byte[] data = new byte[length];
                    byte[] message = new byte[length - 1]; // skip last byte as its checksum
                    for (int i = 0; i < data.length; i++) {
                        data[i] = in.readByte();
                        if (i < length - 1) {
                            message[i] = data[i];

                        }
                     }
                     System.out.print("\n\nData in bytes (with last byte as checksum):");
                     System.out.println(Arrays.toString(data));
                     byte checkSum = calculateCheckSum(data);
                     System.out.print("Calculated CheckSum at receiver:");
                     System.out.println(checkSum);
                     if (checkSum == 0) {
                         System.out.println("VALID DATA..");
                     } else {
                         System.out.println("FORGED DATA!!");
                     }

                     System.out.println("message:" + new String(message, "UTF-8"));


                } catch (IOException i) {
                    System.out.println(i);
                }
            }
            System.out.println("Closing connection");

            // close connection
            socket.close();
            in.close();
        } catch (IOException i) {
            System.out.println(i);
        }
    }

    private boolean isCheckSumValid(byte checkSum) {
        // TODO Auto-generated method stub
        return true;
    }

    public byte calculateCheckSum(byte[] dataInBytes) {
        int sum = 0;
        for (int i = 0; i < dataInBytes.length; i++) {
            sum += (dataInBytes[i]);
            int carry = (sum >> 7) & 1;
            if (carry == 1) {
                sum = sum & 127;
                sum += 1;
            }
        }

        byte checksum = (byte) (sum ^ 127);
        return checksum;
    }

    public static void main(String args[]) {
        Server server = new Server(5000);
    }
}
