package org.example.hotelmanager.controllers;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.example.hotelmanager.FormBuilder;
import java.io.IOException;
import javafx.scene.control.*;
import org.example.hotelmanager.data.MongoDatabaseConnection;
public class LoginFormController {
    public Button cancel_exit_btn;
    public Button login_btn;
    public PasswordField pass_field;
    public TextField login_field;
    MongoDatabaseConnection mongoDatabaseConnection = new MongoDatabaseConnection();
    private Stage stage = new Stage();
    private FormBuilder formBuilder = new FormBuilder();
    public void registerButtonClick(ActionEvent event) throws IOException{
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        formBuilder.openWindow(stage, "register-form.fxml",
                "Створити акаунт | Hotelis", 700, 500);
    }
    public void loginButtonClick(ActionEvent event){
        if(login_field.getText().isEmpty() || pass_field.getText().isEmpty()) {
            formBuilder.errorValidation("Усі обов'язкові поля повинні бути заповненні");
            return;
        }
        mongoDatabaseConnection.loginAccount(login_field.getText(), pass_field.getText(), event);
    }
    public void cancelAndExit(ActionEvent event){
        System.exit(0);
    }
}
