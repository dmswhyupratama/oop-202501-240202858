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

    // Method untuk Menampung parameter Keterangan seperti(Material,varietas,Jenis)
    public String getKeterangan() {
        return "";
    }

    public String formatHarga() {
        return String.format("Rp %,.0f", this.harga).replace(',', '.');
    }

    public void deskripsi() {
        System.out.printf("%-10s %-17s %-12s %-6s %s %n", getKode(), getNama(), formatHarga(), getStok(), getKeterangan());
    }

}