package com.upb.agripos.service;

import com.upb.agripos.dao.UserDAO;
import com.upb.agripos.model.User;

public class AuthService {
    private UserDAO userDAO;

    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User login(String username, String password) throws Exception {
        // 1. Cari user di DB
        User user = userDAO.findByUsername(username);

        // 2. Cek apakah user ada?
        if (user == null) {
            throw new Exception("Username tidak ditemukan!");
        }

        // 3. Cek password (plain text sesuai request database tadi)
        if (!user.getPassword().equals(password)) {
            throw new Exception("Password salah!");
        }

        // 4. Login Sukses -> Kembalikan data user (biar tahu dia ADMIN atau KASIR)
        return user;
    }
}