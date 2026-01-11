package main.java.com.upb.agripos;

import main.java.com.upb.agripos.util.CreditBy;

public class MainCart {
    public static void main(String[] args) {

        // 1. Buat Objek Produk
        Product p1 = new Product("P01", "Beras 5kg", 65000);
        Product p2 = new Product("P02", "Pupuk Urea", 30000);
        Product p3 = new Product("P03", "Bibit Jagung", 45000);

        // 2. Buat Keranjang Belanja
        ShoppingCart cart = new ShoppingCart();

        // 3. Tambah Produk
        System.out.println("\n[Aksi] Menambahkan produk...");
        cart.addProduct(p1);
        cart.addProduct(p2);
        cart.addProduct(p3);
        cart.printCart();

        // 4. Hapus Produk
        System.out.println("\n[Aksi] Menghapus produk (Beras)...");
        cart.removeProduct(p1);
        cart.printCart();

        System.out.println("---------------------------------");
        CreditBy.print("240202858", "Dimas Wahyu Pratama");
    }
}
