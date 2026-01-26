package com.upb.agripos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.upb.agripos.config.DatabaseConnection;
import com.upb.agripos.model.User;

public class UserDAOImpl implements UserDAO {

    @Override
    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("fullname")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Return null jika user tidak ditemukan
    }
}