package com.upb.agripos;

import java.util.List;
import java.util.Scanner;

import com.upb.agripos.dao.ProductDAO;
import com.upb.agripos.dao.ProductDAOImpl;
import com.upb.agripos.model.Product;

public class MainDAOTest {
    private static final Scanner scanner = new Scanner(System.in);
    
    private static final ProductDAO dao = new ProductDAOImpl();

    public static void main(String[] args) {
        int pilihan = 0;
        
        while (pilihan != 5) {
            System.out.println("\n=== APLIKASI AGRIPOS (JDBC DAO) ===");
            System.out.println("1. Tampilkan Semua Produk");
            System.out.println("2. Tambah Produk Baru");
            System.out.println("3. Cari Produk per Kode");
            System.out.println("4. Hapus Produk");
            System.out.println("5. Keluar");
            System.out.print("Pilih menu: ");
            
            try {
                String input = scanner.nextLine();
                // Validasi input kosong
                if (input.isEmpty()) continue;
                
                pilihan = Integer.parseInt(input);
                
                switch (pilihan) {
                    case 1 -> showAllProducts();
                    case 2 -> insertProduct();
                    case 3 -> findProduct();
                    case 4 -> deleteProduct();
                    case 5 -> System.out.println("Terima kasih! Menutup aplikasi...");
                    default -> System.out.println("Pilihan tidak valid.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Input harus angka!");
            } catch (Exception e) {
                System.out.println("Terjadi Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static void showAllProducts() throws Exception {
        List<Product> list = dao.findAll();
        System.out.println("\n--- DAFTAR PRODUK ---");
        System.out.printf("%-10s %-30s %-15s %-10s\n", "KODE", "NAMA", "HARGA", "STOK");
        System.out.println("-----------------------------------------------------------------");
        
        if (list.isEmpty()) {
            System.out.println("DATA KOSONG.");
        } else {
            for (Product p : list) {
                System.out.printf("%-10s %-30s Rp%,-12.0f %-10d\n", 
                    p.getCode(), p.getName(), p.getPrice(), p.getStock());
            }
        }
    }

    private static void insertProduct() throws Exception {
        System.out.println("\n--- TAMBAH DATA ---");
        System.out.print("Kode Produk : ");
        String code = scanner.nextLine();
        
        // Cek dulu apakah kode sudah ada (Validasi sederhana)
        if (dao.findByCode(code) != null) {
            System.out.println("GAGAL: Kode produk " + code + " sudah ada!");
            return;
        }

        System.out.print("Nama Produk : ");
        String name = scanner.nextLine();
        System.out.print("Harga       : ");
        double price = Double.parseDouble(scanner.nextLine());
        System.out.print("Stok        : ");
        int stock = Integer.parseInt(scanner.nextLine());

        Product p = new Product(code, name, price, stock);
        dao.insert(p);
        System.out.println(">> Sukses menambah data!");
    }

    private static void findProduct() throws Exception {
        System.out.print("\nMasukkan Kode Produk: ");
        String code = scanner.nextLine();
        Product p = dao.findByCode(code);
        
        if (p != null) {
            System.out.println("Ditemukan: " + p.getName() + " | Harga: Rp" + p.getPrice());
        } else {
            System.out.println("Produk tidak ditemukan.");
        }
    }

    private static void deleteProduct() throws Exception {
        System.out.print("\nMasukkan Kode Produk yang akan DIHAPUS: ");
        String code = scanner.nextLine();
        
        if (dao.findByCode(code) == null) {
            System.out.println("Data tidak ditemukan, gagal hapus.");
        } else {
            dao.delete(code);
            System.out.println(">> Data berhasil dihapus.");
        }
    }
}