import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class server_HW3 {
    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args){
        int port = 5080;
        boolean listening = true;

        try(ServerSocket serverTCPSocket = new ServerSocket(port)){
            System.out.println("Server connected on port 5080");

            while(listening){
                new TCPServerThread(serverTCPSocket.accept()).start();
            }
        } catch (IOException e){
            System.err.println("Could not listen on port 5080");
            e.printStackTrace();
        }
    }
}

class TCPServerThread extends Thread {
    private final Socket clientTCPSocket;

    public TCPServerThread(Socket socket){
        this.clientTCPSocket = socket;
    }

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public void run(){
        try (
            PrintWriter socketOut = new PrintWriter(clientTCPSocket.getOutputStream(), true);
            BufferedReader socketIn = new BufferedReader(new InputStreamReader(clientTCPSocket.getInputStream()));
         ) {
            String fromClient = socketIn.readLine();
            if(fromClient == null || !fromClient.startsWith("GET")){
                sendError(socketOut, "400 Bad Request");
                return;
            }

            String fileName = fromClient.split(" ")[1].substring(1);
            if (fileName.isEmpty()) fileName = "index.html";
            File file = new File(fileName);

            if(!file.exists()){
                sendError(socketOut, "404 Not Found");
            } else{
                sendSuccess(socketOut, file);
            }
         } catch(IOException e){
            e.printStackTrace();
         }

        try {
            clientTCPSocket.close();
        } catch (IOException ex) {
        }
    }

    private void sendSuccess(PrintWriter serverOut, File file) throws FileNotFoundException, IOException {
        serverOut.println("HTTP/1.1 200 OK");
        serverOut.println("Date: " + new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z").format(new Date()));
        serverOut.println("Server: My_HTTP_Server");
        serverOut.println();

        try(BufferedReader fileReader = new BufferedReader(new FileReader(file))){
            String htmlLine;
            while((htmlLine = fileReader.readLine()) != null){
                serverOut.println(htmlLine);
            }
        }
        serverOut.println("\r\n\r\n\r\n\r\n");
    }

    private void sendError(PrintWriter serverOut, String status){
        serverOut.println("HTTP/1.1 " + status);
        serverOut.println("Date: " + new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z").format(new Date()));
        serverOut.println("Server: My_HTTP_Server");
        serverOut.println();
    }
}
