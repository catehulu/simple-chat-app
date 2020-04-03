/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ChatApp;

import java.net.*;
import java.util.*;

/**
 *
 * @author waswib
 */
public class IPFinder {

    public static void main(String[] args)
    {
        String host;
        Scanner input = new Scanner(System.in);  // deklarasi variable Scanner
        System.out.print("\n\nEnter host name: "); // mengeluarkan tampilan
        host = input.next(); //

       // System.out.println(" input data : " + host);

        try
        {
            InetAddress address =  InetAddress.getByName(host);
            System.out.println("IP address: "  + address.toString());
        }
        catch (UnknownHostException uhEx)
        {
            System.out.println("Could not find " + host);
        }


    }
}
