package com.upb.agripos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.upb.agripos.config.DatabaseConnection;
import com.upb.agripos.model.Voucher;

public class VoucherDAO {

    public Voucher findByCode(String code) {
        String sql = "SELECT * FROM vouchers WHERE code = ? AND is_active = TRUE";
        
        // PERBAIKAN: Connection diambil di luar try-with-resources agar TIDAK OTOMATIS TERTUTUP
        try {
            Connection conn = DatabaseConnection.getConnection();
            
            // Hanya PreparedStatement & ResultSet yang boleh di dalam try(...) agar otomatis close
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, code);
                
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new Voucher(
                            rs.getString("code"),
                            rs.getDouble("discount_percent"),
                            rs.getString("description")
                        );
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Tidak ditemukan
    }
}