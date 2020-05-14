/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ChatApp;

/**
 *
 * @author ASUS
 */
public class Soal {
    private int nomor;
    private String soal;
    
    public Soal(int nomor, String soal) {
        this.nomor = nomor;
        this.soal = soal;
    }
    
    public int getNomor () {
        return this.nomor;
    }

    public String getSoal() {
        return this.soal;
    }
}
