package main.java.com.upb.agripos.model.pembayaran;

import main.java.com.upb.agripos.model.kontrak.Receiptable;

public class Cash extends Pembayaran implements Receiptable {
    private double tunai;

    public Cash(String invoiceNo, double total, double tunai) {
        super(invoiceNo, total);
        this.tunai = tunai;
    }

    @Override
    public double biaya() {
        return 0.0;
    }

    @Override
    public boolean prosesPembayaran() {
        return tunai >= totalBayar(); // sederhana: cukup uang tunai
    }

    @Override
    public String cetakStruk() {
        // Menghitung kembalian, pastikan tidak negatif
        double kembali = Math.max(0, tunai - totalBayar());
        
        return String.format("--- STRUK PEMBAYARAN CASH ---\n" +
                             "INVOICE   : %s\n" +
                             "TOTAL     : Rp %,.2f\n" +
                             "TUNAI     : Rp %,.2f\n" +
                             "KEMBALI   : Rp %,.2f\n" +
                             "STATUS    : %s\n",
                             invoiceNo, totalBayar(), tunai, kembali, 
                             prosesPembayaran() ? "BERHASIL" : "GAGAL (Uang Kurang)");
    }
}