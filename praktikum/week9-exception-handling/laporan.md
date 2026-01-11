# Laporan Praktikum Minggu 9
Topik: Exception Handling, Custom Exception, dan Penerapan Design Pattern

## Identitas
- Nama  : Dimas Wahyu Pratama
- NIM   : 240202858
- Kelas : 3IKRA

---

## Tujuan
1.  Mahasiswa mampu menjelaskan perbedaan antara error dan exception.
2.  Mahasiswa mampu mengimplementasikan blok `try-catch-finally` dengan tepat.
3.  Mahasiswa mampu membuat *custom exception* untuk menangani logika bisnis yang spesifik.
4.  Mahasiswa mampu mengintegrasikan penanganan error ke dalam sistem Agri-POS.

---

## Dasar Teori
1.  **Error vs Exception**: Error adalah kondisi fatal yang biasanya tidak bisa ditangani oleh program (contoh: `OutOfMemoryError`), sedangkan Exception adalah kondisi tidak normal yang masih bisa ditangkap dan ditangani agar program tidak berhenti mendadak.
2.  **Try-Catch-Finally**: Struktur blok kode untuk menangani error. `try` berisi kode yang berpotensi error, `catch` menangkap error tersebut, dan `finally` adalah blok yang selalu dieksekusi baik terjadi error maupun tidak.
3.  **Custom Exception**: Exception yang dibuat sendiri oleh programmer dengan cara mewarisi class `Exception`. Ini berguna untuk menandai kesalahan logika bisnis yang spesifik, seperti "Stok Habis" atau "Jumlah Beli Negatif".

---

## Langkah Praktikum
1.  **Membuat Custom Exception**: Membuat tiga class exception baru (`InvalidQuantityException`, `ProductNotFoundException`, `InsufficientStockException`) yang mewarisi class `Exception`.
2.  **Update Model Product**: Menambahkan atribut `stock` pada class `Product` untuk simulasi pengecekan ketersediaan barang.
3.  **Implementasi Logic Keranjang**: Memperbarui `ShoppingCart` agar melempar (*throw*) exception ketika terjadi kesalahan validasi (misal: input minus atau stok kurang).
4.  **Uji Coba (Main Program)**: Membuat class `MainExceptionDemo` yang menggunakan blok `try-catch` untuk menangkap error yang dilempar oleh keranjang belanja.

---

## Kode Program
```java
// InsufficientStockException.java
package main.java.com.upb.agripos;

public class InsufficientStockException extends Exception {
    public InsufficientStockException(String msg) {
        super(msg);
    }
}
```
```java
// InvalidQuantityException.java
package main.java.com.upb.agripos;

public class InvalidQuantityException extends Exception {
    public InvalidQuantityException(String msg) {
        super(msg);
    }
}
```
```java
// Product.java
package main.java.com.upb.agripos;

public class Product {
    private final String code;
    private final String name;
    private final double price;
    private int stock;

    public Product(String code, String name, double price, int stock) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    
    public void reduceStock(int qty) {
        this.stock -= qty;
    }
}
```
```java
// ProductNotFoundException.java
package main.java.com.upb.agripos;

public class ProductNotFoundException extends Exception {
    public ProductNotFoundException(String msg) {
        super(msg);
    }
}
```
```java
// ShoppingCart.java
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
```
```java
// MainExceptionDemo.java
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
```
---

## Hasil Eksekusi  
![Screenshot hasil](screenshots/hasil.png)
---

## Analisis

1.  **Mekanisme Penanganan Error**
    Program menggunakan kata kunci `throws` pada method di `ShoppingCart` untuk memberi sinyal bahwa method tersebut berpotensi gagal. Di sisi lain, `MainExceptionDemo` menggunakan `try-catch` untuk "menangkap" kegagalan tersebut.

2.  **Keuntungan Custom Exception**
    Dengan membuat `InvalidQuantityException` dan lainnya, pesan error menjadi lebih jelas dan spesifik sesuai bahasa manusia/bisnis, bukan sekadar `NullPointerException` atau error umum lainnya.

3.  **Alur Program**
    Ketika `cart.checkout()` dipanggil dan stok barang kurang dari permintaan, program langsung melompat ke blok `catch` yang sesuai, sehingga kode pengurangan stok di baris bawahnya tidak dieksekusi. Ini menjaga integritas data stok agar tidak minus.

---

## Kesimpulan

Praktikum ini menunjukkan bahwa *Exception Handling* sangat krusial untuk membuat aplikasi yang tangguh (*robust*). Dengan memisahkan logika utama (*happy path*) dan penanganan error (*sad path*) menggunakan `try-catch`, kode menjadi lebih rapi. Penggunaan *Custom Exception* juga memudahkan debugging karena jenis kesalahannya didefinisikan secara eksplisit sesuai kebutuhan sistem Agri-POS.

---

## Quiz

**1. Jelaskan perbedaan error dan exception.**
* **Error:** Masalah serius di level sistem/JVM yang sulit dipulihkan (contoh: memori penuh/`OutOfMemoryError`). Aplikasi biasanya harus mati.
* **Exception:** Masalah yang muncul saat runtime tapi bisa ditangani (di-*catch*) oleh program sehingga aplikasi bisa tetap berjalan (contoh: file tidak ketemu, input salah).

**2. Apa fungsi finally dalam blok try–catch–finally?**
Blok `finally` berfungsi untuk menjalankan kode yang **wajib dieksekusi** apa pun yang terjadi (baik ada error maupun sukses). Biasanya digunakan untuk bersih-bersih resource, seperti menutup koneksi database atau menutup file.

**3. Mengapa custom exception diperlukan?**
Diperlukan untuk menangani kasus spesifik yang tidak dicakup oleh exception standar Java. Contohnya, Java tidak tahu bahwa "Jumlah Beli -5" itu salah secara logika bisnis toko. Kita butuh `InvalidQuantityException` untuk menegakkan aturan tersebut.

**4. Berikan contoh kasus bisnis dalam POS yang membutuhkan custom exception.**
* **Stok Habis:** `InsufficientStockException` saat user checkout barang lebih dari yang tersedia.
* **Barang Kadaluarsa:** `ExpiredProductException` saat kasir scan barang basi.
* **Limit Transaksi:** `TransactionLimitExceededException` jika belanjaan melebihi batas maksimum per struk.