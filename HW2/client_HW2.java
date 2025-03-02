import java.io.*;
import java.net.*;
import java.util.*;

public class client_HW2{

    public static void main(String[] args) throws IOException{
   
        int port = 5080;
        DatagramSocket udpSocket = new DatagramSocket();
        Scanner userInput = new Scanner(System.in);  

        System.out.println("Enter the server's DNS or IP address");
        String serverInput = userInput.nextLine().trim();

        boolean userEnd = false;   
        while(!userEnd) {

            System.out.println("\nItem ID      Item Description \n00001        New Inspiron 15 \n00002        New Inspiron 17 \n00003        New Inspiron 15R \n00004        New Inspiron 15z Ultrabook \n00005        XPS 14 Ultrabook \n00006        New XPS 12 UltrabookXPS");

            String itemID;
            boolean invalidID = true;
            do { 
                System.out.print("Enter an Item ID: ");
                itemID = userInput.nextLine().trim();
                if(itemID.matches("0000[1-6]")){
                    invalidID = false;
                }
                else System.out.println("Invalid Item ID, please try again.");
            } while (invalidID);

            InetAddress server = InetAddress.getByName(serverInput);
            byte[] buf0 = itemID.getBytes();
            DatagramPacket udpPacket0 = new DatagramPacket(buf0, buf0.length, server, port);

            long startTime = System.currentTimeMillis();
            udpSocket.send(udpPacket0);

            byte[] buf1 = new byte[256];
            DatagramPacket udpPacket1 = new DatagramPacket(buf1, buf1.length);

            udpSocket.receive(udpPacket1);
            long endTime = System.currentTimeMillis();

            long rtt = endTime - startTime;

            String fromServer = new String(udpPacket1.getData(), 0, udpPacket1.getLength());
            if(fromServer.startsWith("ERROR")){
                System.out.println("Server Response: " + fromServer);
            } else {
                String[] data = fromServer.split(",");
                System.out.printf("%-10s %-25s %-12s %-10s %-10s", 
                                  "Item ID", "Item Description", "Unit Price", "Inventory", "RTT of Query");
                System.out.printf("%-10s %-25s %-12s %-10s %-10d ms",
                        data[0], data[1], data[2], data[3], (endTime - startTime));
            }

            System.out.println("Would you like to continue? Enter 'yes' or 'no'.");
            String userResponse = userInput.nextLine().trim();
            if(!userResponse.equalsIgnoreCase("yes")){
                userEnd = true;
            }
        }

        udpSocket.close();
        userInput.close();
    }
}