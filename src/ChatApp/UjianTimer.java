/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ChatApp;

import java.io.PrintStream;
import java.util.Date;
import java.util.HashMap;
import java.util.TimerTask;

/**
 *
 * @author ASUS
 */
public class UjianTimer extends TimerTask {

    clientThread clientThread;
    HashMap<Integer, String> soalUjian = new HashMap<Integer, String>();
    String guruUjian;
    int nomorSoal;
    int jumlahSoal;
    PrintStream os;

    public UjianTimer(clientThread clientThread, PrintStream os, HashMap<Integer, String> soalUjian, String guruUjian) {
        this.clientThread = clientThread;
        this.soalUjian = soalUjian;
        this.guruUjian = guruUjian;
        this.nomorSoal = 0;
        this.os = os;
        this.jumlahSoal = soalUjian.size();
//        System.out.println(clientThread);
//        System.out.println(soalUjian);
//        System.out.println(guruUjian);
//        System.out.println(nomorSoal);
//        System.out.println(os);
//        System.out.println(jumlahSoal);

    }

    public void run() {
        System.out.println("run for soal"+this.nomorSoal);
        if (jumlahSoal < nomorSoal) {
            this.clientThread.setIsUjian(0);
        } else {
            this.nomorSoal++;
            os.println("Soal nomor" + this.nomorSoal);
            os.println(soalUjian.get(nomorSoal));
            this.clientThread.setCurrentNumber(nomorSoal);
        }
    }

}
