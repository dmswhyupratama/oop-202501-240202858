package com.upb.agripos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.upb.agripos.config.DatabaseConnection;

public class ReturnDAO {

    public void processReturn(String trxId, String productCode, int qty, String reason) throws Exception {
        Connection conn = null;
        PreparedStatement psInsert = null;
        PreparedStatement psUpdateStock = null;

        String sqlInsert = "INSERT INTO returns (transaction_id, product_code, quantity, reason) VALUES (?, ?, ?, ?)";
        String sqlStock = "UPDATE products SET stock = stock + ? WHERE code = ?"; // Perhatikan tanda '+'

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Transaksi Database

            // 1. Simpan Log Retur
            psInsert = conn.prepareStatement(sqlInsert);
            psInsert.setString(1, trxId);
            psInsert.setString(2, productCode);
            psInsert.setInt(3, qty);
            psInsert.setString(4, reason);
            psInsert.executeUpdate();

            // 2. Kembalikan Stok (Tambah Stok)
            psUpdateStock = conn.prepareStatement(sqlStock);
            psUpdateStock.setInt(1, qty);
            psUpdateStock.setString(2, productCode);
            psUpdateStock.executeUpdate();

            conn.commit(); // Simpan Perubahan

        } catch (Exception e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) {}
            throw new Exception("Gagal memproses retur: " + e.getMessage());
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ex) {}
            if (psInsert != null) psInsert.close();
            if (psUpdateStock != null) psUpdateStock.close();
        }
    }
}