package org.example.hotelmanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ManagerApplication extends Application {
    private FXMLLoader fxmlLoader;
    private Scene scene;

    public ManagerApplication() {
    }

    @Override
    public void start(Stage stage) throws IOException {
        FormBuilder formBuilder = new FormBuilder();
        formBuilder.openWindow(stage, "login-form.fxml", "Система управління готелями", 700, 500);
    }
    public static void main(String[] args) {launch();}
}