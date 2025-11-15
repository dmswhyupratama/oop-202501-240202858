package main.java.com.upb.agripos.model;

public class Benih extends Produk {
    private String varietas;

    public Benih(String kode, String nama, double harga, int stok, String varietas) {
        super(kode, nama, harga, stok); // Memanggil constructor superclass
        this.varietas = varietas;
    }

    // Method Overriding
    @Override
    public String getInfo() {
        // Memanggil getInfo() dari superclass dan menambahkan info spesifik
        return "Benih: " + super.getInfo() + ", Varietas: " + varietas;
    }
}