# Runbook - Cara Instalasi & Menjalankan Agri-POS

**Dokumen ini berisi panduan teknis untuk menyiapkan lingkungan kerja (Environment Setup), instalasi database, dan menjalankan aplikasi Agri-POS.**

---

## 1. Prasyarat Sistem (Prerequisites)
Sebelum menjalankan aplikasi, pastikan komputer Anda telah terinstal software berikut:

1.  **Java Development Kit (JDK):** Versi 17 atau lebih baru (Wajib untuk JavaFX).
2.  **Apache Maven:** Untuk manajemen dependensi.
3.  **PostgreSQL:** Database server (Versi 13+).
4.  **IDE:** Visual Studio Code (dengan Extension Pack for Java) atau IntelliJ IDEA.
5.  **Git:** Untuk kloning repositori.

---

## 2. Setup Database

Aplikasi ini membutuhkan database PostgreSQL dengan skema tabel tertentu agar bisa berjalan.

### Langkah-langkah:
1.  Buka **pgAdmin** atau terminal PostgreSQL.
2.  Buat database baru dengan nama: `agripos_db`.
    ```sql
    CREATE DATABASE agripos_db;
    ```
3.  **Import Skema & Data:**
    * Cari file `database_final.sql` yang ada di root folder project.
    * Buka **Query Tool** di pgAdmin (pada database `agripos_db`).
    * Copy seluruh isi file `database_final.sql` dan Paste ke Query Tool.
    * Klik tombol **Run/Execute** (▶).
    
    *Catatan: Script ini akan otomatis membuat tabel `users`, `products`, `transactions`, `customers`, `vouchers`, dan mengisi data awal (seeding).*

---

## 3. Konfigurasi Aplikasi (Database Connection)

Jika username atau password PostgreSQL di laptop Anda berbeda dengan default code, Anda perlu menyesuaikannya.

1.  Buka file: `src/main/java/com/upb/agripos/util/DatabaseConnection.java` (atau `DBConnection.java`).
2.  Cari baris konfigurasi berikut dan sesuaikan:

```java
private static final String URL = "jdbc:postgresql://localhost:5432/agripos_db";
private static final String USER = "postgres";  // Ganti dengan username DB Anda
private static final String PASS = "admin";     // Ganti dengan password DB Anda
```
## 4. Cara Menjalankan Aplikasi (Run)

### Opsi A: Menggunakan Maven (Terminal)
Buka terminal di root folder project, lalu jalankan perintah:

```bash
mvn clean javafx:run
```

### Opsi B: Menggunakan IDE (VS Code / IntelliJ)
1.  Buka file utama: `src/main/java/com/upb/agripos/AppJavaFX.java` (atau `Launcher.java`).
2.  Tunggu hingga Java Language Server siap.
3.  Klik tombol **Run** atau **Debug** yang muncul di atas nama class `main`.

---

## 5. Troubleshooting (Kendala Umum)

Berikut adalah solusi untuk error yang sering muncul:

| Masalah | Pesan Error | Solusi |
| :--- | :--- | :--- |
| **Gagal Koneksi DB** | `PSQLException: Connection refused` / `FATAL: password authentication failed` | 1. Cek apakah service PostgreSQL sudah jalan.<br>2. Cek username/password di file `DatabaseConnection.java`. |
| **Tabel Tidak Ada** | `PSQLException: relation "vouchers" does not exist` | Database Anda masih versi lama. Jalankan ulang script `database_final.sql` untuk update skema Week 15. |
| **Kolom Hilang** | `PSQLException: column "member_code" does not exist` | Sama seperti di atas, skema database belum di-update dengan kolom baru. |
| **Export Gagal** | `FileNotFoundException: ... (The process cannot access the file because it is being used by another process)` | Tutup file Excel laporan yang sedang terbuka sebelum melakukan Export CSV baru. |
| **JavaFX Error** | `Error: JavaFX runtime components are missing` | Pastikan Anda menjalankan lewat Maven (`mvn javafx:run`) atau konfigurasi `vmArgs` di IDE sudah benar menunjuk ke library JavaFX. |

---

## 6. Akun Login Demo
Gunakan akun berikut untuk pengujian:

* **Admin:** `admin` / `admin123`
* **Kasir:** `kasir` / `kasir123`