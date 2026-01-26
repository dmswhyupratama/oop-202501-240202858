package com.upb.agripos.service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.upb.agripos.dao.TransactionDAO;
import com.upb.agripos.model.Transaction;

public class ReportService {
    private TransactionDAO transactionDAO;

    public ReportService(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }

    public List<Transaction> getSalesReport() {
        return transactionDAO.findAll();
    }

    // --- FITUR BARU: EXPORT CSV ---
    public boolean exportToCSV(String filePath) {
        List<Transaction> transactions = getSalesReport();
        String header = "ID Transaksi,Tanggal,Metode Bayar,Total Belanja,Total Diskon,ID User\n";

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(header);
            
            for (Transaction t : transactions) {
                // 1. FIX TANGGAL: Karena getDate() kamu sudah String, langsung pakai saja.
                String dateStr = (t.getDate() != null) ? t.getDate() : "-";

                // 2. FIX GETTER: Sesuaikan dengan Transaction.java kamu
                // getPaymentMethod() dan getTotalAmount()
                String line = String.format("%s,%s,%s,%.0f,%.0f,%d\n",
                    t.getId(),
                    dateStr,
                    t.getPaymentMethod(), // Sesuai Transaction.java
                    t.getTotalAmount(),   // Sesuai Transaction.java
                    t.getDiscount(),
                    t.getUserId()
                );
                writer.write(line);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}