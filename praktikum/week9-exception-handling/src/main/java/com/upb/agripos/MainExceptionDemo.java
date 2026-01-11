package main.java.com.upb.agripos;

import main.java.com.upb.agripos.util.CreditBy;

public class MainExceptionDemo {
    public static void main(String[] args) {
        
        ShoppingCart cart = new ShoppingCart();
        // Stok awal cuma 3
        Product p1 = new Product("P01", "Pupuk Organik", 25000, 3);

        System.out.println("\n[Skenario 1] Menambah produk dengan jumlah negatif");
        try {
            cart.addProduct(p1, -1);
        } catch (InvalidQuantityException e) {
            System.out.println("Kesalahan tertangkap: " + e.getMessage());
        }

        System.out.println("\n[Skenario 2] Menghapus produk yang belum ada");
        try {
            cart.removeProduct(p1);
        } catch (ProductNotFoundException e) {
            System.out.println("Kesalahan tertangkap: " + e.getMessage());
        }

        System.out.println("\n[Skenario 3] Checkout melebihi stok");
        try {
            cart.addProduct(p1, 5); // Minta 5, padahal stok 3
            cart.checkout();
        } catch (Exception e) {
            System.out.println("Kesalahan tertangkap: " + e.getMessage());
        }
    
    CreditBy.print("240202858", "Dimas Wahyu Pratama");
    }
}