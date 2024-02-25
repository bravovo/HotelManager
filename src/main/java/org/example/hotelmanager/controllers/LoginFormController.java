package org.example.hotelmanager.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.bson.Document;
import org.example.hotelmanager.FormBuilder;

import java.io.IOException;
import javafx.scene.control.*;
import org.example.hotelmanager.data.MongoDatabaseConnection;
import org.example.hotelmanager.objects.Hotel;

public class LoginFormController {
    @FXML
    private Button logibtn;

    @FXML
    private TextField login_field;

    @FXML
    private PasswordField pass_field;
    private Stage stage = new Stage();
    private Scene scene;

    private FormBuilder formBuilder = new FormBuilder();

    public void registerButtonClick(ActionEvent event) throws IOException{
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        formBuilder.openWindow(stage, "register-form.fxml",
                "Створити акаунт | Система управління готелями", 700, 500);
    }

    public void loginButtonClick(ActionEvent event) throws IOException{ //TODO ПЕРЕВІРКА НА ТИП АКАУНТА (АДМІН/КЛІЄНТ)
        Document loginDoc = new Document("login", login_field.getText()).append("password", pass_field.getText());
        MongoDatabaseConnection mongoDatabaseConnection = new MongoDatabaseConnection();
        Hotel hotel = mongoDatabaseConnection.loginAcc(loginDoc);
        if(hotel != null){
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            System.out.println(hotel.getAddress());
            formBuilder.openWindow(stage, "admin-forms/admin-form.fxml",
                    "Версія для адміністратора | Система управління готелями", 1100, 750, hotel);
            if(hotel.getEmail() == null){
                formBuilder.openDialog("after-register-form.fxml", "Введення необіхдних даних", 400, 400);
            }
        }
        else{
            formBuilder.errorValidation("Такого облікового запису не існує.\n\t\tСтворіть аккаунт");
        }
    }

    public void cancelAndExit(ActionEvent event){
        System.exit(0);
    }
}
