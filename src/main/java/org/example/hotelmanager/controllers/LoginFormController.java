package org.example.hotelmanager.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.hotelmanager.ManagerApplication;
import java.io.IOException;

public class LoginFormController {
    private Stage stage;
    private Scene scene;

    public void registerButtonClick(ActionEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(ManagerApplication.class.getResource("register-form.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load(), 700, 500);
        stage.setScene(scene);
        stage.setTitle("Create Account | Hotel Manager Studio");
        stage.show();
    }
}
