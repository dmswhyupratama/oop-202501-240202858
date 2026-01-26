package com.upb.agripos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List; 

import com.upb.agripos.config.DatabaseConnection;
import com.upb.agripos.model.Product;

public class ProductDAOImpl implements ProductDAO {

    // HAPUS constructor yang menyimpan connection ke field
    // private final Connection connection; <-- HAPUS INI

    public ProductDAOImpl() {
        // Kosongkan constructor, jangan simpan connection di sini
    }

    @Override
    public void insert(Product p) throws Exception {
        String sql = "INSERT INTO products(code, name, price, stock) VALUES (?, ?, ?, ?)";
        
        // PERBAIKAN: Ambil koneksi DI DALAM method
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, p.getCode());
                ps.setString(2, p.getName());
                ps.setDouble(3, p.getPrice());
                ps.setInt(4, p.getStock());
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Product findByCode(String code) throws Exception {
        String sql = "SELECT * FROM products WHERE code = ?";
        
        // PERBAIKAN: Ambil koneksi DI DALAM method
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, code);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new Product(
                            rs.getString("code"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            rs.getInt("stock")
                        );
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return null;
    }

    @Override
    public List<Product> findAll() throws Exception {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products ORDER BY code ASC"; 
        
        // PERBAIKAN: Ambil koneksi DI DALAM method
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Product(
                        rs.getString("code"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return list;
    }

    @Override
    public void update(Product p) throws Exception {
        String sql = "UPDATE products SET name=?, price=?, stock=? WHERE code=?";
        
        // PERBAIKAN: Ambil koneksi DI DALAM method
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, p.getName());
                ps.setDouble(2, p.getPrice());
                ps.setInt(3, p.getStock());
                ps.setString(4, p.getCode());
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void delete(String code) throws Exception {
        String sql = "DELETE FROM products WHERE code=?";
        
        // PERBAIKAN: Ambil koneksi DI DALAM method
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, code);
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}