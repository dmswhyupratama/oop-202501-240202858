# Desain Database - Agri-POS

**Dokumen ini menjelaskan struktur basis data (Schema) dan relasi antar tabel yang digunakan dalam sistem Agri-POS.**

---

## 1. Ringkasan Database
* **Database Name:** `agripos_db`
* **RDBMS:** PostgreSQL
* **Versi Skema:** Week 15 (Final Update)

---

## 2. Entity Relationship Diagram (ERD)

Berikut adalah deskripsi relasi antar entitas dalam sistem:

* **One-to-Many (User -> Transactions):** Satu kasir (`users`) dapat melayani banyak transaksi.
* **One-to-Many (Customer -> Transactions):** Satu pelanggan (`customers`) dapat memiliki riwayat banyak transaksi.
* **One-to-Many (Transaction -> TransactionDetails):** Satu transaksi memiliki banyak detail item barang.
* **Many-to-One (TransactionDetails -> Products):** Setiap detail transaksi merujuk pada satu produk spesifik.
* **One-to-Many (Transaction -> Returns):** Satu transaksi dapat memiliki beberapa retur barang (jika ada barang rusak).

*(Diagram visual dapat dilihat pada screenshot ERD jika tersedia)*

---

## 3. Skema Database (DDL)

Berikut adalah struktur tabel final yang digunakan.

### A. Tabel Master Data

#### 1. Tabel `users` (Pengguna)
Menyimpan data akun untuk login.
```sql
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL, -- 'ADMIN' atau 'KASIR'
    fullname VARCHAR(100)
);
```
#### 2. Tabel `products` (Inventori)
Menyimpan data stok barang dagangan.
```sql
CREATE TABLE products (
    code VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    stock INT NOT NULL
);
```
#### 3. Tabel `customers` (Pelanggan & Member)
Menyimpan data pelanggan umum dan member (CRM).
* Update Week 15: Penambahan kolom member_code dan points.
```sql
CREATE TABLE customers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) UNIQUE,
    is_member BOOLEAN DEFAULT FALSE,
    member_code VARCHAR(50) UNIQUE, -- Kode Member Unik
    points INT DEFAULT 0
);
```
#### 4. Tabel `vouchers` (Diskon)
Menyimpan data voucher promo aktif.
```sql
CREATE TABLE vouchers (
    code VARCHAR(50) PRIMARY KEY,
    discount_percent DOUBLE PRECISION NOT NULL, -- Cth: 0.10 untuk 10%
    description VARCHAR(100),
    is_active BOOLEAN DEFAULT TRUE
);
```
#### 5. Tabel `transactions` (Header Penjualan)
Mencatat header transaksi.
* Update Week 15: Penambahan kolom discount dan voucher_code.
```sql
CREATE TABLE transactions (
    id VARCHAR(20) PRIMARY KEY, -- Format: TRX-YYYYMMDD-XXX
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(15, 2) NOT NULL,
    payment_method VARCHAR(50), -- 'CASH', 'E-WALLET'
    user_id INT REFERENCES users(id),
    customer_id INT REFERENCES customers(id),
    discount DOUBLE PRECISION DEFAULT 0,
    voucher_code VARCHAR(50)
);
```
#### 6. Tabel `users` (Pengguna)
Menyimpan data akun untuk login.
```sql
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL, -- 'ADMIN' atau 'KASIR'
    fullname VARCHAR(100)
);
```
#### 7. Tabel `returns` (Retur Barang)
Mencatat pengembalian barang.
```sql
CREATE TABLE returns (
    id SERIAL PRIMARY KEY,
    transaction_id VARCHAR(50) REFERENCES transactions(id),
    product_code VARCHAR(50) REFERENCES products(code),
    quantity INT NOT NULL,
    reason TEXT,
    return_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'APPROVED'
);
```

## 4. Data Seeding (Initial Data)

Script berikut digunakan untuk mengisi data awal agar aplikasi bisa langsung dijalankan (Login & Demo).

```sql
-- 1. Akun Default
INSERT INTO users (username, password, role, fullname) VALUES 
('admin', 'admin123', 'ADMIN', 'Dimas Administrator'),
('kasir', 'kasir123', 'KASIR', 'Budi Kasir');
```

```sql
-- 2. Produk Contoh
INSERT INTO products (code, name, price, stock) VALUES 
('P001', 'Pupuk Urea', 50000, 100),
('P002', 'Benih Padi Premium', 25000, 200),
('A001', 'Cangkul Baja', 75000, 15);
```

```sql
-- 3. Pelanggan & Member Contoh
INSERT INTO customers (name, phone, is_member, member_code, points) VALUES 
('Pelanggan Umum', '-', FALSE, NULL, 0),
('Sultan Agri', '08123456789', TRUE, 'MEM-001', 50);
```

```sql
-- 4. Voucher Promo
INSERT INTO vouchers (code, discount_percent, description) VALUES 
('MEMBER10', 0.10, 'Diskon Member 10%'),
('PROMO5', 0.05, 'Promo Merdeka 5%');
```

```sql
## 5. Catatan Migrasi & Perubahan

### Update Week 15 (Final Integration)
Untuk mendukung fitur CRM (Member) dan Laporan yang lebih detail, dilakukan perubahan skema berikut pada database lama:
```
1.  **Tabel Customers:**
    * `ADD COLUMN member_code VARCHAR(50) UNIQUE`
    * `ADD COLUMN points INT DEFAULT 0`
    * **Tujuan:** Untuk mendukung pencarian member via kode unik dan sistem poin masa depan.

2.  **Tabel Transactions:**
    * `ADD COLUMN discount DOUBLE PRECISION DEFAULT 0`
    * `ADD COLUMN voucher_code VARCHAR(50)`
    * **Tujuan:** Untuk menyimpan histori potongan harga agar laporan keuangan akurat.

3.  **Tabel Vouchers:**
    * **New Table** `vouchers` dibuat untuk menyimpan master data diskon agar tidak *hardcoded* di program Java.
