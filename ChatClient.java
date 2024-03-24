import java.net.*;
import java.io.*;

/**
 * This is the chat client program.
 * Type 'bye' to terminte the program.
 */
public class ChatClient {
    private String hostname;
    private int port;
    private String userName;

    public ChatClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public void execute() {
        try {
            Socket socket = new Socket(hostname, port);

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String sessionId = reader.readLine();

            System.out.println("\nYou're now connected to the ChatServer !\n");
            System.out.println("--------------------------------");
            System.out.println("| Session ID:    " + sessionId + "  |");
            System.out.println("--------------------------------\n");

            new ReadThread(socket, this).start();
            new WriteThread(socket, this).start();


        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
        }

    }

    void setUserName(String userName) {
        this.userName = userName;
    }

    String getUserName() {
        return this.userName;
    }


    public static void main(String[] args) {
        if (args.length < 2) return;

        String hostname = args[0];
        int port = Integer.parseInt(args[1]);

        ChatClient client = new ChatClient(hostname, port);
        client.execute();
    }
}
