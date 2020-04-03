/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ChatApp;
import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 *
 * @author waswib
 */
public class cekIP {

public static void main(String[] args)
{
    try
    {
        InetAddress address =InetAddress.getLocalHost();
        System.out.println(address);
        InetAddress address2 =InetAddress.getByName("www.its.ac.id");
         System.out.println(address2);
    }
    catch (Exception e)
    {
        System.out.println("Could not find local address!");
    }


}

}
