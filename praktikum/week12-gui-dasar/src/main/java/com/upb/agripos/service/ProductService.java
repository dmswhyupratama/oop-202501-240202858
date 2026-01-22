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
            // SOLUSI ERROR 1: Bungkus dengan try-catch
            productDAO.insert(product); 
        } catch (Exception e) {
            e.printStackTrace(); // Tampilkan error di console jika gagal
            // Opsional: Lempar ulang sebagai RuntimeException biar Controller tahu
            throw new RuntimeException("Gagal menyimpan produk: " + e.getMessage());
        }
    }

    public List<Product> getAllProducts() {
        try {
            // SOLUSI ERROR 2: Ganti .getAll() jadi .findAll() (sesuaikan dengan nama di DAO kamu)
            // Cek file ProductDAO.java, apa nama method untuk ambil semua data?
            // Biasanya findAll() atau getProducts()
            return productDAO.findAll(); 
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // Kembalikan list kosong jika error
        }
    }
}