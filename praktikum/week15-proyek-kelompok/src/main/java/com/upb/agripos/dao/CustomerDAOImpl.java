package com.upb.agripos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.upb.agripos.config.DatabaseConnection;
import com.upb.agripos.model.Customer;

public class CustomerDAOImpl implements CustomerDAO {

    @Override
    public void save(Customer c) throws Exception {
        // Query Insert lengkap sesuai kolom database
        String sql = "INSERT INTO customers (name, phone, is_member, member_code, points) VALUES (?, ?, ?, ?, ?)";
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, c.getName());
                ps.setString(2, c.getPhone());
                ps.setBoolean(3, c.isMember());      // Ambil status member (True/False)
                ps.setString(4, c.getMemberCode());  // Kode Member (Misal: MEM-001)
                ps.setInt(5, c.getPoints());         // Poin awal
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<Customer> findAll() throws Exception {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM customers ORDER BY id DESC"; // Yang baru daftar paling atas
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToCustomer(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return list;
    }

    @Override
    public Customer findByMemberCode(String code) throws Exception {
        String sql = "SELECT * FROM customers WHERE member_code = ?";
        return getSingleCustomer(sql, code);
    }
    
    @Override
    public Customer findByPhone(String phone) throws Exception {
        String sql = "SELECT * FROM customers WHERE phone = ?";
        return getSingleCustomer(sql, phone);
    }

    // Helper method untuk query single result biar kode lebih rapi
    private Customer getSingleCustomer(String sql, String param) throws Exception {
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, param);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return mapResultSetToCustomer(rs);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return null; // Return null kalau tidak ketemu
    }
    
    // Mapper dari Database ke Java Object (PENTING: Urutan & Nama Kolom harus pas)
    private Customer mapResultSetToCustomer(ResultSet rs) throws Exception {
        return new Customer(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("phone"),
            rs.getBoolean("is_member"),   // Kolom boolean
            rs.getString("member_code"),  // Kolom String kode unik
            rs.getInt("points")           // Kolom Integer poin
        );
    }

    @Override
    public void update(Customer c) throws Exception {
        String sql = "UPDATE customers SET name=?, phone=? WHERE member_code=?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, c.getName());
                ps.setString(2, c.getPhone());
                ps.setString(3, c.getMemberCode());
                ps.executeUpdate();
            }
        } catch (Exception e) { e.printStackTrace(); throw e; }
    }

    @Override
    public void delete(String memberCode) throws Exception {
        String sql = "DELETE FROM customers WHERE member_code=?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, memberCode);
                ps.executeUpdate();
            }
        } catch (Exception e) { e.printStackTrace(); throw e; }
    }
}