package com.upb.agripos.dao;

import java.util.List;

import com.upb.agripos.model.CartItem;
import com.upb.agripos.model.Transaction;

public interface TransactionDAO {
    
    // 1. Method untuk mengambil semua data laporan (Admin)
    List<Transaction> findAll();
    
    // 2. Method untuk menyimpan transaksi belanja (Kasir)
    void save(Transaction trx, List<CartItem> items) throws Exception;

    // 3. Method untuk mengambil detail barang per transaksi (Fitur Retur)
    // PENTING: Method ini wajib ada agar @Override di TransactionDAOImpl valid
    List<CartItem> getTransactionItems(String trxId);
}