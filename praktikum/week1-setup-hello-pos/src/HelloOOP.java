import java.util.Scanner;

class Mahasiswa{

    // Deklarasi Variable
    String nama, nim;

    // Membuat Constructor untuk menginisialisasi sebagai object
    Mahasiswa(String nama, String nim){
    this.nama = nama;
    this.nim = nim;
    }

    // Method yang digunakan untuk melakukan print sapa'an yang dapat dipanggil secara berulang(lebih dari 1)
    public void sapa(){
        System.out.println("Hello World I'm "+ nama + " -"+ nim);
    }

}

public class HelloOOP {
    
    public static void main(String[] args) {
        //membuat variable scanner input
        Scanner userInput = new Scanner(System.in);

        // Meminta Input Dari User
        System.out.print("Masukkan Nama Mahasiswa = ");
        String nama = userInput.nextLine();
        System.out.print("Masukkan NIM Mahasiswa = ");
        String nim = userInput.nextLine();

        // Mengambil Object dari scaner input
        Mahasiswa nm1 =  new Mahasiswa (nama,nim);

        // Membuat Object secara manual
        Mahasiswa nm2 =  new Mahasiswa ("Fahmi","240202859");
        Mahasiswa nm3 =  new Mahasiswa ("Ridho","240202860");

        // Call method sapa
        nm1.sapa();
        nm2.sapa();
        nm3.sapa();

        System.out.println("Program OOP Selesai Dijalankan, Terimakasih");

    }

}
