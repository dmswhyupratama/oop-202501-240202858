package com.upb.agripos.model;

public class Customer {
    private int id;
    private String name;
    private String phone;
    private boolean isMember;   // Field Lama
    private String memberCode;  // Field Baru (Penting buat Diskon)
    private int points;         // Field Baru (Opsional)

    // 1. Constructor Kosong (Wajib ada untuk standar Java)
    public Customer() {
    }

    // 2. Constructor Lengkap (Dipakai saat ambil data dari Database via DAO)
    public Customer(int id, String name, String phone, boolean isMember, String memberCode, int points) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.isMember = isMember;
        this.memberCode = memberCode;
        this.points = points;
    }

    // 3. Constructor untuk REGISTRASI Member Baru (Tanpa ID, otomatis Member)
    // Dipakai saat Admin/Kasir input member baru di form
    public Customer(String name, String phone, String memberCode) {
        this.name = name;
        this.phone = phone;
        this.isMember = true; // Kalau register, otomatis jadi member
        this.memberCode = memberCode;
        this.points = 0;      // Poin awal 0
    }

    // ================= GETTER & SETTER =================
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public boolean isMember() { return isMember; }
    public void setMember(boolean member) { isMember = member; }

    public String getMemberCode() { return memberCode; }
    public void setMemberCode(String memberCode) { this.memberCode = memberCode; }

    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }

    // ================= TO STRING =================
    // Diupdate biar tampil Kode Member juga di ComboBox
    @Override
    public String toString() {
        if (isMember && memberCode != null) {
            return name + " (" + memberCode + ")";
        }
        return name + (isMember ? " (Member)" : " (Umum)");
    }
}