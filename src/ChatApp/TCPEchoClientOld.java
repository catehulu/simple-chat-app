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
import java.util.*;

import javax.swing.*;

public class TCPEchoClientOld {

    private static InetAddress host;
    private int PORT;
    private JTextArea myLog;
public static void main(String[] args)
{
   
 }


public TCPEchoClientOld( String port, JTextArea serverLog ){

    this.PORT =  Integer.parseInt(port);
    this.myLog = serverLog;


}

public void Start(){

         try
        {
            host = InetAddress.getLocalHost();
        }
        catch(UnknownHostException uhEx)
        {
            System.out.println("Host ID not found!");
            System.exit(1);
       }
        accessServer();

}

public void kirim (String pesan ) {


}


private void accessServer()
{
    Socket link = null; //Step 1.
    try
    {
        link = new Socket(host,PORT); //Step 1.
        Scanner input =  new Scanner(link.getInputStream()); //Step 2.
        PrintWriter output =   new PrintWriter(
        link.getOutputStream(),true); //Step 2.
        //Set up stream for keyboard entry...
        Scanner userEntry = new Scanner(System.in);
        String message, response;
        do
        {
            System.out.print("Enter message: ");
            message = userEntry.nextLine();
            output.println(message); //Step 3.
            response = input.nextLine(); //Step 3.


            //System.out.println("[Server] "+response);
            this.myLog.append("[Server] "+response);


        }while (!message.equals("***CLOSE***"));
    }
    catch(IOException ioEx)
    {
        ioEx.printStackTrace();
    }
    finally
    {
        try
        {
            System.out.println(
            "\n* Closing connection... *");
            link.close(); //Step 4.
        }
    catch(IOException ioEx)
        {
            System.out.println("Unable to disconnect!");
            System.exit(1);
        }
    }
 }



}
