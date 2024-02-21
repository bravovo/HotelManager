package org.example.hotelmanager.controllers;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.hotelmanager.ManagerApplication;
import org.example.hotelmanager.data.MongoDatabaseConnection;

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
            pass_match_label.setText("Passwords in password fields are not matching");
            labelFading(pass_match_label);
            //return;
        }
        else if(pass_field.getText().length() < 8){
            pass_match_label.setText("Password must contain 8 or more characters");
            labelFading(pass_match_label);
            //return;
        }
    }

    public void createHotelClick(ActionEvent event){
        if(!admin_pass.getText().equals(pass_field_confirm_admin.getText())){
            pass_match_label_admin.setText("Passwords in password fields are not matching");
            labelFading(pass_match_label_admin);
            return;
        }
        if(admin_pass.getText().length() < 8){
            pass_match_label_admin.setText("Password must contain 8 or more characters");
            labelFading(pass_match_label_admin);
            return;
        }
        String hotel_name = hotelName.getText();
        String hotel_address = hotelAddress.getText();
        String hotel_admin = adminLogin.getText();
        String hotel_pass = admin_pass.getText();
        MongoDatabaseConnection mongoDatabaseConnection = new MongoDatabaseConnection();
        if(mongoDatabaseConnection.createHotelObj(hotel_name, hotel_address, hotel_admin, hotel_pass, 0)){
            pass_match_label_admin.setText("Hotel was successfully created");
            labelFading(pass_match_label_admin);
        }
        else{
            pass_match_label_admin.setText("Hotel already exists in database");
            labelFading(pass_match_label_admin);
        }
    }
    public void labelFading(Label label){
        label.setVisible(true);
        FadeTransition fadeOutTransition = new FadeTransition(Duration.millis(2500), pass_match_label_admin);
        fadeOutTransition.setFromValue(1.0);
        fadeOutTransition.setToValue(0.0);
        fadeOutTransition.play();
    }

    public void goBackClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ManagerApplication.class.getResource("login-form.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load(), 700, 500);
        stage.setScene(scene);
        stage.setTitle("Hotel Manager Studio");
        stage.show();
    }
}
