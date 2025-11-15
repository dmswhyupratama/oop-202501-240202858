package main.java.com.upb.agripos.model;

public class Produk {
    private String kode;
    private String nama;
    private double harga;
    private int stok;

    public Produk(String kode, String nama, double harga, int stok) {
        this.kode = kode;
        this.nama = nama;
        this.harga = harga;
        this.stok = stok;
    }

    // Overloading: Menerima parameter integer
    public void tambahStok(int jumlah) {
        this.stok += jumlah;
        System.out.println("Stok " + this.nama + " ditambah " + jumlah + ". Stok baru: " + this.stok);
    }

    // Overloading: Menerima parameter double
    public void tambahStok(double jumlah) {
        // Dikonversi ke int jika perlu
        this.stok += (int) jumlah;
        System.out.println("Stok " + this.nama + " ditambah " + (int)jumlah + ". Stok baru: " + this.stok);
    }

    // Method ini akan dioverride oleh subclass
    public String getInfo() {
        return "Produk: " + nama + " (Kode: " + kode + ")";
    }

}