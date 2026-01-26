package com.upb.agripos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.upb.agripos.config.DatabaseConnection;
import com.upb.agripos.model.CartItem;
import com.upb.agripos.model.Product;
import com.upb.agripos.model.Transaction;

public class TransactionDAOImpl implements TransactionDAO {

    @Override
    public List<Transaction> findAll() {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT t.id, t.date, t.total_amount, t.payment_method, " +
                     "u.fullname AS cashier, c.name AS customer " +
                     "FROM transactions t " +
                     "LEFT JOIN users u ON t.user_id = u.id " +
                     "LEFT JOIN customers c ON t.customer_id = c.id " +
                     "ORDER BY t.date DESC";

        // PERBAIKAN 1: Connection diambil DI LUAR try-with-resources agar tidak auto-close
        try {
            Connection conn = DatabaseConnection.getConnection();
            
            // Hanya PreparedStatement & ResultSet yang boleh di dalam try(...)
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");

                while (rs.next()) {
                    String dateStr = sdf.format(rs.getTimestamp("date"));
                    String custName = rs.getString("customer");
                    if (custName == null) custName = "Umum";

                    list.add(new Transaction(
                        rs.getString("id"),
                        dateStr,
                        rs.getDouble("total_amount"),
                        rs.getString("payment_method"),
                        rs.getString("cashier"),
                        custName
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void save(Transaction trx, List<CartItem> items) throws Exception {
        Connection conn = null;
        PreparedStatement psHeader = null;
        PreparedStatement psDetail = null;
        PreparedStatement psUpdateStock = null;

        String sqlHeader = "INSERT INTO transactions (id, total_amount, payment_method, user_id, customer_id, discount) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlDetail = "INSERT INTO transaction_details (transaction_id, product_code, quantity, subtotal) VALUES (?, ?, ?, ?)";
        String sqlStock = "UPDATE products SET stock = stock - ? WHERE code = ?";

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Mulai Transaksi Database

            // 1. Simpan Header
            psHeader = conn.prepareStatement(sqlHeader);
            psHeader.setString(1, trx.getId());
            psHeader.setDouble(2, trx.getTotalAmount());
            psHeader.setString(3, trx.getPaymentMethod());
            psHeader.setInt(4, trx.getUserId());
            
            if (trx.getCustomerId() == 0) {
                psHeader.setObject(5, null);
            } else {
                psHeader.setInt(5, trx.getCustomerId());
            }
            
            // Diskon (Pastikan Model Transaction sudah update)
            psHeader.setDouble(6, trx.getDiscount());
            
            psHeader.executeUpdate();

            // 2. Simpan Detail & Update Stok
            psDetail = conn.prepareStatement(sqlDetail);
            psUpdateStock = conn.prepareStatement(sqlStock);

            for (CartItem item : items) {
                // Detail
                psDetail.setString(1, trx.getId());
                psDetail.setString(2, item.getProduct().getCode());
                psDetail.setInt(3, item.getQuantity());
                psDetail.setDouble(4, item.getSubtotal());
                psDetail.addBatch();

                // Stok
                psUpdateStock.setInt(1, item.getQuantity());
                psUpdateStock.setString(2, item.getProduct().getCode());
                psUpdateStock.addBatch();
            }

            psDetail.executeBatch();
            psUpdateStock.executeBatch();

            conn.commit(); // Simpan Permanen

        } catch (Exception e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) {}
            throw new Exception("Transaksi Gagal: " + e.getMessage());
        } finally {
            // Finalisasi: Reset AutoCommit & Tutup Statement
            if (conn != null) try { conn.setAutoCommit(true); } catch (SQLException ex) {}
            
            if (psHeader != null) psHeader.close();
            if (psDetail != null) psDetail.close();
            if (psUpdateStock != null) psUpdateStock.close();
            
            // PENTING: Connection JANGAN di-close di sini (Singleton)
        }
    }

    @Override
    public List<CartItem> getTransactionItems(String trxId) {
        List<CartItem> items = new ArrayList<>();
        String sql = "SELECT d.product_code, d.quantity, p.name, p.price " +
                     "FROM transaction_details d " +
                     "JOIN products p ON d.product_code = p.code " +
                     "WHERE d.transaction_id = ?";

        // PERBAIKAN 2: Connection diambil DI LUAR try-with-resources
        try {
            Connection conn = DatabaseConnection.getConnection();
            
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, trxId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Product p = new Product(
                            rs.getString("product_code"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            0 // Stok dummy untuk view history
                        );
                        items.add(new CartItem(p, rs.getInt("quantity")));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }
}