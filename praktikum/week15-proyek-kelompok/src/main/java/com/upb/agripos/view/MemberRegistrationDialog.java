package com.upb.agripos.view;

import com.upb.agripos.dao.CustomerDAO;
import com.upb.agripos.dao.CustomerDAOImpl;
import com.upb.agripos.model.Customer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MemberRegistrationDialog extends Stage {

    private final TextField txtName;
    private final TextField txtPhone;
    private final TextField txtCode;
    private final CustomerDAO customerDAO;

    public MemberRegistrationDialog() {
        this.customerDAO = new CustomerDAOImpl();

        // 1. Setup Window
        setTitle("Registrasi Member Baru");
        initModality(Modality.APPLICATION_MODAL); // Block window belakangnya
        setResizable(false);

        // 2. Setup Layout Form
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        // 3. Components
        Label lblName = new Label("Nama Lengkap:");
        txtName = new TextField();
        txtName.setPromptText("Contoh: Budi Santoso");

        Label lblPhone = new Label("No. Handphone:");
        txtPhone = new TextField();
        txtPhone.setPromptText("0812xxxx");

        Label lblCode = new Label("Kode Member:");
        txtCode = new TextField();
        // Auto-generate kode member sederhana biar cepat (MEM + Epoch Time)
        // User tetap bisa ganti kalau mau
        txtCode.setText("MEM-" + System.currentTimeMillis() % 100000); 

        // 4. Buttons
        Button btnSave = new Button("Simpan Member");
        btnSave.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;");
        
        Button btnCancel = new Button("Batal");
        btnCancel.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");

        HBox buttonBox = new HBox(10, btnSave, btnCancel);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        // 5. Add to Grid
        grid.add(lblName, 0, 0);
        grid.add(txtName, 1, 0);
        
        grid.add(lblPhone, 0, 1);
        grid.add(txtPhone, 1, 1);
        
        grid.add(lblCode, 0, 2);
        grid.add(txtCode, 1, 2);
        
        grid.add(buttonBox, 1, 3);

        // 6. Event Handling
        btnSave.setOnAction(e -> handleSave());
        btnCancel.setOnAction(e -> close());

        // 7. Show Scene
        Scene scene = new Scene(grid, 350, 200);
        setScene(scene);
    }

    private void handleSave() {
        try {
            // Validasi Input
            if (txtName.getText().isEmpty() || txtPhone.getText().isEmpty() || txtCode.getText().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Peringatan", "Semua kolom harus diisi!");
                return;
            }

            // Buat Object Customer Baru (Pakai Constructor yang baru kita gabung tadi)
            Customer newMember = new Customer(
                txtName.getText(),
                txtPhone.getText(),
                txtCode.getText()
            );

            // Simpan ke Database
            customerDAO.save(newMember);

            // Beri Feedback Sukses
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Member berhasil didaftarkan!\nKode: " + newMember.getMemberCode());
            
            // Tutup Dialog
            close();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Gagal", "Gagal menyimpan data: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}