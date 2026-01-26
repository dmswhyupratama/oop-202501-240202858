# Arsitektur Sistem Agri-POS

**Dokumen ini menjelaskan desain arsitektur perangkat lunak, struktur paket (package), dan aturan dependensi yang diterapkan dalam pengembangan Agri-POS.**

---

## 1. Pola Arsitektur (Architectural Pattern)

Agri-POS dibangun menggunakan pola **Layered Architecture** (Arsitektur Berlapis) yang diadaptasi dari konsep **MVC (Model-View-Controller)**. Tujuan utama arsitektur ini adalah **Separation of Concerns** (pemisahan tanggung jawab), di mana kode antarmuka (UI) tidak bercampur dengan logika bisnis maupun kode akses database.

### Lapisan Sistem (System Layers)

Sistem dibagi menjadi 4 lapisan utama:

1.  **Presentation Layer (View):**
    * Berisi kode antarmuka pengguna (GUI) berbasis JavaFX.
    * Hanya bertugas menampilkan data dan menerima input pengguna.
    * **Aturan:** Dilarang keras menulis query SQL atau logika bisnis kompleks di sini.

2.  **Control Layer (Controller):**
    * Menjembatani View dan Service.
    * Menangani *event* dari pengguna (klik tombol, input text).
    * Memanggil Service untuk memproses permintaan pengguna.

3.  **Domain/Service Layer (Business Logic):**
    * Jantung dari logika aplikasi.
    * Menangani validasi data, kalkulasi (misal: hitung diskon, total belanja), dan aturan bisnis lainnya.
    * Bersifat *agnostic* terhadap UI (tidak peduli UI-nya pakai JavaFX atau Console).

4.  **Data Access Layer (DAO):**
    * Satu-satunya lapisan yang diizinkan mengakses Database.
    * Melakukan operasi CRUD (Create, Read, Update, Delete) menggunakan JDBC.
    * Mengubah data dari Database (ResultSet) menjadi Objek Java (Model).

---

## 2. Diagram Arsitektur

Berikut adalah visualisasi aliran data dan ketergantungan antar komponen:

```mermaid
graph TD
    User[Pengguna] --> View[View Layer (JavaFX)]
    View --> Controller[Controller Layer]
    Controller --> Service[Service Layer]
    Service --> DAO[DAO Layer (Interface)]
    DAO -.-> Impl[DAO Implementation]
    Impl --> DB[(PostgreSQL Database)]
    
    subgraph Core Logic
    Service
    end
    
    subgraph Data Infrastructure
    DAO
    Impl
    DB
    end

## 3. Peta Paket (Package Map)

Struktur kode program diatur dalam paket-paket (*packages*) berikut sesuai dengan implementasi Week 15:

```text
com.upb.agripos
├── AppJavaFX.java          # Entry Point (Main Class)
├── Launcher.java           # Launcher untuk build JAR
│
├── config/                 # Konfigurasi
│   └── DatabaseConnection.java  # Singleton Koneksi Database
│
├── model/                  # Entitas Data (POJO)
│   ├── payment/            # Pola Strategy (Pembayaran)
│   │   ├── Pembayaran.java      # Interface/Abstract Parent
│   │   ├── CashPayment.java     # Implementasi Tunai
│   │   └── EWalletPayment.java  # Implementasi E-Wallet
│   ├── CartItem.java
│   ├── Customer.java
│   ├── Product.java
│   ├── Transaction.java
│   ├── User.java
│   └── Voucher.java
│
├── view/                   # Antarmuka Pengguna (GUI)
│   ├── LoginView.java
│   ├── MainLayout.java
│   ├── CashierView.java
│   ├── ReportView.java
│   ├── ProductView.java
│   ├── ReturnView.java
│   ├── ReceiptDialog.java
│   └── MemberRegistrationDialog.java
│
├── controller/             # Pengendali Alur
│   ├── CashierController.java
│   ├── ProductController.java
│   └── ReturnController.java
│
├── service/                # Logika Bisnis
│   ├── AuthService.java    # Cek login & hak akses
│   ├── ProductService.java # Validasi stok
│   └── ReportService.java  # Logika Export CSV
│
├── dao/                    # Akses Database (JDBC)
│   ├── CustomerDAO.java & Impl
│   ├── ProductDAO.java & Impl
│   ├── TransactionDAO.java & Impl
│   ├── UserDAO.java & Impl
│   ├── ReturnDAO.java
│   └── VoucherDAO.java
│
└── util/                   # Utilitas Pendukung
    └── AppStyles.java      # Konstanta CSS/Style

## 4. Aturan Dependensi & DIP

Untuk menjaga kualitas kode, sistem menerapkan aturan dependensi berikut:

### A. Aturan Arah Ketergantungan (One-Way Dependency)
Ketergantungan hanya boleh mengarah dari lapisan atas ke lapisan bawah.

* ✅ **Benar:** `Controller` memanggil `Service`.
* ❌ **Salah:** `Service` memanggil `Controller` (Logika bisnis tidak boleh tahu siapa yang memanggilnya).
* ✅ **Benar:** `Service` memanggil `DAO`.
* ❌ **Salah:** `View` memanggil `DAO` (UI tidak boleh langsung akses DB).

### B. Dependency Inversion Principle (DIP)
Kami menerapkan prinsip SOLID (DIP) pada lapisan Data Access:

* Module tingkat tinggi (`Service`) tidak bergantung pada detail implementasi tingkat rendah (`DAOImpl`).
* Sebaliknya, keduanya bergantung pada **Abstraksi (Interface)**.
    * *Contoh:* `ProductService` tidak menginisialisasi `new ProductDAOImpl()`, melainkan menerima objek `ProductDAO` (Interface) melalui constructor (Dependency Injection).
    * Hal ini memudahkan pengujian (Unit Testing) karena DAO asli bisa diganti dengan Mock Object.

### C. Enkapsulasi Model
* Objek Model (`com.upb.agripos.model`) adalah objek data murni yang boleh diakses oleh semua lapisan untuk pertukaran data, namun model tidak boleh mengandung logika akses database.