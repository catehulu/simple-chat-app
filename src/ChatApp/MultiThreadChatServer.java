/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ChatApp;

/**
 *
 * @author waswib
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Timer;

/**
 *
 * @author ayya
 */
public class MultiThreadChatServer {

    // The server socket.
    private static ServerSocket serverSocket = null;
    // The client socket.
    private static Socket clientSocket = null;

    // This chat server can accept up to maxClientsCount clients' connections.
    private static final int maxClientsCount = 10;
    private static final clientThread[] threads = new clientThread[maxClientsCount]; ///ada di kelas utama
    private static final List<String> nama = new ArrayList<String>();
    private static final HashMap<String, User> userAccount = new HashMap<String, User>();
    private static final HashMap<String, HashMap<Integer, String>> ujianUser = new HashMap<String, HashMap<Integer, String>>();
    private static final HashMap<String, HashMap<String, UjianSiswa>> ujianSiswas = new HashMap<String, HashMap<String, UjianSiswa>>();

    public static void main(String args[]) {

        userAccount.put("Siraj", new User("Siraj", "kelincilucu", "guru"));
        userAccount.put("Tyo", new User("Tyo", "dangkotenak", "siswa"));

        ujianUser.put("Siraj", new HashMap<Integer, String>());
        int latestNomor;
        latestNomor = ujianUser.get("Siraj").size();
        ujianUser.get("Siraj").put(latestNomor + 1, "Apa gambar dibawah ini ?<br><img src=\"https://images.pexels.com/photos/730896/pexels-photo-730896.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940\">");
        ujianUser.get("Siraj").put(latestNomor + 2, "Apa yang disebut dengan harimau ?");

        ujianSiswas.put("Siraj", new HashMap<String, UjianSiswa>());
        ujianSiswas.get("Siraj").put("Tyo", new UjianSiswa("Tyo", ujianUser.get("Siraj")));

        //Cara jawab
        ujianSiswas.get("Siraj").get("Tyo").jawabSoal(1, "Jawaban saya");

        //Cara ambil semua soal
        //ujianUser.get("Siraj")
        //Cara iterate semua soal, pakai for aja
        /*
        
//        for (i=1, i<=ujianUser.get("Siraj").size(), i++ {
//            soal = ujianUser.get("Siraj").get(i);
//        }
        
         */
        // The default port number.
        int portNumber = 2222;
        if (args.length < 1) {
            System.out
                    .println("Usage: java MultiThreadChatServer <portNumber>\n"
                            + "Now using port number=" + portNumber);
        } else {
            portNumber = Integer.valueOf(args[0]).intValue();
        }

        /*
     * Open a server socket on the portNumber (default 2222). Note that we can
     * not choose a port less than 1023 if we are not privileged users (root).
         */
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            System.out.println(e);
        }

        /*
     * Create a client socket for each connection and pass it to a new client
     * thread.
         */
        while (true) {
            try {
                clientSocket = serverSocket.accept(); /// true bila ada koneksi yang bloacking
                int i = 0;
                for (i = 0; i < maxClientsCount; i++) {
                    if (threads[i] == null) {
                        //(threads[i] = new clientThread(clientSocket, threads)).start();
                        threads[i] = new clientThread(clientSocket, threads, nama, userAccount, ujianUser, ujianSiswas); // obyek array of thread di object induk juga dilempar ke objeck anak (client thread)
                        threads[i].start();

                        break;
                    }
                }
                if (i == maxClientsCount) {
                    PrintStream os = new PrintStream(clientSocket.getOutputStream());
                    os.println("Server too busy. Try later.");
                    os.close();
                    clientSocket.close();
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}

/*
 * The chat client thread. This client thread opens the input and the output
 * streams for a particular client, ask the client's name, informs all the
 * clients connected to the server about the fact that a new client has joined
 * the chat room, and as long as it receive data, echos that data back to all
 * other clients. When a client leaves the chat room this thread informs also
 * all the clients about that and terminates.
 */
class clientThread extends Thread {

    private DataInputStream is = null;
    private PrintStream os = null;
    private Socket clientSocket = null;
    private final clientThread[] threads;
    private int maxClientsCount;
    private final List<String> nama;
    private final HashMap<String, User> userAccount;
    private final HashMap<String, HashMap<Integer, String>> ujianUser;
    private final HashMap<String, HashMap<String, UjianSiswa>> ujianSiswas;
    private int isUjian = 0;
    private int currentNumber = 0;
    private Timer ujianTimer;

    public clientThread(Socket clientSocket, clientThread[] threads, List<String> nama, HashMap<String, User> userAccount,
            HashMap<String, HashMap<Integer, String>> ujianUser, HashMap<String, HashMap<String, UjianSiswa>> ujianSiswas) {
        this.clientSocket = clientSocket;
        this.threads = threads;
        maxClientsCount = threads.length;
        this.nama = nama;
        this.userAccount = userAccount;
        this.ujianUser = ujianUser;
        this.ujianSiswas = ujianSiswas;
    }

    public void run() {
        int maxClientsCount = this.maxClientsCount;
        clientThread[] threads = this.threads;

        try {
            /*
       * Create input and output streams for this client.
             */
            is = new DataInputStream(clientSocket.getInputStream()); //bi-directional link
            os = new PrintStream(clientSocket.getOutputStream());
            os.println("Enter your name.");
            String name = is.readLine().trim();
            os.println("Enter your password.");
            String password = is.readLine().trim();
            if (userAccount.containsKey(name)) {
                if (userAccount.get(name).matchPassword(password)) {
                    this.nama.add(name);

                    os.println("Hello " + name
                            + " to our chat room.\nTo leave enter /quit in a new line");

                    //   String nama = "#ulil#apa kabar ulil" ;
                    //          String datanya[] = nama.split("#");
//                    for (int i = 0; i < maxClientsCount; i++) {
//                        if (threads[i] != null && threads[i] != this) {
//                            threads[i].os.println("* A new user " + name
//                                    + " entered the chat room !!! *");
//                        }
//                    }
                    if (userAccount.get(name).getRole().equals("siswa")) {
                        os.println("Command usage :");
                        os.println("ujian#[nama_guru] : Mendaftarkan diri ke ujian oleh guru");
                        os.println("jawab#[nama_guru] : Memulai ujian guru yang dipilih");
                        os.println("nilai#[nama_guru] : Melihat nilai ujian yang telah diinputkan guru");
                    } else {
                        os.println("Command usage :");
                        os.println("tambah#[soal] : Menambah soal");
                        os.println("nilai#[nama_siswa]#[nomor]#[nilai] : Menilai jawaban siswa");
                        os.println("jawab#[nama_siswa] : Melihat jawaban siswa");
                        os.println("list : Melihat jawaban siswa");
                    }
                    OUTER:
                    while (!Thread.interrupted()) {
                        if (userAccount.get(name).getRole().equals("siswa")) {
                            String line = is.readLine();
                            String[] messages = line.split("#");
                            switch (messages[0]) {
                                case "jawab": {
                                    String nameGuru = messages[1];
                                    os.println("Selamat Datang pada Ujian, selamat mengerjakan");
                                    os.println("Dalam 5 detik ujian akan dimulai");
                                    os.println("Silahkan menjawab soal, persoal 60 detik");
                                    os.println("Jawaban akan tersimpan setiap soal");
                                    String answer;
                                    this.isUjian = 1;
                                    this.ujianTimer = new Timer();
                                    os.println("system#timer_on");
                                    UjianTimer tt = new UjianTimer(this, os, ujianUser.get(nameGuru), nameGuru);
                                    this.ujianTimer.schedule(tt, 5000, 30000);
                                    System.out.println("ujiantimer run");
                                    while (this.isUjian == 1) {
                                        answer = is.readLine();
                                        ujianSiswas.get(nameGuru).get(name).jawabSoal(currentNumber, answer);
                                        os.println("Jawaban anda :" + answer);
                                    }
                                    this.ujianTimer.cancel();
                                    os.println("system#timer_off");
                                    os.println("Ujian telah selesai");
                                    os.println("Jawaban anda yang masuk : ");
                                    for (int i = 1; i <= ujianUser.get(nameGuru).size(); i++) {
                                        os.println(i + ". " + ujianSiswas.get(nameGuru).get(name).getJawaban(i));
                                    }
                                    break;
                                }
                                case "nilai": {
                                    String nameGuru = messages[1];
                                    os.println("Nilai anda untuk ujian oleh " + nameGuru);
                                    for (int i = 1; i <= ujianUser.get(nameGuru).size(); i++) {
                                        os.println(i + ". " + ujianSiswas.get(nameGuru).get(name).getNilai(i));
                                    }
                                    break;
                                }
                                case "ujian": {
                                    String nameGuru = messages[1];
                                    ujianSiswas.get(nameGuru).put(name, new UjianSiswa(name, ujianUser.get(nameGuru)));
                                    os.println("Anda terdaftar ikut ujian oleh " + nameGuru);
                                }
                            }
                        } else {
                            String line = is.readLine();
                            String[] messages = line.split("#");
                            switch (messages[0]) {
                                case "tambah": {
                                    String soal = messages[1];
                                    break;
                                }
                                case "nilai": {
                                    String nameSiswa = messages[1];
                                    int nomor = Integer.parseInt(messages[2]);
                                    int nilai = Integer.parseInt(messages[3]);
                                    ujianSiswas.get(name).get(nameSiswa).nilaiSoal(nomor, nilai);
                                    os.println("Nilai " + nameSiswa + " berhasil di update");
                                    break;
                                }
                                case "jawab": {
                                    String nameSiswa = messages[1];
                                    os.println("Jawaban untuk siswa " + nameSiswa + ":");
                                    for (int i = 1; i <= ujianUser.get(name).size(); i++) {
                                        os.println(i + ". " + ujianSiswas.get(name).get(nameSiswa).getJawaban(i));
                                    }
                                    break;
                                }
                                case "list": {
                                    os.println("Siswa yang terdaftar dalam ujian :");
                                    os.println("---");
                                    for (HashMap.Entry<String, UjianSiswa> entry : ujianSiswas.get(name).entrySet()) {
//                                        UjianSiswa ujianSiswa = entry.getValue();
                                        String namaSiswa = entry.getKey();
                                        os.println("->" + namaSiswa);
                                    }
                                    os.println("---");
                                    break;
                                }
                            }

//                            String line = is.readLine();
//                            switch (line.charAt(0)) {
//                                case '?': {
//                                    String[] messages = line.split("#");
//                                    String namaUser = messages[0].substring(1, messages[0].length());
//                                    if (namaUser.compareTo("kick") == 0) {
//                                        namaUser = messages[1];
//                                        for (int i = 0; i < maxClientsCount; i++) {
//                                            if (threads[i] != null && nama.get(i).equalsIgnoreCase(namaUser)) {
//                                                threads[i].interrupt();
//                                                threads[i] = null;
//
//                                            }
//                                        }
//                                        break;
//
//                                    } else {
//
//                                        String pesan = messages[1];
//                                        for (int i = 0; i < maxClientsCount; i++) {
//                                            if (threads[i] != null && nama.get(i).equalsIgnoreCase(namaUser)) {
//                                                threads[i].os.println("<" + name + "--> " + pesan);
//                                            }
//                                        }
//                                        break;
//                                    }
//                                }
//                                case '!': {
//                                    String[] messages = line.split("#");
//                                    String namaUser = messages[0].substring(1, messages[0].length());
//                                    String pesan = messages[1];
//                                    for (int i = 0; i < maxClientsCount; i++) {
//                                        if (threads[i] != null && (nama.get(i).toLowerCase().compareTo(namaUser.toLowerCase()) != 0)
//                                                && (name.compareTo(nama.get(i)) != 0)) {
//                                            threads[i].os.println("<" + name + "--> " + pesan);
//                                        }
//                                    }
//                                    break;
//                                }
//                                default:
//                                    if (line.startsWith("/quit")) {
//                                        break OUTER;
//                                    }
//                                    for (int i = 0; i < maxClientsCount; i++) {
//                                        if (threads[i] != null) {
//                                            threads[i].os.println("[" + name + "] " + line);
//                                        }
//                                    }
//                                    break;
//                            }
                        }

                    }

                    for (int i = 0; i < maxClientsCount; i++) {
                        if (threads[i] != null && threads[i] != this) {
                            threads[i].os.println("* The user " + name
                                    + " is leaving the chat room !!! *");
                        }
                    }
                    os.println("* Bye " + name + " *");
                } else {
                    os.println("Password salah !");
                }
            } else {
                os.println("Akun tidak ada!");
            }

            /*
       * Clean up. Set the current thread variable to null so that a new client
       * could be accepted by the server.
             */
            for (int i = 0; i < maxClientsCount; i++) {
                if (threads[i] == this) {
                    threads[i] = null;
                }
            }

            /*
       * Close the output stream, close the input stream, close the socket.
             */
            is.close();
            os.close();
            clientSocket.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public void setCurrentNumber(int currentNumber) {
        this.currentNumber = currentNumber;
    }

    public void setIsUjian(int isUjian) {
        this.isUjian = isUjian;
    }
}
