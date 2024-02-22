package org.example.hotelmanager.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.bson.Document;
import org.example.hotelmanager.ManagerApplication;
import java.io.IOException;
import javafx.scene.control.*;
import org.example.hotelmanager.data.MongoDatabaseConnection;

public class LoginFormController {
    @FXML
    private Button logibtn;

    @FXML
    private TextField login_field;

    @FXML
    private PasswordField pass_field;
    private Stage stage = new Stage();
    private Scene scene;

    private ManagerApplication managerApplication = new ManagerApplication();

    public void registerButtonClick(ActionEvent event) throws IOException{
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        managerApplication.openWindow(stage, "register-form.fxml",
                "Створити акаунт | Система управління готелями", 700, 500);
    }

    public void loginButtonClick(ActionEvent event) throws IOException{
        Document loginDoc = new Document("login", login_field.getText()).append("password", pass_field.getText());
        MongoDatabaseConnection mongoDatabaseConnection = new MongoDatabaseConnection();
        if(mongoDatabaseConnection.loginAcc(loginDoc)){
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            managerApplication.openWindow(stage, "admin-form.fxml",
                    "Версія для адміністратора | Система управління готелями", 1100, 750);
        }
        else{
            return;
        }
    }

    public void cancelAndExit(ActionEvent event){
        System.exit(0);
    }
}
