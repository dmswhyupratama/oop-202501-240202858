package com.upb.agripos.service;

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
}