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
        fxmlLoader = new FXMLLoader(ManagerApplication.class.getResource("login-form.fxml"));
        scene = new Scene(fxmlLoader.load(), 700, 500);
        stage.setTitle("Система управління готелями");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
    public void openWindow(Stage stage, String source, String title, int x, int y) throws IOException{
        fxmlLoader = new FXMLLoader(ManagerApplication.class.getResource(source));
        scene = new Scene(fxmlLoader.load(), x, y);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {launch();}
}