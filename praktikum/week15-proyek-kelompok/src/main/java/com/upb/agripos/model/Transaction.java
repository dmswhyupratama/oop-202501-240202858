package com.upb.agripos.model;

public class Transaction {
    // Field Utama (Database)
    private String id;
    private double totalAmount;
    private String paymentMethod;
    private int userId;
    private int customerId;
    private double discount;

    // Field Tambahan (Khusus Tampilan Laporan)
    private String date;         // String tanggal yang sudah diformat
    private String cashierName;  // Nama Kasir (dari join users)
    private String customerName; // Nama Pelanggan (dari join customers)

    // CONSTRUCTOR 1: Untuk INSERT/Simpan (Dipakai saat Checkout Kasir)
    // Data tanggal & nama user tidak dibutuhkan di sini karena database yang handle
    public Transaction(String id, double totalAmount, String paymentMethod, int userId, int customerId, double discount) {
        this.id = id;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.userId = userId;
        this.customerId = customerId;
        this.discount = discount;
    }

    // CONSTRUCTOR 2: Untuk SELECT/Laporan (Dipakai di TransactionDAO.findAll)
    // PERBAIKAN: Semua parameter date, cashier, customer sekarang disimpan ke field
    public Transaction(String id, String date, double totalAmount, String paymentMethod, String cashierName, String customerName) {
        this.id = id;
        this.date = date;                  // <-- SEBELUMNYA HILANG
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.cashierName = cashierName;    // <-- SEBELUMNYA HILANG
        this.customerName = customerName;  // <-- SEBELUMNYA HILANG
    }

    // --- GETTER (Wajib ada agar PropertyValueFactory di Tabel bisa baca) ---
    public String getId() { return id; }
    public double getTotalAmount() { return totalAmount; }
    public String getPaymentMethod() { return paymentMethod; }
    public int getUserId() { return userId; }
    public int getCustomerId() { return customerId; }
    public double getDiscount() { return discount; }
    
    // GETTER UNTUK LAPORAN (PENTING)
    public String getDate() { return date; }
    public String getCashierName() { return cashierName; }
    public String getCustomerName() { return customerName; }
    
    // Helper Format Rupiah (Opsional, jika tabel pakai ini)
    public String getFormattedTotal() {
        return String.format("Rp %,.0f", totalAmount).replace(',', '.');
    }
}