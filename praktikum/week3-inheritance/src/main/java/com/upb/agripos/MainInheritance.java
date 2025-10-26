package main.java.com.upb.agripos;

import main.java.com.upb.agripos.model.*;
import main.java.com.upb.agripos.util.CreditBy;

public class MainInheritance {
    public static void main(String[] args) {
        Benih b = new Benih("BNH-001", "Benih Padi IR64", 25000, 100, "IR64");
        Pupuk p = new Pupuk("PPK-101", "Pupuk Urea", 350000, 40, "Urea");
        AlatPertanian a = new AlatPertanian("ALT-501", "Cangkul Baja", 90000, 15, "Baja");

        System.out.println("\n                        Deskripsi Produk                      ");
        System.out.println("----------------------------------------------------------------");
        System.out.printf("%-10s %-17s %-12s %-6s %s %n" ,"Kode", "Nama", "Harga", "Stok", "Keterangan");
        System.out.println("----------------------------------------------------------------");
        b.deskripsi();
        p.deskripsi();
        a.deskripsi();
        System.out.println("----------------------------------------------------------------");

        CreditBy.print("240202858", "Dimas Wahyu Pratama");
    }
}