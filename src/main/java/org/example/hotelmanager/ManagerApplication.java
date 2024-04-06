package org.example.hotelmanager;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class ManagerApplication extends Application {
    public ManagerApplication() {
    }

    @Override
    public void start(Stage stage) throws IOException {
        FormBuilder formBuilder = new FormBuilder();
        formBuilder.openWindow(stage, "login-form.fxml", "Авторизація | Hotelis", 700, 500);
    }
    public static void main(String[] args) {launch();}
}