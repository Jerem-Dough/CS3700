import java.io.*;
import java.net.*;

public class client_HW3 {
    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java client <server-address>");
            return;
        }

        String serverAddress = args[0];
        int serverPort = 5080;
        Socket clientSocket = null;
        PrintWriter outputStream = null;
        BufferedReader inputStream = null;
        BufferedReader userReader = null;

        try {
            clientSocket = new Socket(serverAddress, serverPort);
            outputStream = new PrintWriter(clientSocket.getOutputStream(), true);
            inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            userReader = new BufferedReader(new InputStreamReader(System.in));
            
            System.out.println("Connected to " + serverAddress);
            
            while (true) {
                System.out.print("HTTP Method: ");
                String httpMethod = userReader.readLine().trim();
                
                System.out.print("Resource Path: ");
                String resourcePath = userReader.readLine().trim();
                
                System.out.print("HTTP Version (HTTP/1.X): ");
                String httpVer = userReader.readLine().trim();
                
                System.out.print("User-Agent: ");
                String userAgentInfo = userReader.readLine().trim();
                
                long requestStart = System.currentTimeMillis();
                outputStream.println(httpMethod + " /" + resourcePath + " " + httpVer);
                outputStream.println("Host: " + serverAddress);
                outputStream.println("User-Agent: " + userAgentInfo);
                outputStream.println();
                
                String serverResponse;
                boolean isHeaderProcessed = false;
                StringBuilder responseContent = new StringBuilder();
                
                while ((serverResponse = inputStream.readLine()) != null) {
                    System.out.println(serverResponse);
                    if (serverResponse.isEmpty() && !isHeaderProcessed) {
                        isHeaderProcessed = true;
                        continue;
                    }
                    if (isHeaderProcessed) {
                        responseContent.append(serverResponse).append("\n");
                    }
                }
                
                long requestEnd = System.currentTimeMillis();
                System.out.println("RTT: " + (requestEnd - requestStart) + " ms");
                
                try (FileWriter responseFile = new FileWriter(resourcePath)) {
                    responseFile.write(responseContent.toString());
                }
                
                System.out.print("Would you like run the program again? (yes/no): ");
                if (!userReader.readLine().trim().equalsIgnoreCase("yes")) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (clientSocket != null) clientSocket.close();
                if (outputStream != null) outputStream.close();
                if (inputStream != null) inputStream.close();
                if (userReader != null) userReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
