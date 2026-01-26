package com.upb.agripos.dao;

import java.util.List;

import com.upb.agripos.model.Customer;

public interface CustomerDAO {
    // Simpan member baru
    void save(Customer customer) throws Exception;
    
    // Update data member (opsional, buat edit nama/hp)
    void update(Customer customer) throws Exception;
    
    // Hapus member
    void delete(String memberCode) throws Exception;
    
    // Cari member buat validasi diskon (PENTING)
    Customer findByMemberCode(String code) throws Exception;
    
    // Cari by Phone (buat cek biar gak double daftar)
    Customer findByPhone(String phone) throws Exception;
    
    // Ambil semua data buat tabel Admin
    List<Customer> findAll() throws Exception;
}