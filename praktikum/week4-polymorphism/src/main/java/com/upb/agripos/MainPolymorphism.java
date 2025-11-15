package main.java.com.upb.agripos;

import main.java.com.upb.agripos.model.*;
import main.java.com.upb.agripos.util.CreditBy;

public class MainPolymorphism {
    public static void main(String[] args) {
        
        System.out.println("--- Demonstrasi Dynamic Binding (Overriding) ---");
        
        Produk[] daftarProduk = {
            new Benih("BNH-001", "Benih Padi IR64", 25000, 100, "IR64"),
            new Pupuk("PPK-101", "Pupuk Urea", 350000, 40, "Urea"),
            new AlatPertanian("ALT-501", "Cangkul Baja", 90000, 15, "Baja"),
            new ObatHama("HMA-202", "Insektisida Regent", 75000, 50, "Wereng Coklat")
        };

        for (Produk p : daftarProduk) {
            System.out.println(p.getInfo()); // Dynamic binding 
        }
        
        System.out.println("\n--- Demonstrasi Overloading ---");
        Produk produkPadi = daftarProduk[0];
        produkPadi.tambahStok(10);
        produkPadi.tambahStok(5.5);
        System.out.println("---------------------------------");

        CreditBy.print("240202858", "Dimas Wahyu Pratama"); 
    }
}