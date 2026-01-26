package com.upb.agripos.view;

import com.upb.agripos.dao.UserDAOImpl;
import com.upb.agripos.model.User;
import com.upb.agripos.service.AuthService;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginView {
    
    private Stage stage; // Reference ke window utama
    
    public LoginView(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #f0f2f5;");

        // Komponen UI
        Label lblTitle = new Label("Login Agri-POS");
        lblTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        TextField txtUsername = new TextField();
        txtUsername.setPromptText("Username");
        
        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Password");

        Button btnLogin = new Button("MASUK");
        btnLogin.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        btnLogin.setPrefWidth(200);

        Label lblError = new Label();
        lblError.setStyle("-fx-text-fill: red;");

        // Event Handler Login
        btnLogin.setOnAction(e -> {
            String user = txtUsername.getText();
            String pass = txtPassword.getText();

            try {
                // Panggil Service
                AuthService authService = new AuthService(new UserDAOImpl());
                User loggedInUser = authService.login(user, pass);

                // --- PERUBAHAN ADA DI SINI ---
                // Jika login sukses, langsung buka MainLayout (Dashboard Baru)
                new MainLayout(stage, loggedInUser).show();
                
            } catch (Exception ex) {
                // Jika gagal, tampilkan pesan error merah di bawah tombol
                lblError.setText(ex.getMessage());
            }
        });

        root.getChildren().addAll(lblTitle, txtUsername, txtPassword, btnLogin, lblError);

        Scene scene = new Scene(root, 400, 350);
        stage.setTitle("Login - Agri POS System");
        stage.setScene(scene);
        stage.show();
    }
}