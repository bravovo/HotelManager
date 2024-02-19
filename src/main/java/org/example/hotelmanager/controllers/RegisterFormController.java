package org.example.hotelmanager.controllers;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class RegisterFormController {

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
            pass_match_label.setVisible(true);
            FadeTransition fadeOutTransition = new FadeTransition(Duration.millis(2500), pass_match_label);
            fadeOutTransition.setFromValue(1.0);
            fadeOutTransition.setToValue(0.0);
            fadeOutTransition.play();
        }
    }

    public void createHotelClick(ActionEvent event){
        if(!admin_pass.getText().equals(pass_field_confirm_admin.getText())){
            pass_match_label_admin.setVisible(true);
            FadeTransition fadeOutTransition = new FadeTransition(Duration.millis(2500), pass_match_label_admin);
            fadeOutTransition.setFromValue(1.0);
            fadeOutTransition.setToValue(0.0);
            fadeOutTransition.play();
        }
    }
}
