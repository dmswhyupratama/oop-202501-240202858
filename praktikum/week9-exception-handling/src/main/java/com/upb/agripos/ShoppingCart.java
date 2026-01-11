package main.java.com.upb.agripos;

import java.util.HashMap;
import java.util.Map;

public class ShoppingCart {
    private final Map<Product, Integer> items = new HashMap<>();

    public void addProduct(Product p, int qty) throws InvalidQuantityException {
        if (qty <= 0) {
            throw new InvalidQuantityException("Quantity harus lebih dari 0.");
        }
        items.put(p, items.getOrDefault(p, 0) + qty);
        System.out.println("Berhasil menambahkan: " + p.getName() + " (" + qty + ")");
    }

    public void removeProduct(Product p) throws ProductNotFoundException {
        if (!items.containsKey(p)) {
            throw new ProductNotFoundException("Produk tidak ada dalam keranjang.");
        }
        items.remove(p);
        System.out.println("Berhasil menghapus: " + p.getName());
    }

    public void checkout() throws InsufficientStockException {
        System.out.println("\n--- Proses Checkout ---");
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            Product product = entry.getKey();
            int qty = entry.getValue();
            if (product.getStock() < qty) {
                throw new InsufficientStockException(
                    "Stok tidak cukup untuk: " + product.getName() + " (Stok: " + product.getStock() + ", Minta: " + qty + ")"
                );
            }
        }
        // Kurangi stok jika semua aman
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            entry.getKey().reduceStock(entry.getValue());
            System.out.println("Checkout sukses: " + entry.getKey().getName());
        }
    }
}