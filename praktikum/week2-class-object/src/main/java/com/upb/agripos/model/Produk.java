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

     public String getKode() { return kode; }
    public void setKode(String kode) { this.kode = kode; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public double getHarga() { return harga; }
    public void setHarga(double harga) { this.harga = harga; }

    public int getStok() { return stok; }
    public void setStok(int stok) { this.stok = stok; }

    public void tambahStok(int jumlah) {
        this.stok += jumlah;
    }

    public boolean kurangiStok(int jumlah) {
        if (jumlah <= 0) return false;
        if (this.stok >= jumlah) {
            this.stok -= jumlah;
            return true;
        } else {
            System.out.println("Stok tidak mencukupi!");
            return false;
        }
    }

    // Method untuk Menampilkan Perubahan Stok(tambah/kurang)
    public void ubahStok(int jumlah) {
        int stokAwal = this.stok;
        if (jumlah > 0) {
            tambahStok(jumlah);
            System.out.println(nama + " => +" + jumlah + " (dari " + stokAwal + " menjadi " + stok + ")");
        } else {
            if (kurangiStok(-jumlah)) {
                System.out.println(nama + " => " + jumlah + " (dari " + stokAwal + " menjadi " + stok + ")");
            }
        }
    }

    // Method Untuk Mengubah Format harga
    public String formatHarga() {
        // Gunakan String.format untuk tambahkan pemisah ribuan
        return String.format("Rp %, .0f", this.harga).replace(',', '.');
    }

    // Method Untuk Menampilkan Rincian Kode, Nama Produk, harga, dan Stok
    public void tampilkanInfo() {
        System.out.println("Kode: " + kode + ", Nama: " + nama + ", Harga: " + formatHarga() + ", Stok: " + stok);
    }

    
}