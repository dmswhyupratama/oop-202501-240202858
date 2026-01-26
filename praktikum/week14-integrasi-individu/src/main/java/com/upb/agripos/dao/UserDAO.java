package com.upb.agripos.dao;

import com.upb.agripos.model.User;

public interface UserDAO {
    User findByUsername(String username);
}