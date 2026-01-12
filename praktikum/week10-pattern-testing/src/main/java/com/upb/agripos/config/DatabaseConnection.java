package com.upb.agripos.config;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    
    // Constructor private agar tidak bisa di-new sembarangan
    private DatabaseConnection() {
        System.out.println("Koneksi Database dibuat.");
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
}