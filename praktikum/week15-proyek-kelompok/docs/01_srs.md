# Dokumen Desain Sistem (SRS) - Agri-POS

**Nama Proyek:** Agri-POS (Agricultural Point of Sales)  
**Versi Dokumen:** 1.0  
**Tanggal:** 26 Januari 2026

---

## 1. Pendahuluan

### 1.1 Latar Belakang Masalah
Sektor ritel pertanian skala menengah seringkali menghadapi kendala dalam operasional harian karena masih mengandalkan pencatatan manual. Masalah utama yang sering muncul meliputi:
* Kesalahan perhitungan total belanja dan kembalian.
* Selisih stok barang (inventory) yang sulit dilacak.
* Tidak adanya data pelanggan setia (member) yang terorganisir.
* Kesulitan pemilik toko dalam merekap laporan penjualan harian secara cepat.

### 1.2 Tujuan Sistem
Tujuan pengembangan **Agri-POS** adalah menyediakan solusi perangkat lunak berbasis desktop yang mampu:
* Mengotomatisasi proses transaksi penjualan agar lebih cepat dan akurat.
* Mengelola data produk dan stok secara real-time.
* Membangun hubungan pelanggan melalui fitur keanggotaan (membership) dan diskon.
* Menyediakan laporan penjualan yang transparan dan mudah diolah (exportable).

---

## 2. Ruang Lingkup & Batasan

### 2.1 Ruang Lingkup (Scope)
Sistem Agri-POS mencakup fitur-fitur berikut:
* Manajemen Inventori Produk (CRUD).
* Transaksi Penjualan (Point of Sales) dengan dukungan keranjang belanja.
* Manajemen Pelanggan (Member) dan Diskon.
* Penanganan Retur Barang.
* Laporan Penjualan dengan fitur ekspor data.
* Manajemen Hak Akses (Admin & Kasir).

### 2.2 Batasan Sistem (Constraints)
* **Platform:** Aplikasi Desktop berbasis Java (JavaFX).
* **Database:** PostgreSQL (Relational Database Management System).
* **Konektivitas:** Berjalan pada jaringan lokal (Localhost/LAN).
* **Perangkat Keras:** Input menggunakan Keyboard/Mouse standar (belum terintegrasi langsung dengan Barcode Scanner fisik).

---

## 3. Kebutuhan Fungsional (Functional Requirements)

Berikut adalah daftar kebutuhan fungsional berdasarkan standar FR dan OFR (Optional Functional Requirements) yang telah diimplementasikan:

| ID Requirement | Deskripsi Fitur | Aktor | Status |
| :--- | :--- | :--- | :--- |
| **FR-1** | **Manajemen Produk**<br>Admin dapat menambah, mengubah, menghapus, dan melihat daftar produk beserta stoknya. | Admin | ✅ Selesai |
| **FR-2** | **Transaksi Penjualan**<br>Kasir dapat memasukkan item ke keranjang, mengubah kuantitas, dan memproses pembayaran. | Kasir | ✅ Selesai |
| **FR-3** | **Metode Pembayaran**<br>Sistem mendukung pembayaran Tunai dan E-Wallet. | Kasir | ✅ Selesai |
| **FR-4** | **Laporan & Struk**<br>Admin dapat melihat rekap penjualan dan melakukan Export ke CSV. | Admin | ✅ Selesai |
| **FR-5** | **Login & Hak Akses**<br>Sistem membatasi akses fitur berdasarkan role (Admin/Kasir). | Semua | ✅ Selesai |
| **OFR-1** | **Retur Barang (Opsional)**<br>Admin/Kasir dapat memproses pengembalian barang dan stok otomatis bertambah. | Semua | ✅ Selesai |
| **OFR-2** | **Diskon & Voucher (Opsional)**<br>Sistem dapat menerapkan logika diskon berdasarkan kode voucher atau threshold belanja. | Kasir | ✅ Selesai |
| **OFR-3** | **Pelanggan & Member (Opsional)**<br>Fitur registrasi member baru di kasir dan penerapan diskon khusus member. | Kasir | ✅ Selesai |

---

## 4. Kebutuhan Non-Fungsional (Non-Functional Requirements)

1.  **Reliability (Keandalan):** Data transaksi harus konsisten (ACID Compliant) menggunakan transaksi database PostgreSQL.
2.  **Usability (Kemudahan Penggunaan):** Antarmuka (GUI) harus intuitif, meminimalkan jumlah klik untuk menyelesaikan transaksi.
3.  **Performance (Kinerja):** Waktu respon pencarian produk dan kalkulasi total harus di bawah 2 detik.
4.  **Security (Keamanan):** Password pengguna harus disimpan secara aman, dan akses ke menu sensitif (seperti kelola produk) dibatasi hanya untuk Admin.

---

## 5. Arsitektur Sistem

Sistem dikembangkan menggunakan pola arsitektur **MVC (Model-View-Controller)** yang dipadukan dengan pola **DAO (Data Access Object)** untuk pemisahan tanggung jawab yang jelas.

### 5.1 Layering Architecture

* **View Layer (Presentation):**
    * Dibangun menggunakan JavaFX.
    * Bertanggung jawab menampilkan antarmuka pengguna (`CashierView`, `ReportView`, `LoginView`).
    * Tidak mengandung logika bisnis atau query SQL.

* **Controller Layer:**
    * Menghubungkan View dengan Service/Model.
    * Menangani event input pengguna (klik tombol, input text).
    * Contoh: `CashierController`, `ProductController`.

* **Service Layer (Business Logic):**
    * Menangani aturan bisnis utama sebelum data dikirim ke database.
    * Contoh: Perhitungan total diskon, validasi stok sebelum checkout, logika export CSV.
    * Contoh Class: `TransactionService`, `ReportService`.

* **DAO Layer (Data Access):**
    * Satu-satunya layer yang berinteraksi langsung dengan Database PostgreSQL.
    * Menggunakan JDBC PreparedStatement untuk keamanan dan performa.
    * Contoh Class: `ProductDAOImpl`, `TransactionDAOImpl`, `CustomerDAOImpl`.

* **Database Layer:**
    * PostgreSQL Database (`agripos_db`) sebagai tempat penyimpanan data persisten.

### 5.2 Diagram Arsitektur (High-Level)
