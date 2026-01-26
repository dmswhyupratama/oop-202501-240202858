# Panduan Pengguna (User Guide) - Agri-POS

**Dokumen ini berisi panduan singkat penggunaan aplikasi Agri-POS untuk peran Admin dan Kasir.**

---

## 1. Login Sistem
Saat aplikasi pertama kali dijalankan, pengguna akan disambut halaman Login.

1.  Masukkan **Username** dan **Password**.
2.  Klik tombol **Login**.
3.  Sistem akan mengarahkan ke dashboard sesuai hak akses (role).

**Kredensial Default (Untuk Demo):**
| Role | Username | Password | Akses Fitur |
| :--- | :--- | :--- | :--- |
| **Admin** | `admin` | `admin123` | Kelola Produk, Laporan Penjualan |
| **Kasir** | `kasir` | `kasir123` | Transaksi Kasir, Retur Barang |

---

## 2. Panduan Admin (Pemilik Toko)

### A. Manajemen Produk (Kelola Stok)
Menu ini digunakan untuk menambah, mengubah, atau menghapus data barang.

1.  Klik menu **"Kelola Produk"** di sidebar.
2.  **Tambah Produk:**
    * Isi form di sebelah kiri (Kode, Nama, Harga, Stok).
    * Klik tombol **[Simpan]**.
3.  **Edit Produk:**
    * Klik salah satu baris produk di tabel.
    * Ubah data di form input.
    * Klik tombol **[Update]**.
4.  **Hapus Produk:**
    * Pilih produk di tabel, lalu klik **[Hapus]**.

### B. Laporan Penjualan & Export Data
Menu ini digunakan untuk melihat rekapitulasi transaksi.

1.  Klik menu **"Laporan Penjualan"**.
2.  Tabel akan menampilkan riwayat transaksi (ID, Tanggal, Kasir, Total).
3.  Klik **[Refresh Data]** untuk memuat data terbaru.
4.  **Export ke Excel/CSV (Fitur Baru):**
    * Klik tombol berwarna oranye **[Export CSV]**.
    * Pilih lokasi penyimpanan di komputer Anda.
    * File laporan siap dibuka menggunakan Microsoft Excel.

---

## 3. Panduan Kasir (Operasional)

### A. Melakukan Transaksi Penjualan
1.  Klik menu **"Transaksi Penjualan"**.
2.  **Cari Produk:** Ketik nama/kode barang di kolom pencarian atau pilih langsung dari tabel.
3.  **Masukan ke Keranjang:**
    * Masukkan jumlah beli di kolom "Jml".
    * Klik tombol **[Masukan ke Keranjang ->]**.
4.  **Kelola Keranjang:**
    * Jika salah input qty, klik item di tabel kanan -> **[Ubah Qty]**.
    * Untuk membatalkan item, klik **[Hapus]**.

### B. Fitur Member & Diskon (CRM)
Ini adalah fitur untuk memberikan potongan harga kepada pelanggan.

**Cara 1: Registrasi Member Baru (Langsung di Kasir)**
1.  Jika pelanggan belum terdaftar, klik tombol hijau **[+ Member]**.
2.  Isi Nama dan No HP pelanggan pada jendela *pop-up*.
3.  Klik **Simpan**. Kode member akan ter-generate otomatis (misal: `MEM-123`).

**Cara 2: Menggunakan Diskon Member/Voucher**
1.  Pada bagian bawah keranjang, ketik **Kode Member** (misal: `MEM-001`) atau **Kode Voucher** (misal: `PROMO5`) di kolom input.
2.  Klik tombol **[Cek]**.
3.  Jika valid, info diskon akan muncul (misal: *"✅ Diskon Member 10%"*) dan total tagihan otomatis berkurang.

### C. Pembayaran (Checkout)
1.  Pilih **Metode Pembayaran** (CASH atau E-WALLET).
2.  Lihat **Total Tagihan** akhir.
3.  Masukkan nominal uang pelanggan di kolom **Bayar (Rp)**.
4.  Sistem akan menghitung kembalian secara otomatis.
5.  Klik tombol **[BAYAR / CHECKOUT]** untuk menyelesaikan transaksi.

---

## 4. Panduan Retur Barang
Digunakan jika pelanggan ingin mengembalikan barang yang rusak atau salah beli.

1.  Klik menu **"Retur Barang"**.
2.  Masukkan **ID Transaksi** (bisa dilihat di laporan/struk).
3.  Sistem akan menampilkan daftar barang dari transaksi tersebut.
4.  Pilih barang yang ingin diretur dan masukkan jumlahnya.
5.  Klik **[Proses Retur]**. Stok barang di gudang akan otomatis bertambah kembali.

---

**Catatan Teknis:**
* Pastikan database PostgreSQL sudah berjalan sebelum membuka aplikasi.
* File CSV hasil export tersimpan dengan format `Laporan_AgriPOS_Tahun-Bulan-Tanggal.csv`.