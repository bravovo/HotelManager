package org.example.hotelmanager.controllers;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

public class RegisterFormController {

    @FXML
    private TextField admin_pass_text;

    @FXML
    private CheckBox show_admin_pass;

    @FXML
    private Label pass_match_label_admin;

    @FXML
    private PasswordField pass_field_admin;

    @FXML
    private PasswordField pass_field_confirm_admin;

    @FXML
    private Button create_acc_btn;

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

    public void showPasswordClick(ActionEvent event){
        if(show_pass_check.isSelected()){
            pass_text.setText(pass_field.getText());
            pass_text.setVisible(true);
            pass_field.setVisible(false);
        }else{
            pass_field.setText(pass_text.getText());
            pass_text.setVisible(false);
            pass_field.setVisible(true);
        }
    }

    public void showAdminPasswordClick(ActionEvent event){
        if(show_admin_pass.isSelected()){
            admin_pass_text.setText(pass_field_admin.getText());
            admin_pass_text.setVisible(true);
            pass_field_admin.setVisible(false);
        }else{
            pass_field_admin.setText(admin_pass_text.getText());
            admin_pass_text.setVisible(false);
            pass_field_admin.setVisible(true);
        }
    }

    // TODO: Додати додаткову перевірку схожості паролів, коли пароль зверху показується.
    //  Протестувати найбільше можливих випадків
    public void createAccountClick(ActionEvent event){
        if(!pass_field.getText().equals(pass_field_confirm.getText())){
            pass_match_label.setVisible(true);
            FadeTransition fadeOutTransition = new FadeTransition(Duration.millis(2500), pass_match_label);
            fadeOutTransition.setFromValue(1.0);
            fadeOutTransition.setToValue(0.0);
            fadeOutTransition.play();
        }
    }

    public void createHotelClick(ActionEvent event){
        if(!pass_field_admin.getText().equals(pass_field_confirm_admin.getText())){
            pass_match_label_admin.setVisible(true);
            FadeTransition fadeOutTransition = new FadeTransition(Duration.millis(2500), pass_match_label_admin);
            fadeOutTransition.setFromValue(1.0);
            fadeOutTransition.setToValue(0.0);
            fadeOutTransition.play();
        }
    }
}
