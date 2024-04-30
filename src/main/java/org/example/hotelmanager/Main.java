package org.example.hotelmanager;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FormBuilder formBuilder = new FormBuilder();
        formBuilder.openWindow(primaryStage, "login-form.fxml","Авторизація", 700, 500);
    }
}
