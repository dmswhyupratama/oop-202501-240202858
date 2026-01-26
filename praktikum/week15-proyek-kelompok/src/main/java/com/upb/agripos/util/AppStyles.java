package com.upb.agripos.util;

public class AppStyles {
    // Warna Tema
    public static final String COL_PRIMARY = "#2E7D32"; // Hijau Tua
    public static final String COL_ACCENT = "#F57C00";  // Oranye
    public static final String COL_DANGER = "#D32F2F";  // Merah
    public static final String COL_SIDEBAR = "#1B5E20"; // Hijau Lebih Gelap

    // --- STYLE SIDEBAR (TIDAK DIUBAH) ---
    public static final String BTN_SIDEBAR = 
        "-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 14px; -fx-alignment: BASELINE_LEFT; -fx-padding: 10 20; -fx-cursor: hand;";

    public static final String BTN_SIDEBAR_ACTIVE = 
        "-fx-background-color: #43A047; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-alignment: BASELINE_LEFT; -fx-padding: 10 20; -fx-background-radius: 0 20 20 0;";

    public static final String BTN_SIDEBAR_LOGOUT = 
        "-fx-background-color: " + COL_DANGER + "; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-alignment: BASELINE_LEFT; -fx-padding: 10 20; -fx-cursor: hand;";

    // --- UPDATE: STYLE TOMBOL AKSI (FORM) ---

    // 1. Style Baru untuk Update (Hijau Terang Normal)
    public static final String BTN_PRIMARY = 
        "-fx-background-color: #43A047; " + // Warna sama dengan sidebar active
        "-fx-text-fill: white; " +
        "-fx-font-weight: bold; " +
        "-fx-background-radius: 5; " + // Membulat normal di semua sudut
        "-fx-padding: 8 15; " + // Tambah padding biar tombol agak besar
        "-fx-cursor: hand;";

    // 2. Update BTN_SUCCESS (Simpan) biar seragam paddingnya
    public static final String BTN_SUCCESS = 
        "-fx-background-color: " + COL_PRIMARY + "; " +
        "-fx-text-fill: white; " +
        "-fx-font-weight: bold; " +
        "-fx-background-radius: 5; " +
        "-fx-padding: 8 15; " + // Samakan padding
        "-fx-cursor: hand;";

    // 3. Update BTN_DANGER (Hapus) biar seragam paddingnya
    public static final String BTN_DANGER = 
        "-fx-background-color: " + COL_DANGER + "; " +
        "-fx-text-fill: white; " +
        "-fx-font-weight: bold; " +
        "-fx-background-radius: 5; " +
        "-fx-padding: 8 15; " + // Samakan padding
        "-fx-cursor: hand;";
    
    // Style Card (TIDAK DIUBAH)
    public static final String CARD_STYLE = 
        "-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0); -fx-background-radius: 8;";
}