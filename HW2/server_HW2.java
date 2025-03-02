import java.io.*;
import java.net.*;
import java.util.*;

public class server_HW2 {

    public static void main(String[] args) throws IOException {
        
        DatagramSocket udpServerSocket = new DatagramSocket(5080);
        DatagramPacket udpPacket0, udpPacket1;
        String fromClient, toClient;
        boolean morePackets = true;

        HashMap<String, String> data = new HashMap<>();
        data.put("00001", "New Inspiron 15,$379.99,157");
        data.put("00002", "New Inspiron 17,$449.99,128");
        data.put("00003", "New Inspiron 15R,$549.99,202");
        data.put("00004", "New Inspiron 15Z Ultrabook,$749.99,315");
        data.put("00005", "XPS 14 Ultrabook,$999.99,261");
        data.put("00006", "New XPS 12 UltrabookXPS,$1199.99,178");

        while (morePackets) {
            try {
                
                byte[] buf = new byte[256];
                udpPacket0 = new DatagramPacket(buf, buf.length);
                udpServerSocket.receive(udpPacket0);

                fromClient = new String(udpPacket0.getData(), 0, udpPacket0.getLength(), "UTF-8").trim();
                System.out.println("Received Request for " + fromClient);

                
                if (data.containsKey(fromClient)) {
                    toClient = fromClient + "," + data.get(fromClient);
                } else {
                    toClient = "ERROR, Invalid Item ID";
                }

                InetAddress client = udpPacket0.getAddress();
                int port = udpPacket0.getPort();
                byte[] buf2 = toClient.getBytes("UTF-8");
                udpPacket1 = new DatagramPacket(buf2, buf2.length, client, port);
                udpServerSocket.send(udpPacket1);

            } catch (IOException e) {
                e.printStackTrace();
                morePackets = false;
            }
        }

        udpServerSocket.close();
    }
}
