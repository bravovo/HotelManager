package org.example.hotelmanager.controllers;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.hotelmanager.FormBuilder;
import org.example.hotelmanager.data.MongoDatabaseConnection;
import org.example.hotelmanager.objects.Hotel;

import java.io.IOException;

public class RegisterFormController {

    private Stage stage;
    private Scene scene;

    @FXML
    private TextField adminLogin;

    @FXML
    private TextField hotelAddress;

    @FXML
    private TextField hotelName;

    @FXML
    private TextField admin_pass_text;

    @FXML
    private CheckBox show_admin_pass;

    @FXML
    private Label pass_match_label_admin;

    @FXML
    private PasswordField admin_pass;

    @FXML
    private PasswordField pass_field_confirm_admin;

    @FXML
    private Label pass_match_label;

    @FXML
    private PasswordField pass_field;

    @FXML
    private PasswordField pass_field_confirm;

    @FXML
    private TextField pass_text;

    @FXML
    private CheckBox show_pass_check;

    private FormBuilder formBuilder = new FormBuilder();

    public void showPasswordClick(ActionEvent event){
        if(show_pass_check.isSelected()){
            pass_text.setText(pass_field.getText());
            pass_text.setVisible(true);
            pass_field.setVisible(false);
            return;
        }
        pass_field.setText(pass_text.getText());
        pass_field.setVisible(true);
        pass_text.setVisible(false);
    }

    public void showAdminPasswordClick(ActionEvent event){
        if(show_admin_pass.isSelected()){
            admin_pass_text.setText(admin_pass.getText());
            admin_pass_text.setVisible(true);
            admin_pass.setVisible(false);
            admin_pass_text.setEditable(false);
            return;
        }
        admin_pass_text.setEditable(true);
        admin_pass.setText(admin_pass_text.getText());
        admin_pass.setVisible(true);
        admin_pass_text.setVisible(false);
    }

    public void editPass(MouseEvent event){
        pass_field.setText(pass_text.getText());
        pass_field.setVisible(true);
        pass_text.setVisible(false);
        show_pass_check.setSelected(false);
        pass_field.requestFocus();
    }

    public void editAdminPass(MouseEvent event){
        admin_pass.setText(admin_pass_text.getText());
        admin_pass.setVisible(true);
        admin_pass_text.setVisible(false);
        show_admin_pass.setSelected(false);
        admin_pass.requestFocus();
    }

    public void createAccountClick(ActionEvent event){
        if(!pass_field.getText().equals(pass_field_confirm.getText())){
            formBuilder.errorValidation("Паролі у полях не збігаються");
            //return; Прибрати коментар, далі буде код
        }
        else if(pass_field.getText().length() < 8){
            formBuilder.errorValidation("Пароль повинен містити 8 або більше символів");
            //return; Прибрати коментар, далі буде код
        }
    }

    public void createHotelClick(ActionEvent event) throws IOException{
        if(hotelName.getText().length() == 0 || hotelAddress.getText().length() == 0 ||
                adminLogin.getText().length() == 0){
            formBuilder.errorValidation("Всі поля повинні бути заповнені");
            return;
        }
        if(!admin_pass.getText().equals(pass_field_confirm_admin.getText())){
            formBuilder.errorValidation("Паролі у полях не збігаються");
            return;
        }
        if(admin_pass.getText().length() < 8){
            formBuilder.errorValidation("Пароль повинен містити 8 або більше символів");
            return;
        }
        String hotel_name = hotelName.getText();
        String hotel_address = hotelAddress.getText();
        String hotel_admin = adminLogin.getText();
        String hotel_pass = admin_pass.getText();
        MongoDatabaseConnection mongoDatabaseConnection = new MongoDatabaseConnection();
        Hotel registeredHotel = mongoDatabaseConnection.createHotelObj(hotel_name, hotel_address, hotel_admin, hotel_pass);
        if(registeredHotel != null){
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            formBuilder.openWindow(stage, "admin-forms/admin-form.fxml",
                    "Версія для адміністратора | Система управління готелями", 1100, 750, registeredHotel);
            formBuilder.openDialog("after-register-form.fxml", "Завершення реєстрації", 400, 400);
        }
        else{
            formBuilder.errorValidation("Такий готель вже існує"); // Додати причину повідомлення (існуючий логін чи адреса)
        }
    }
    public void goBackClick(ActionEvent event) throws IOException {
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        formBuilder.openWindow(stage, "login-form.fxml",
                "Система управління готелями", 700, 500);
    }

}
