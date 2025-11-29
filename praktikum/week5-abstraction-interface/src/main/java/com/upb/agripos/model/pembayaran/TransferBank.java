package main.java.com.upb.agripos.model.pembayaran;

import main.java.com.upb.agripos.model.kontrak.Validatable;
import main.java.com.upb.agripos.model.kontrak.Receiptable;

// Latihan Mandiri: TransferBank
// implements Validatable (untuk cek mutasi) dan Receiptable (untuk struk)
public class TransferBank extends Pembayaran implements Validatable, Receiptable {
    
    private String noRekening;
    private boolean sudahTransfer; // Simulasi pengecekan mutasi

    public TransferBank(String invoiceNo, double total, String noRekening, boolean sudahTransfer) {
        super(invoiceNo, total);
        this.noRekening = noRekening;
        this.sudahTransfer = sudahTransfer;
    }

    @Override
    public double biaya() {
        // Biaya admin tetap
        return 3500.0;
    }

    @Override
    public boolean validasi() {
        // Validasi: Cek apakah sistem sudah mendeteksi transfer masuk
        return sudahTransfer;
    }

    @Override
    public boolean prosesPembayaran() {
        // Proses berhasil jika validasi (cek mutasi) berhasil
        return validasi();
    }

    @Override
    public String cetakStruk() {
        return String.format("--- STRUK PEMBAYARAN TRANSFER BANK ---\n" +
                             "INVOICE   : %s\n" +
                             "NO REK    : %s\n" +
                             "TOTAL     : Rp %,.2f\n" +
                             "ADMIN FEE : Rp %,.2f\n" +
                             "TOTAL BAYAR: Rp %,.2f\n" +
                             "STATUS    : %s\n",
                             invoiceNo, noRekening, total, biaya(), totalBayar(),
                             prosesPembayaran() ? "BERHASIL" : "GAGAL (Transfer Belum Diterima)");
    }
}