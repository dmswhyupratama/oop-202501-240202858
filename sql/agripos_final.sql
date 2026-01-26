-- =============================================================
-- DATABASE SETUP SCRIPT - AGRIPOS (FINAL WEEK 15)
-- Author: Lead Developer (Dimas & Team)
-- =============================================================

-- 1. HAPUS TABEL LAMA (Urutan penting karena Foreign Key)
-- Ini biar kalau temen lu run ulang, gak error "Table already exists"
DROP TABLE IF EXISTS returns CASCADE;
DROP TABLE IF EXISTS transaction_details CASCADE;
DROP TABLE IF EXISTS transactions CASCADE;
DROP TABLE IF EXISTS vouchers CASCADE;
DROP TABLE IF EXISTS products CASCADE;
DROP TABLE IF EXISTS customers CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- =============================================================
-- STRUKTUR TABEL (SCHEMA)
-- =============================================================

-- 1. TABEL PENGGUNA (Login)
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL, 
    role VARCHAR(20) NOT NULL, -- 'ADMIN' atau 'KASIR'
    fullname VARCHAR(100)
);

-- 2. TABEL PELANGGAN (Member, Poin, & Member Code)
-- Sudah digabung dengan update terakhir (member_code, points, unique phone)
CREATE TABLE customers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) UNIQUE, -- Wajib Unik
    is_member BOOLEAN DEFAULT FALSE,
    member_code VARCHAR(50) UNIQUE, -- Kode Member Unik (e.g., MEM-001)
    points INT DEFAULT 0
);

-- 3. TABEL PRODUK
CREATE TABLE products (
    code VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    stock INT NOT NULL
);

-- 4. TABEL VOUCHER (Diskon)
CREATE TABLE vouchers (
    code VARCHAR(50) PRIMARY KEY,
    discount_percent DOUBLE PRECISION NOT NULL,
    description VARCHAR(100),
    is_active BOOLEAN DEFAULT TRUE
);

-- 5. TABEL TRANSAKSI (Header)
-- Sudah digabung dengan kolom discount & voucher_code
CREATE TABLE transactions (
    id VARCHAR(20) PRIMARY KEY,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(15, 2) NOT NULL,
    payment_method VARCHAR(50),
    user_id INT REFERENCES users(id),
    customer_id INT REFERENCES customers(id),
    discount DOUBLE PRECISION DEFAULT 0, -- Kolom baru
    voucher_code VARCHAR(50)             -- Kolom baru
);

-- 6. TABEL DETAIL TRANSAKSI (Keranjang)
CREATE TABLE transaction_details (
    id SERIAL PRIMARY KEY,
    transaction_id VARCHAR(20) REFERENCES transactions(id),
    product_code VARCHAR(20) REFERENCES products(code),
    quantity INT NOT NULL,
    subtotal DECIMAL(15, 2) NOT NULL
);

-- 7. TABEL RETUR
-- Tipe data sudah disesuaikan jadi VARCHAR(50) sesuai request terakhir
CREATE TABLE returns (
    id SERIAL PRIMARY KEY,
    transaction_id VARCHAR(50) REFERENCES transactions(id),
    product_code VARCHAR(50) REFERENCES products(code),
    quantity INT NOT NULL,
    reason TEXT,
    return_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'APPROVED'
);

-- =============================================================
-- DATA DUMMY (SEEDING)
-- =============================================================

-- User Login
INSERT INTO users (username, password, role, fullname) VALUES 
('admin', 'admin123', 'ADMIN', 'Dimas Administrator'),
('kasir', 'kasir123', 'KASIR', 'Budi Kasir');

-- Pelanggan Awal
INSERT INTO customers (name, phone, is_member, member_code, points) VALUES 
('Pelanggan Umum', '-', FALSE, NULL, 0),
('Sultan Agri', '08123456789', TRUE, 'MEM-001', 50);

-- Produk Awal
INSERT INTO products (code, name, price, stock) VALUES 
('P001', 'Pupuk Urea', 50000, 100),
('P002', 'Benih Padi Premium', 25000, 200),
('A001', 'Cangkul Baja', 75000, 15);

-- Voucher Awal
INSERT INTO vouchers (code, discount_percent, description) VALUES 
('MEMBER10', 0.10, 'Diskon Member 10%'),
('PROMO5', 0.05, 'Promo Merdeka 5%');

-- Selesai!