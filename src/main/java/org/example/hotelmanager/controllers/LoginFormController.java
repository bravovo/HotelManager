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
import org.example.hotelmanager.controllers.admin.AdminRoomsFormController;
import org.example.hotelmanager.data.MongoDatabaseConnection;
import org.example.hotelmanager.objects.Hotel;
import org.example.hotelmanager.objects.HotelHolder;

public class LoginFormController {
    MongoDatabaseConnection mongoDatabaseConnection = new MongoDatabaseConnection();
    @FXML
    private Button logibtn;

    @FXML
    private TextField login_field;

    @FXML
    private PasswordField pass_field;
    private Stage stage = new Stage();
    Hotel hotel;

    private FormBuilder formBuilder = new FormBuilder();

    public void registerButtonClick(ActionEvent event) throws IOException{
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        formBuilder.openWindow(stage, "register-form.fxml",
                "Створити акаунт | Система управління готелями", 700, 500);
    }

    public void loginButtonClick(ActionEvent event) throws IOException{ //TODO ПЕРЕВІРКА НА ТИП АКАУНТА (АДМІН/КЛІЄНТ)
        Document loginDoc = new Document("login", login_field.getText()).append("password", pass_field.getText());
        hotel = mongoDatabaseConnection.loginAcc(loginDoc);
        if(hotel.getHotel_name().length() != 0){
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            formBuilder.openWindow(stage, "admin-forms/admin-form.fxml",
                    "Версія для адміністратора | Система управління готелями", 1100, 750, hotel);
            if(hotel.getEmail() == null){
                formBuilder.openDialog("after-register-form.fxml", "Введення необхідних даних", 400, 400);
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
