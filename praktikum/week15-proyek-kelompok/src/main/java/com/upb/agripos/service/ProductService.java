package com.upb.agripos.service;

import java.util.List;

import com.upb.agripos.dao.ProductDAO;
import com.upb.agripos.model.Product;

public class ProductService {
    private ProductDAO productDAO;

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public void insert(Product product) {
        try {
            productDAO.insert(product); 
        } catch (Exception e) {
            e.printStackTrace(); 
            throw new RuntimeException("Gagal menyimpan produk: " + e.getMessage());
        }
    }

    public List<Product> getAllProducts() {
        try {
            return productDAO.findAll(); 
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public void delete(String code) {
        try {
            productDAO.delete(code);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Gagal menghapus produk: " + e.getMessage());
        }
    }

    // --- TAMBAHAN BARU UNTUK FITUR EDIT ---
    public void update(Product product) {
        try {
            productDAO.update(product);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Gagal mengupdate produk: " + e.getMessage());
        }
    }
    
    public Product findByCode(String code) {
        // Cara simpel: Ambil semua, lalu filter stream
        // (Idealnya pakai query SELECT ... WHERE code = ?, tapi ini cukup untuk tugas)
        return getAllProducts().stream()
                .filter(p -> p.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElse(null);
    }

    // TAMBAHAN BARU: Cari Produk berdasarkan Nama (Partial Match / Mirip)
    public java.util.List<Product> findProductsByName(String keyword) {
        return getAllProducts().stream()
                .filter(p -> p.getName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(java.util.stream.Collectors.toList());
    }
}