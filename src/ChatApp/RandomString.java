/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ChatApp;

/**
 *
 * @author waswib
 */

import java.util.Random;
public class RandomString extends Thread {

 public static void main (String[] args)
{
    RandomString thread1;
    RandomString thread2, thread3;

  //  String[] toppings = {"Cheese", "Pepperoni", "Black Olives"};
    thread1 = new RandomString();
    thread2 = new RandomString();
    thread3 = new RandomString();
 //   thread1.setName("satu ");
  //  thread2.setName("dua");
   // thread3.setName(" tiga  ");

    thread1.start(); //Will call run.
    thread2.start(); //Will call run.
    thread3.start();
}


    public void run(){

    int pause;
    String[] dataString ={"mangga","pisang","jambu","Cheese", "Pepperoni"};
    Random rand = new Random();
    for (int i=0; i<5; i++)
    {
        try
        {

            int x = rand.nextInt(5);
            System.out.println(i +". Thread ke "+ this.getName()+ " index : " + x + " : " + dataString[x]);
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



