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

public class TCPEchoClient {

    private InetAddress host;
    private int PORT;
    private FormEchoClient serverGUI;
    PrintWriter output;
    Scanner input;
    String message="";
    String userID;
    String Ip;


public TCPEchoClient(){

}
public TCPEchoClient( String Ip, String port, FormEchoClient Gui , String userID){

    this.Ip = Ip;
    this.PORT =  Integer.parseInt(port);
    this.serverGUI= Gui;
    this.userID = userID;
    this.serverGUI.writeToLog("Starting communication with server ");


}

public void Start(){

         try
        {
            host = InetAddress.getByName(this.Ip);
        }
        catch(UnknownHostException uhEx)
        {
            System.out.println("Host ID not found!");
            System.exit(1);
       }
        accessServer();

}

public void kirim (String pesan ) {

    output.println(pesan);
}

public void tulisLog (String pesan){
    this.serverGUI.writeToLog(pesan);
}

private void accessServer()
{
    Socket link = null; //Step 1.
    try
    {
        link = new Socket(host,PORT); //Step 1.
        input =  new Scanner(link.getInputStream()); //Step 2.
        output =   new PrintWriter(link.getOutputStream(),true); //Step 2.
        //output.println("Connectec to Server with ID " + this.userID);
         chatHandler handler = new chatHandler(this.input,this.output,this);
         handler.start();
    }
    catch(IOException ioEx)
    {
        ioEx.printStackTrace();
    }
    finally
    {
  
    }
 }

  class chatHandler extends Thread {

    PrintWriter myOutput;
    Scanner myInput;
    TCPEchoClient myClient;

    public chatHandler (Scanner input, PrintWriter output,TCPEchoClient client ) {
        this.myOutput = output;
        this.myInput = input;
        this.myClient = client;
    }

    public void run(){
       //String receive;

        String  response;
        response = this.myInput.nextLine(); //Step 3.
        this.myOutput.println(this.myClient.userID);
        do
        {
           response = this.myInput.nextLine(); //Step 3.

           //this.myClient.serverGUI.writeToLog
           this.myClient.serverGUI.writeToLog(response);
        }while (!message.equals("q!"));



    }
  }

}
