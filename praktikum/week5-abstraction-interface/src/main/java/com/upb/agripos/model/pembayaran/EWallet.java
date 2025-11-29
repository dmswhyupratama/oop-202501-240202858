package main.java.com.upb.agripos.model.pembayaran;

import main.java.com.upb.agripos.model.kontrak.Receiptable;
import main.java.com.upb.agripos.model.kontrak.Validatable;


public class EWallet extends Pembayaran implements Validatable, Receiptable {
    private String akun;
    private String otp; // sederhana untuk simulasi

    public EWallet(String invoiceNo, double total, String akun, String otp) {
        super(invoiceNo, total);
        this.akun = akun;
        this.otp = otp;
    }

    @Override
    public double biaya() {
        return total * 0.015; // 1.5% fee
    }

    @Override
    public boolean validasi() {
        return otp != null && otp.length() == 6; // contoh validasi sederhana
    }

    @Override
    public boolean prosesPembayaran() {
        return validasi(); // jika validasi lolos, anggap berhasil
    }

    @Override
    public String cetakStruk() {
        return String.format("--- STRUK PEMBAYARAN E-WALLET ---\n" +
                             "INVOICE   : %s\n" +
                             "AKUN      : %s\n" +
                             "TOTAL     : Rp %,.2f\n" +
                             "FEE       : Rp %,.2f\n" +
                             "TOTAL BAYAR: Rp %,.2f\n" +
                             "STATUS    : %s\n",
                             invoiceNo, akun, total, biaya(), totalBayar(),
                             prosesPembayaran() ? "BERHASIL" : "GAGAL (OTP Salah)");
    }
}