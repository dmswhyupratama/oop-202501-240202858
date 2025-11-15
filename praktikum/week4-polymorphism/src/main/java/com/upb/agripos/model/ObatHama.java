package main.java.com.upb.agripos.model;

public class ObatHama extends Produk {
    private String targetHama;

    public ObatHama(String kode, String nama, double harga, int stok, String targetHama) {
        // Memanggil constructor superclass (Produk)
        super(kode, nama, harga, stok);
        this.targetHama = targetHama;
    }

    // Method Overriding
    @Override
    public String getInfo() {
        // Memanggil getInfo() dari superclass dan menambahkan info spesifik
        return "Obat Hama: " + super.getInfo() + ", Target Hama: " + targetHama;
    }
}