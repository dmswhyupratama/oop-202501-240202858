package com.upb.agripos;

import com.upb.agripos.view.LoginView;

import javafx.application.Application;
import javafx.stage.Stage;

public class AppJavaFX extends Application {

    @Override
    public void start(Stage stage) {
        // Identitas Praktikum (Requirement Week 1)
        System.out.println("Hello World, I am Dimas Wahyu Pratama-240202858");

        // Langsung buka Login View
        new LoginView(stage).show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}