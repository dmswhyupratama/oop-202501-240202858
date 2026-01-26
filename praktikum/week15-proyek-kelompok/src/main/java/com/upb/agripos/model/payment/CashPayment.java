package com.upb.agripos.model.payment;

public class CashPayment extends Pembayaran {
    public CashPayment(double totalBelanja) {
        super(totalBelanja);
    }

    @Override
    public double hitungTotalBayar() {
        return totalBelanja; // Cash tidak ada biaya admin
    }

    @Override
    public String getNamaMetode() {
        return "CASH";
    }
}