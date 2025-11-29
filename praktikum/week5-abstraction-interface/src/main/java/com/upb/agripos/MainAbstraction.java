package main.java.com.upb.agripos;

import main.java.com.upb.agripos.model.kontrak.*;
import main.java.com.upb.agripos.model.pembayaran.*; 
import main.java.com.upb.agripos.util.CreditBy;

public class MainAbstraction {
    public static void main(String[] args) {
        
        // Buat array polimorfik bertipe Pembayaran
        Pembayaran[] daftarPembayaran = {
            // Kasus 1: Cash berhasil (tunai > total)
            new Cash("INV-001", 100000, 100000),
            
            // Kasus 2: EWallet berhasil (OTP 6 digit)
            new EWallet("INV-002", 150000, "08123456789 (OVO)", "123456"),
            
            // Kasus 3: Transfer Bank (Latihan Mandiri) berhasil
            new TransferBank("INV-003", 200000, "BCA-112233", true), // true = sudah transfer
            
            // Kasus 4: EWallet gagal (OTP salah)
            new EWallet("INV-004", 50000, "user@gopay", "123"), // OTP hanya 3 digit
            
            // Kasus 5: Cash gagal (uang kurang)
            new Cash("INV-005", 75000, 70000) // Bayar 70rb untuk total 75rb
        };

        System.out.println("--- MEMPROSES SEMUA PEMBAYARAN ---");

        // Loop dan proses
        for (Pembayaran p : daftarPembayaran) {
            // Kita harus melakukan casting ke (Receiptable)
            // karena method cetakStruk() BUKAN bagian dari abstract class Pembayaran,
            // tapi bagian dari interface Receiptable.
            if (p instanceof Receiptable) {
                Receiptable r = (Receiptable) p;
                System.out.println(r.cetakStruk());
            } else {
                System.out.println("Metode pembayaran " + p.getInvoiceNo() + " tidak bisa cetak struk.");
            }
        }

        System.out.println("---------------------------------");
        // GANTI DENGAN NIM DAN NAMA ANDA
        CreditBy.print("240202858", "Dimas Wahyu Pratama");
    }
}