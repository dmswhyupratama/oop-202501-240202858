package main.java.com.upb.agripos;

import main.java.com.upb.agripos.model.*;
import main.java.com.upb.agripos.util.*;

public class MainProduk {
    public static void main(String[] args) {

        Produk p1 = new Produk("BNH-001", "Benih Padi IR64", 25000, 100);
        Produk p2 = new Produk("PPK-101", "Pupuk Urea 50kg", 350000, 40);
        Produk p3 = new Produk("ALT-501", "Cangkul Baja", 90000, 15);

        // Menampilkan Data Awal produk
        System.out.println("======================== DATA AWAL PRODUK ========================");
        p1.tampilkanInfo();
        p2.tampilkanInfo();
        p3.tampilkanInfo();
        System.out.println("==================================================================");
        
        // Menampilkan Rincian Perubahan yaitu penambahan dan Pengurangan Stok Produk
        System.out.println("\n=========== RINCIAN PERUBAHAN STOK ==========");
        p1.ubahStok(50);
        p2.ubahStok(-10);
        p3.ubahStok(15);
        System.out.println("=============================================");

        // Data Final Setelah Stok Barang Dirubah
        System.out.println("\n  DATA SETELAH STOK DITAMBAH/DIKURANG  ");
        System.out.println("-----------------------------------------------------");
        System.out.printf("%-10s %-20s %-15s %-10s%n", "Kode", "Nama", "Harga", "Stok");
        System.out.println("-----------------------------------------------------");
        System.out.printf("%-10s %-20s %-15s %-10d%n", p1.getKode(), p1.getNama(), p1.formatHarga(), p1.getStok());
        System.out.printf("%-10s %-20s %-15s %-10d%n", p2.getKode(), p2.getNama(), p2.formatHarga(), p2.getStok());
        System.out.printf("%-10s %-20s %-15s %-10d%n", p3.getKode(), p3.getNama(), p3.formatHarga(), p3.getStok());

        // Tampilkan identitas mahasiswa
        CreditBy.print("240202858", "Dimas Wahyu Pratama");
    }

}