/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ChatApp;

/**
 *
 * @author waswib
 */

import java.net.*;
import java.io.*;

public class MultiEchoServer {

    private ServerSocket serverSocket;
    private static final int PORT = 4444;

    private static MultiEchoServer myServer;


    public void start() throws IOException{
        
        try
        {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Startng the Server");
        }
        catch (IOException ioEx)
        {
            System.out.println("\nUnable to set up port!");
            System.exit(1);
         }

        do
        {
            //Wait for client...
            Socket client = serverSocket.accept();
            System.out.println("\nNew client accepted.\n");
            ClientHandler handler =  new ClientHandler(client);
            handler.start();//As usual, method calls run.


         }while (true);

    }

    public static void main(String[] args) throws IOException
    {
           myServer = new MultiEchoServer();
           myServer.start();

     }


    class ClientHandler extends Thread
    {
        private Socket client;
        private java.util.Scanner input;
        private PrintWriter output;
        private String userID;


        public ClientHandler(Socket socket)
        {
        //Set up reference to associated socket...
            client = socket;
            try
            {
                input = new java.util.Scanner(client.getInputStream());
                output = new PrintWriter(client.getOutputStream(),true);
            }
            catch(IOException ioEx)
            {
                ioEx.printStackTrace();
            }
        }

        public ClientHandler(Socket socket, String userId)
        {
        //Set up reference to associated socket...
            client = socket;
            this.userID = userId;
            try
            {
                input = new java.util.Scanner(client.getInputStream());
                output = new PrintWriter(client.getOutputStream(),true);
            }
            catch(IOException ioEx)
            {
                ioEx.printStackTrace();
            }
        }

        public void kirim (String message){

            this.output.println(message);

        }

        public void run()
            {
            String received;
            do
            {
            //Accept message from client on
            //the socket's input stream...
                 received = input.nextLine();
            //Echo message back to client on
            //the socket's output stream...
                output.println("ECHO dari Server : " + received);
            //Repeat above until 'QUIT' sent by client...
            }while (!received.equals("QUIT"));
            try
            {
                if (client!=null)
                {
                    System.out.println(
                    "Closing down connection...");
                    client.close();
                }
            }
            catch(IOException ioEx)
            {
                System.out.println("Unable to disconnect!");
            }
         }
    }
}
