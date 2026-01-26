package com.upb.agripos.model;

public class Customer {
    private int id;
    private String name;
    private String phone;
    private boolean isMember;

    public Customer(int id, String name, String phone, boolean isMember) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.isMember = isMember;
    }

    // Getter & Setter (Generate via IDE -> Right Click -> Source Action)
    public int getId() { return id; }
    public String getName() { return name; }
    public boolean isMember() { return isMember; }
    
    // Override toString biar nanti enak pas muncul di ComboBox/Pilihan
    @Override
    public String toString() {
        return name + (isMember ? " (Member)" : "");
    }
}