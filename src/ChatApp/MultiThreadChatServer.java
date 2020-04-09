/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ChatApp;

/**
 *
 * @author waswib
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

/**
 *
 * @author ayya
 */
public class MultiThreadChatServer {

    // The server socket.
    private static ServerSocket serverSocket = null;
    // The client socket.
    private static Socket clientSocket = null;

    // This chat server can accept up to maxClientsCount clients' connections.
    private static final int maxClientsCount = 10;
    private static final clientThread[] threads = new clientThread[maxClientsCount]; ///ada di kelas utama
    private static final List<String> nama = new ArrayList<String>();
    private static final HashMap<String, String> userAccount = new HashMap<String, String>();

    public static void main(String args[]) {

        userAccount.put("Siraj", "kelinciimut");
        userAccount.put("Tyo", "dangkotenak");

        // The default port number.
        int portNumber = 2222;
        if (args.length < 1) {
            System.out
                    .println("Usage: java MultiThreadChatServer <portNumber>\n"
                            + "Now using port number=" + portNumber);
        } else {
            portNumber = Integer.valueOf(args[0]).intValue();
        }

        /*
     * Open a server socket on the portNumber (default 2222). Note that we can
     * not choose a port less than 1023 if we are not privileged users (root).
         */
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            System.out.println(e);
        }

        /*
     * Create a client socket for each connection and pass it to a new client
     * thread.
         */
        while (true) {
            try {
                clientSocket = serverSocket.accept(); /// true bila ada koneksi yang bloacking
                int i = 0;
                for (i = 0; i < maxClientsCount; i++) {
                    if (threads[i] == null) {
                        //(threads[i] = new clientThread(clientSocket, threads)).start();
                        threads[i] = new clientThread(clientSocket, threads, nama, userAccount); // obyek array of thread di object induk juga dilempar ke objeck anak (client thread)
                        threads[i].start();

                        break;
                    }
                }
                if (i == maxClientsCount) {
                    PrintStream os = new PrintStream(clientSocket.getOutputStream());
                    os.println("Server too busy. Try later.");
                    os.close();
                    clientSocket.close();
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}

/*
 * The chat client thread. This client thread opens the input and the output
 * streams for a particular client, ask the client's name, informs all the
 * clients connected to the server about the fact that a new client has joined
 * the chat room, and as long as it receive data, echos that data back to all
 * other clients. When a client leaves the chat room this thread informs also
 * all the clients about that and terminates.
 */
class clientThread extends Thread {

    private DataInputStream is = null;
    private PrintStream os = null;
    private Socket clientSocket = null;
    private final clientThread[] threads;
    private int maxClientsCount;
    private final List<String> nama;
    private final HashMap<String, String> userAccount;

    public clientThread(Socket clientSocket, clientThread[] threads, List<String> nama, HashMap<String, String> userAccount) {
        this.clientSocket = clientSocket;
        this.threads = threads;
        maxClientsCount = threads.length;
        this.nama = nama;
        this.userAccount = userAccount;
    }

    public void run() {
        int maxClientsCount = this.maxClientsCount;
        clientThread[] threads = this.threads;

        try {
            /*
       * Create input and output streams for this client.
             */
            is = new DataInputStream(clientSocket.getInputStream()); //bi-directional link
            os = new PrintStream(clientSocket.getOutputStream());
            os.println("Enter your name.");
            String name = is.readLine().trim();
            os.println("Enter your password.");
            String password = is.readLine().trim();
            if (userAccount.containsKey(name)) {
                if (userAccount.get(name).compareTo(password) == 0) {
                    this.nama.add(name);

                    os.println("Hello " + name
                            + " to our chat room.\nTo leave enter /quit in a new line");

                    //   String nama = "#ulil#apa kabar ulil" ;
                    //          String datanya[] = nama.split("#");
                    for (int i = 0; i < maxClientsCount; i++) {
                        if (threads[i] != null && threads[i] != this) {
                            threads[i].os.println("* A new user " + name
                                    + " entered the chat room !!! *");
                        }
                    }

                    OUTER:
                    while (!Thread.interrupted()) {
                        String line = is.readLine();
                        switch (line.charAt(0)) {
                            case '?': {
                                String[] messages = line.split("#");
                                String namaUser = messages[0].substring(1, messages[0].length());
                                if (namaUser.compareTo("kick") == 0) {
                                    namaUser = messages[1];
                                    for (int i = 0; i < maxClientsCount; i++) {
                                        if (threads[i] != null && nama.get(i).equalsIgnoreCase(namaUser)) {
                                            threads[i].interrupt();
                                            threads[i] = null;

                                        }
                                    }
                                    break;

                                } else {

                                    String pesan = messages[1];
                                    for (int i = 0; i < maxClientsCount; i++) {
                                        if (threads[i] != null && nama.get(i).equalsIgnoreCase(namaUser)) {
                                            threads[i].os.println("<" + name + "--> " + pesan);
                                        }
                                    }
                                    break;
                                }
                            }
                            case '!': {
                                String[] messages = line.split("#");
                                String namaUser = messages[0].substring(1, messages[0].length());
                                String pesan = messages[1];
                                for (int i = 0; i < maxClientsCount; i++) {
                                    if (threads[i] != null && (nama.get(i).toLowerCase().compareTo(namaUser.toLowerCase()) != 0)
                                            && (name.compareTo(nama.get(i)) != 0)) {
                                        threads[i].os.println("<" + name + "--> " + pesan);
                                    }
                                }
                                break;
                            }
                            default:
                                if (line.startsWith("/quit")) {
                                    break OUTER;
                                }
                                for (int i = 0; i < maxClientsCount; i++) {
                                    if (threads[i] != null) {
                                        threads[i].os.println("<" + name + "--> " + line);
                                    }
                                }
                                break;
                        }
                    }

                    for (int i = 0; i < maxClientsCount; i++) {
                        if (threads[i] != null && threads[i] != this) {
                            threads[i].os.println("* The user " + name
                                    + " is leaving the chat room !!! *");
                        }
                    }
                    os.println("* Bye " + name + " *");
                } else {
                    os.println("Password salah !");
                }
            } else {
                 os.println("Akun tidak ada!");
            }

            /*
       * Clean up. Set the current thread variable to null so that a new client
       * could be accepted by the server.
             */
            for (int i = 0; i < maxClientsCount; i++) {
                if (threads[i] == this) {
                    threads[i] = null;
                }
            }

            /*
       * Close the output stream, close the input stream, close the socket.
             */
            is.close();
            os.close();
            clientSocket.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
