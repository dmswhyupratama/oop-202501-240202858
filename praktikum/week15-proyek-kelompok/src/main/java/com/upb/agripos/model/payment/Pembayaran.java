package com.upb.agripos.model.payment;

public abstract class Pembayaran {
    protected double totalBelanja;

    public Pembayaran(double totalBelanja) {
        this.totalBelanja = totalBelanja;
    }

    public abstract double hitungTotalBayar(); // Total + Pajak/Admin
    public abstract String getNamaMetode();
}