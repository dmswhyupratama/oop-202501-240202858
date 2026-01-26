package com.upb.agripos.model.payment;

public class EWalletPayment extends Pembayaran {
    public EWalletPayment(double totalBelanja) {
        super(totalBelanja);
    }

    @Override
    public double hitungTotalBayar() {
        // Logika Week 5: Ada pajak 1.5% kalau pakai E-Wallet
        return totalBelanja + (totalBelanja * 0.015);
    }

    @Override
    public String getNamaMetode() {
        return "E-WALLET";
    }
}