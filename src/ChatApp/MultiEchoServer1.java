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

public class MultiEchoServer1 {

    private ServerSocket serverSocket;
    private static final int PORT = 4444;

    private static MultiEchoServer1 myServer;


    public void start() throws IOException{
        
        try
        {
            serverSocket = new ServerSocket(PORT);
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
            //Create a thread to handle communication with
            //this client and pass the constructor for this
            //thread a reference to the relevant socket...
            ClientHandler handler =  new ClientHandler(client);
            handler.start();//As usual, method calls run.
         }while (true);

    }

    public static void main(String[] args) 
    {
           myServer = new MultiEchoServer1();
           try {
                myServer.start();
           }
           catch (Exception e){
               
           }
     }


    class ClientHandler extends Thread
    {
        private Socket client;
        private java.util.Scanner input;
        private PrintWriter output;


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
                output.println("ECHO: " + received);
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
