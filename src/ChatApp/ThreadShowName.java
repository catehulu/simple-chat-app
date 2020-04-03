/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ChatApp;

/**
 *
 * @author waswib
 */
public class ThreadShowName extends Thread {

 public static void main (String[] args)
{
    ThreadShowName thread1, thread2, thread3;
    thread1 = new ThreadShowName();
    thread2 = new ThreadShowName();
    thread3 = new ThreadShowName();
    thread1.setName(" Manggis ");
    thread2.setName("Alpukat");
    thread3.setName(" Jambu  ");

    thread1.start(); //Will call run.
    thread2.start(); //Will call run.
    thread3.start();
}


    public void run(){

    int pause;
    for (int i=0; i<10; i++)
    {
        try
        {
            System.out.println(this.getName() +" dijalankan  ke : " + i);
            pause = (int)(Math.random()*3000);
            sleep(pause); //0-3 seconds.
        }
        catch (InterruptedException interruptEx)
        {
            System.out.println(interruptEx);
        }
       }
    }

}



