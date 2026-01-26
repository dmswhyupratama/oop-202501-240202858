package com.upb.agripos.model;

public class Voucher {
    private String code;
    private double discountPercent;
    private String description;

    public Voucher(String code, double discountPercent, String description) {
        this.code = code;
        this.discountPercent = discountPercent;
        this.description = description;
    }

    public String getCode() { return code; }
    public double getDiscountPercent() { return discountPercent; }
    public String getDescription() { return description; }
}