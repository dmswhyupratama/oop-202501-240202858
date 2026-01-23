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
            return List.of(); // Kembalikan list kosong jika error
        }
    }

    // --- TAMBAHAN PENTING UNTUK WEEK 13 (HAPUS DATA) ---
    public void delete(String code) {
        try {
            productDAO.delete(code);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Gagal menghapus produk: " + e.getMessage());
        }
    }
}