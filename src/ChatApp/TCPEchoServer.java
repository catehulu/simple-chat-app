/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ChatApp;


 import java.net.*;
 import java.io.*;
 import java.util.Scanner;
/**
 *
 * @author waswib
 */
public class TCPEchoServer {



    private static ServerSocket servSock;
    private static final int PORT = 4444;
    String pengguna;




    public TCPEchoServer(){

        System.out.println(" Creating TCP Server Object");
    }


    public TCPEchoServer(String namaPengguna){

        this.pengguna = namaPengguna;

        System.out.println(" Creating TCP Server Object");
    }


    public void start(){

        System.out.println("Opening port...\n");
        try
        {
            servSock = new ServerSocket(PORT); //Step 1.
        }
        catch(IOException ioEx)
        {
            System.out.println("Unable to attach to port!");
            System.exit(1);
        }
        do {
            handleClient();
        } while(true);
    }

    private void handleClient(){

        Socket link = null; //Step 2.
        try
       {
            link = servSock.accept(); //Step 2.
            Scanner inputDariJaringan = new Scanner(link.getInputStream());//Step 3.
            PrintWriter outputKeJaringan =   new PrintWriter(link.getOutputStream(),true); //Step 3.

         //   outputKeJaringan.println("Welcome to chat server ");

            int numMessages = 0;
            String message = inputDariJaringan.nextLine(); //Step 4.
            while (!message.equals("!q"))
            {
               //  System.out.println("Message received.");
                System.out.println(" inbox msg  : " + message);
                 numMessages++;
                outputKeJaringan.println(pengguna + ": " + message); //Step 4.
                message = inputDariJaringan.nextLine();
            }
            outputKeJaringan.println(numMessages   + " messages received.");//Step 4.
        }
        catch(IOException ioEx){
             //ioEx.printStackTrace();
        }
        finally
        {
            try {
                   System.out.println( "\n* Closing connection... *");
                link.close(); //Step 5.
            }
            catch(IOException ioEx)
            {
                System.out.println("Unable to disconnect!");
                System.exit(1);
            }
         }
    } //end handleclient
} // end class
