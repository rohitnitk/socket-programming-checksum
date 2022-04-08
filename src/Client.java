// A Java program for a Client
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    // initialize socket and input output streams
    private Socket socket = null;
    private DataInputStream input = null;
    private DataOutputStream out = null;

    // constructor to put ip address and port
    public Client(String address, int port) {
        // establish a connection
        try {
            socket = new Socket(address, port);
            System.out.println("Connected");

            // takes input from terminal
            input = new DataInputStream(System.in);

            // sends output to the socket
            out = new DataOutputStream(socket.getOutputStream());
        } catch (UnknownHostException u) {
            System.out.println(u);
        } catch (IOException i) {
            System.out.println(i);
        }

        // string to read message from input
        String line = "";

        

        byte checkSum;
        int corruptdata = 0;
        // keep reading until "Over" is input
        while (!line.equals("Over")) {
            corruptdata++;
            try {
                line = input.readLine();

                System.out.print("len:");
                System.out.println(line.length());

                byte[] dataInBytes = new byte[10000];
                dataInBytes = line.getBytes();
                 for (int i = 0; i < line.length(); i++) {
                 System.out.println(dataInBytes[i]);
                 }
                 checkSum = calculateCheckSum(dataInBytes);
                 out.writeInt(line.length() + 1);
                 if (corruptdata % 2 == 0) {
                    if (((dataInBytes[0] >> 6) & 1) == 1)
                        dataInBytes[0] = (byte) (dataInBytes[0] & ~(1 << 5));
                } else {
                    dataInBytes[0] = (byte) (dataInBytes[0] | (1 << 6));
                }
                out.write(dataInBytes);
                out.write(checkSum);
                System.out.println("checkSUM: ");
                System.out.println(checkSum);
            } catch (IOException i) {
                System.out.println(i);
            }
        }

        // close the connection
        try {
            input.close();
            out.close();
            socket.close();
        }
        catch (IOException i) {
            System.out.println(i);
        }
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
        Client client = new Client("127.0.0.1", 5000);
    }
}
