/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ChatApp;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author ASUS
 */
public class UjianSiswa {

    private String namaSiswa;
    private HashMap<Integer, String> jawabanSiswa = new HashMap<Integer, String>();
    private HashMap<Integer, Integer> nilai = new HashMap<Integer, Integer>();

    public UjianSiswa(String namaSiswa, HashMap<Integer, String> soals) {
        for (HashMap.Entry<Integer, String> entry : soals.entrySet()) {
            this.jawabanSiswa.put(entry.getKey(), "kosong");
            this.nilai.put(entry.getKey(), 0);
        }
    }

    public void jawabSoal(Integer nomor, String jawaban) {
        this.jawabanSiswa.put(nomor, jawaban);
    }

    public void nilaiSoal(Integer nomor, int nilai) {
        this.nilai.put(nomor, nilai);
    }
    
    public String getJawaban(int i)
    {
        return this.jawabanSiswa.get(i);
    }
    
    public int getNilai(int i)
    {
        return this.nilai.get(i);
    }
}
