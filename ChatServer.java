import java.io.*;
import java.net.*;
import java.util.*;

/**
 * This is the chat server program.
 * Press Ctrl + C to terminate the program.
 */

public class ChatServer {
    private int port;
    private Set<String> userNames = new HashSet<>();
    private Set<UserThread> userThreads = new HashSet<>();
    private String sessionId;

    public ChatServer(int port) {
        this.port = port;
        this.sessionId = genRandString(12);
    }

    public void execute() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            
            System.out.println("Chat Server is listening on Port: " + port + "\n");
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New user connected");

                UserThread newUser = new UserThread(socket, this);
                userThreads.add(newUser);
                newUser.start();

            }

        } catch (IOException ex) {
            System.out.println("Error in the server: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Syntax: java ChatServer <port-number>");
            System.exit(0);
        }

        int port = Integer.parseInt(args[0]);
        /*String Servername = String*/
        int strlength = 5;
        String RandomString = genRandString(strlength);
        
        
        System.out.println("\nWelcome! to the ChatServer\n");
        System.out.println("--------------------------------");
        System.out.println("| Server Name:  ChatSrv-" + RandomString+"  |");
        System.out.println("--------------------------------");
        
        ChatServer server = new ChatServer(port);

        System.out.println("| Session ID:    " + server.getSessionId() + "  |");
        System.out.println("--------------------------------\n");

        server.execute();
    }

    /**
     * Delivers a message from one user to others (broadcasting)
     */
    void broadcast(String message, UserThread excludeUser) {
        for (UserThread aUser : userThreads) {
            if (aUser != excludeUser) {
                aUser.sendMessage(message);
            }
        }
    }

    /**
     * Stores username of the newly connected client.
     */
    void addUserName(String userName) {
        userNames.add(userName);
    }

    /**
     * When a client is disconneted, removes the associated username and UserThread
     */
    void removeUser(String userName, UserThread aUser) {
        boolean removed = userNames.remove(userName);
        if (removed) {
            userThreads.remove(aUser);
            System.out.println("The user " + userName + " has left the chat");
        }
    }

    Set<String> getUserNames() {
        return this.userNames;
    }

    /**
     * Returns true if there are other users connected (not count the currently connected user)
     */
    boolean hasUsers() {
        return !this.userNames.isEmpty();
    }

    /**
     * Random String Function
     */
    public static String genRandString(int length) {

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        StringBuilder randomStringBuilder = new StringBuilder();

        // Random object
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            randomStringBuilder.append(randomChar);
        }
        return randomStringBuilder.toString();
    }
    String getSessionId(){
        return this.sessionId;
    }
}