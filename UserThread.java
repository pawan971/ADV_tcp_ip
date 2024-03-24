import java.io.*;
import java.net.*;

public class UserThread extends Thread {
    private Socket socket;
    private ChatServer server;
    private PrintWriter writer;

    public UserThread(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
    }

    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            
            writer = new PrintWriter(output,true);

            writer.println(server.getSessionId());

            printUsers();

            String userName = reader.readLine();
            server.addUserName(userName);

            String serverMessage = "New user connected: " + userName;
            server.broadcast(serverMessage, this);

            String clientMessage;

            do {
                clientMessage = reader.readLine();
                serverMessage = "[" + userName + "]: " + clientMessage;

                /*
                try {
                    Thread.sleep(5000); // 5 seconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                */
                
                long timeoutMs = 5000;
                long timeoutExpiredMs = System.currentTimeMillis() + timeoutMs*(server.getUserNames().size()-1);
                while (true) {
                    long waitMillis = timeoutExpiredMs - System.currentTimeMillis();
                    if (waitMillis <= 0) {
                        // timeout expired
                        break;
                    }
                }
               
                server.broadcast(serverMessage, this);

            } while (!clientMessage.equals("bye"));

            server.removeUser(userName, this);
            socket.close();

            serverMessage = userName + " has left the chat!";
            server.broadcast(serverMessage, this);

        } catch (IOException ex) {
            System.out.println("Error in UserThread: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Sends a list of online users to the newly connected user.
     */
    void printUsers() {
        if (server.hasUsers()) {
            writer.println("Connected users: " + server.getUserNames());
        } else {
            writer.println("No other users connected");
        }
    }
    /**
     * Sends a message to the client.
     */
    void sendMessage(String message) {
        writer.println(message);
    }
}
