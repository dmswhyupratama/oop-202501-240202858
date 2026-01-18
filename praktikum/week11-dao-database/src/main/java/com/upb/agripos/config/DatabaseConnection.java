package com.upb.agripos.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection;

    private static final String URL = "jdbc:postgresql://localhost:5432/agripos";
    private static final String USER = "postgres";
    private static final String PASSWORD = "admin123"; 

    // Constructor Private (Wajib untuk Singleton)
    private DatabaseConnection() {}

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // Opsional: Load driver manual untuk memastikan
                try {
                    Class.forName("org.postgresql.Driver");
                } catch (ClassNotFoundException e) {
                    System.err.println("Driver PostgreSQL tidak ditemukan!");
                }
                
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("--> [INFO] Koneksi Database Terhubung (Singleton).");
            }
        } catch (SQLException e) {
            System.err.println("Gagal konek database: " + e.getMessage());
        }
        return connection;
    }
}