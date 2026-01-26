package com.upb.agripos.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import com.upb.agripos.dao.CustomerDAO;
import com.upb.agripos.dao.CustomerDAOImpl;
import com.upb.agripos.dao.TransactionDAO;
import com.upb.agripos.dao.VoucherDAO;
import com.upb.agripos.model.CartItem;
import com.upb.agripos.model.Customer;
import com.upb.agripos.model.Product;
import com.upb.agripos.model.Transaction;
import com.upb.agripos.model.User;
import com.upb.agripos.model.Voucher;
import com.upb.agripos.model.payment.CashPayment;
import com.upb.agripos.model.payment.EWalletPayment;
import com.upb.agripos.model.payment.Pembayaran;
import com.upb.agripos.service.ProductService;
import com.upb.agripos.view.CashierView;
import com.upb.agripos.view.MemberRegistrationDialog;
import com.upb.agripos.view.ReceiptDialog;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

public class CashierController {
    private CashierView view;
    private ProductService productService;
    private TransactionDAO transactionDAO;
    private VoucherDAO voucherDAO;
    private CustomerDAO customerDAO;
    private User currentUser;

    private ObservableList<Product> productList = FXCollections.observableArrayList();
    private ObservableList<CartItem> cartItems = FXCollections.observableArrayList();
    private FilteredList<Product> filteredProducts;

    private double currentGrandTotal = 0;
    
    // STATE DISKON
    private Voucher activeVoucher = null;
    private final double THRESHOLD_LIMIT = 500000;
    private final double THRESHOLD_DISC_PERCENT = 0.05;
    
    // Konstanta Diskon Member (10%)
    private final double MEMBER_DISC_PERCENT = 0.10; 

    public CashierController(CashierView view, ProductService productService, TransactionDAO transactionDAO, User currentUser) {
        this.view = view;
        this.productService = productService;
        this.transactionDAO = transactionDAO;
        this.voucherDAO = new VoucherDAO();
        this.customerDAO = new CustomerDAOImpl();
        this.currentUser = currentUser;

        initController();
        loadProducts();
    }

    private void initController() {
        // Binding Tabel & Search
        view.getTblCart().setItems(cartItems);
        filteredProducts = new FilteredList<>(productList, p -> true);
        view.getTblProduct().setItems(filteredProducts);
        
        view.getTxtSearch().textProperty().addListener((observable, oldValue, newValue) -> {
            filteredProducts.setPredicate(product -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lower = newValue.toLowerCase();
                return product.getName().toLowerCase().contains(lower) || product.getCode().toLowerCase().contains(lower);
            });
        });

        // Event Tombol
        view.getBtnRefresh().setOnAction(e -> loadProducts());
        view.getBtnAddToFlow().setOnAction(e -> addSelectedToCart());
        view.getBtnRemoveItem().setOnAction(e -> removeFromCart());
        view.getBtnClearCart().setOnAction(e -> { 
            cartItems.clear(); 
            activeVoucher = null; 
            view.getTxtVoucher().clear();
            view.getLblDiscountInfo().setText("");
            calculateTotal(); 
        });
        view.getBtnEditQty().setOnAction(e -> editQuantity());

        // Event Voucher & Member
        view.getBtnCheckVoucher().setOnAction(e -> handleCheckVoucher());

        view.getBtnRegisterMember().setOnAction(e -> {
            new MemberRegistrationDialog().showAndWait();
        });

        // Event Pembayaran
        view.getCmbPaymentMethod().setOnAction(e -> calculateTotal());
        view.getBtnCheckout().setOnAction(e -> processCheckout());

        view.getTblProduct().getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) view.getTxtQtyInput().requestFocus();
        });
        
        view.getTxtPayment().textProperty().addListener((obs, oldVal, newVal) -> calculateChange(newVal));
    }

    // --- LOGIC CEK VOUCHER / MEMBER (SUDAH DIPERBAIKI) ---
    private void handleCheckVoucher() {
        String code = view.getTxtVoucher().getText().trim();
        if (code.isEmpty()) {
            activeVoucher = null;
            view.getLblDiscountInfo().setText("");
            calculateTotal();
            return;
        }

        try {
            // 1. Cek di Tabel VOUCHER dulu
            Voucher voucher = voucherDAO.findByCode(code);
            
            // 2. Kalau gak ketemu di Voucher, Cek di Tabel CUSTOMER (Member)
            if (voucher == null) {
                Customer member = customerDAO.findByMemberCode(code);
                if (member != null) {
                    // KETEMU MEMBER!
                    // [PERBAIKAN DI SINI] Menggunakan Constructor yang sesuai (String, double, String)
                    voucher = new Voucher(
                        member.getMemberCode(),              // Code
                        MEMBER_DISC_PERCENT,                 // Discount Percent
                        "Diskon Member: " + member.getName() // Description
                    );
                }
            }

            // 3. Evaluasi Hasil Akhir
            if (voucher != null) {
                // BERHASIL
                activeVoucher = voucher;
                view.getLblDiscountInfo().setText("✅ " + voucher.getDescription());
                view.getLblDiscountInfo().setStyle("-fx-text-fill: green; -fx-font-weight: bold; -fx-font-size: 11px;");
                calculateTotal(); 
            } else {
                // GAGAL TOTAL
                activeVoucher = null;
                view.getLblDiscountInfo().setText("❌ Kode tidak valid");
                view.getLblDiscountInfo().setStyle("-fx-text-fill: red; -fx-font-weight: bold; -fx-font-size: 11px;");
                showAlert("Invalid", "Kode Voucher atau Member tidak ditemukan.");
                view.getTxtVoucher().selectAll();
                view.getTxtVoucher().requestFocus();
                calculateTotal(); 
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error Database", e.getMessage());
        }
    }

    private void calculateTotal() {
        double subtotal = cartItems.stream().mapToDouble(CartItem::getSubtotal).sum();
        
        // Hitung Diskon
        double voucherDiscAmount = (activeVoucher != null) ? subtotal * activeVoucher.getDiscountPercent() : 0;
        double thresholdDiscAmount = (subtotal > THRESHOLD_LIMIT) ? subtotal * THRESHOLD_DISC_PERCENT : 0;
        
        double totalDiscount = voucherDiscAmount + thresholdDiscAmount;
        double afterDiscount = subtotal - totalDiscount;

        // Hitung Pajak
        String method = view.getCmbPaymentMethod().getValue();
        Pembayaran pembayaran = ("E-WALLET".equals(method)) ? new EWalletPayment(afterDiscount) : new CashPayment(afterDiscount);
        
        this.currentGrandTotal = pembayaran.hitungTotalBayar(); 
        double tax = currentGrandTotal - afterDiscount;

        // Update UI
        view.getLblSubtotal().setText(formatRupiah(subtotal));
        view.getLblDiscount().setText("-" + formatRupiah(totalDiscount));
        view.getLblTax().setText(formatRupiah(tax));
        view.getLblGrandTotal().setText(formatRupiah(currentGrandTotal));
        
        // Update Info Text
        String infoText = "";
        if (activeVoucher != null) {
            infoText += "✅ " + activeVoucher.getDescription();
            view.getLblDiscountInfo().setStyle("-fx-text-fill: green; -fx-font-weight: bold; -fx-font-size: 11px;");
        }
        
        if (subtotal > THRESHOLD_LIMIT) {
            if (!infoText.isEmpty()) infoText += " + ";
            infoText += "🎉 Diskon Belanja Besar (5%)";
            view.getLblDiscountInfo().setStyle("-fx-text-fill: blue; -fx-font-weight: bold; -fx-font-size: 11px;");
        }
        
        if (!infoText.isEmpty()) {
            view.getLblDiscountInfo().setText(infoText);
        } else if (activeVoucher == null && !view.getLblDiscountInfo().getText().contains("❌")) {
            view.getLblDiscountInfo().setText("");
        }

        calculateChange(view.getTxtPayment().getText());
    }

    private void loadProducts() { productList.setAll(productService.getAllProducts()); }
    
    private void addSelectedToCart() {
        Product selectedProduct = view.getTblProduct().getSelectionModel().getSelectedItem();
        if (selectedProduct == null) { showAlert("Pilih Produk", "Silakan klik produk di tabel."); return; }
        
        String qtyStr = view.getTxtQtyInput().getText().trim();
        if (qtyStr.isEmpty()) { showAlert("Qty Kosong", "Masukkan jumlah barang."); return; }

        try {
            int qty = Integer.parseInt(qtyStr);
            if (qty <= 0) throw new NumberFormatException();
            if (selectedProduct.getStock() < qty) { showAlert("Stok Kurang", "Sisa stok: " + selectedProduct.getStock()); return; }

            boolean exists = false;
            for (CartItem item : cartItems) {
                if (item.getProduct().getCode().equals(selectedProduct.getCode())) {
                    item.addQuantity(qty); exists = true; view.getTblCart().refresh(); break;
                }
            }
            if (!exists) cartItems.add(new CartItem(selectedProduct, qty));
            
            view.getTxtQtyInput().clear();
            view.getTblProduct().getSelectionModel().clearSelection();
            calculateTotal();
        } catch (NumberFormatException e) { showAlert("Error", "Qty harus angka!"); }
    }

    private void removeFromCart() {
        CartItem selected = view.getTblCart().getSelectionModel().getSelectedItem();
        if (selected != null) { cartItems.remove(selected); calculateTotal(); }
    }

    private void editQuantity() {
        CartItem selected = view.getTblCart().getSelectionModel().getSelectedItem();
        if (selected != null) {
            TextInputDialog dialog = new TextInputDialog(String.valueOf(selected.getQuantity()));
            dialog.setTitle("Ubah Jumlah");
            dialog.setContentText("Jumlah Baru:");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(qtyStr -> {
                try {
                    int newQty = Integer.parseInt(qtyStr);
                    if (newQty > 0 && selected.getProduct().getStock() >= newQty) {
                        cartItems.remove(selected);
                        cartItems.add(new CartItem(selected.getProduct(), newQty));
                        calculateTotal();
                    }
                } catch (Exception e) {}
            });
        }
    }

    private void calculateChange(String payStr) {
        if (payStr == null || payStr.isEmpty()) { view.getLblChange().setText("Rp 0"); return; }
        try {
            double payAmount = Double.parseDouble(payStr);
            double change = payAmount - currentGrandTotal;
            view.getLblChange().setText(change < 0 ? "Kurang" : formatRupiah(change));
            view.getLblChange().setStyle(change < 0 ? "-fx-text-fill: red; -fx-font-weight: bold;" : "-fx-text-fill: #27ae60; -fx-font-weight: bold;");
        } catch (Exception e) { view.getLblChange().setText("Input Salah"); }
    }

    private void processCheckout() {
        if (cartItems.isEmpty()) { showAlert("Error", "Keranjang kosong"); return; }
        String payStr = view.getTxtPayment().getText().trim();
        if (payStr.isEmpty()) { showAlert("Error", "Input pembayaran dulu"); return; }
        
        try {
            double payAmount = Double.parseDouble(payStr);
            if (payAmount < currentGrandTotal) { showAlert("Error", "Uang Kurang!"); return; }

            String trxId = "TRX-" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            double subtotal = cartItems.stream().mapToDouble(CartItem::getSubtotal).sum();
            
            double voucherDiscAmount = (activeVoucher != null) ? subtotal * activeVoucher.getDiscountPercent() : 0;
            double thresholdDiscAmount = (subtotal > THRESHOLD_LIMIT) ? subtotal * THRESHOLD_DISC_PERCENT : 0;
            double totalDiscount = voucherDiscAmount + thresholdDiscAmount;
            
            String method = view.getCmbPaymentMethod().getValue();
            double afterDiscount = subtotal - totalDiscount;
            Pembayaran pembayaran = ("E-WALLET".equals(method)) ? new EWalletPayment(afterDiscount) : new CashPayment(afterDiscount);
            double totalAmount = pembayaran.hitungTotalBayar(); 
            double tax = totalAmount - afterDiscount;
            double changeAmount = payAmount - totalAmount;

            Transaction trx = new Transaction(trxId, totalAmount, method, currentUser.getId(), 0, totalDiscount);
            
            transactionDAO.save(trx, new ArrayList<>(cartItems));

            ReceiptDialog.show(trx, new ArrayList<>(cartItems), currentUser, subtotal, totalDiscount, tax, payAmount, changeAmount);

            cartItems.clear();
            view.getTxtPayment().clear();
            activeVoucher = null; 
            view.getTxtVoucher().clear();
            view.getLblDiscountInfo().setText("");
            loadProducts();
            calculateTotal();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Gagal", e.getMessage());
        }
    }

    private void showAlert(String t, String c) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle(t); a.setHeaderText(null); a.setContentText(c); a.show();
    }
    
    private String formatRupiah(double number) {
        return String.format("Rp %,.0f", number).replace(',', '.');
    }
}