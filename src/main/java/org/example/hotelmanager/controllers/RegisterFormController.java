package org.example.hotelmanager.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.hotelmanager.FormBuilder;
import org.example.hotelmanager.data.MongoDatabaseConnection;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class RegisterFormController implements Initializable {
    MongoDatabaseConnection mongoDatabaseConnection = new MongoDatabaseConnection();
    private Stage stage;
    /* Елементи вкладки Гості */
    @FXML private DatePicker client_date_of_birth;
    @FXML private TextField client_email;
    @FXML private TextField client_first_name;
    @FXML private PasswordField client_password_field;
    @FXML private PasswordField client_password_field_confirm;
    @FXML private TextField client_password_text;
    @FXML private TextField client_phone_number;
    @FXML private TextField client_last_name;
    @FXML private CheckBox client_show_password_check;
    @FXML private Button create_client_acc_btn;
    @FXML private Button client_go_back_btn;

    /* Елементи вкладки Адміністрація */
    @FXML private TextField admin_login;
    @FXML private TextField hotel_address;
    @FXML private TextField hotel_email_field;
    @FXML private TextField hotel_name;
    @FXML private PasswordField hotel_password_confirm_field;
    @FXML private PasswordField hotel_password_field;
    @FXML private TextField hotel_password_text;
    @FXML private TextField hotel_phone_number;
    @FXML private TextField hotel_rooms_count_field;
    @FXML private CheckBox hotel_show_password_check;
    @FXML private Button register_hotel_btn;
    @FXML private Button admin_go_back_btn;
    private final FormBuilder formBuilder = new FormBuilder();

    public void initialize(URL url, ResourceBundle resourceBundle){
        /* Форматування тексту для реєстрації готелю */
        hotel_rooms_count_field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                hotel_rooms_count_field.setText(newValue.replaceAll("\\D", ""));
            }
        });
        hotel_phone_number.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                hotel_phone_number.setText(newValue.replaceAll("\\D", ""));
            }
        });

        /* Форматування тексту для реєстрації клієнта */
        client_phone_number.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                client_phone_number.setText(newValue.replaceAll("\\D", ""));
            }
        });
        client_date_of_birth.setValue(LocalDate.now().minusYears(18));
    }

    /* Початок коду для створення акаунту ГОСТЕЙ (клієнт) -------------------------------------------------------- */

    public void registerClientClick(ActionEvent event) throws IOException{
        TextField[] textFields = {
                client_first_name,
                client_last_name,
                client_email,
                client_phone_number,
        };
        for(TextField textField : textFields){
            if (textField.getText().isEmpty()){
                formBuilder.errorValidation("Всі поля повинні бути заповнені");
                return;
            }
        }
        if(client_date_of_birth.getValue().isAfter(LocalDate.now().minusYears(18))){
            formBuilder.errorValidation("Лише повнолітні особи можуть створити клієнтський акаунт");
            return;
        }
        if(!client_password_field.getText().equals(client_password_field_confirm.getText())){
            formBuilder.errorValidation("Паролі у полях не збігаються");
            return;
        }
        if(client_password_field.getText().length() < 8){
            formBuilder.errorValidation("Пароль повинен містити 8 або більше символів");
            return;
        }
        if(mongoDatabaseConnection.registerClientAccount(
                client_first_name.getText(),
                client_last_name.getText(),
                client_email.getText(),
                client_phone_number.getText(),
                client_date_of_birth.getValue(),
                client_password_field.getText())
        ){
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            formBuilder.openWindow(stage, "client-forms/client-form.fxml",
                    "Версія для гостя | Hotelis", 1100, 750);
        }
        else{
            formBuilder.errorValidation("Користувач з такою електронною поштою вже існує");
        }
    }

    public void showClientsPasswordClick(ActionEvent event){
        if(client_show_password_check.isSelected()){
            client_password_text.setText(client_password_field.getText());
            client_password_text.setVisible(true);
            client_password_field.setVisible(false);
            return;
        }
        client_password_field.setText(client_password_text.getText());
        client_password_field.setVisible(true);
        client_password_text.setVisible(false);
    }
    public void editClientPassword(MouseEvent event){
        client_password_field.setText(client_password_text.getText());
        client_password_field.setVisible(true);
        client_password_text.setVisible(false);
        client_show_password_check.setSelected(false);
        client_password_field.requestFocus();
    }

    /* Початок коду для створення акаунту ГОТЕЛЮ (адмін) --------------------------------------------------------- */

    public void showAdminPasswordClick(ActionEvent event){
        if(hotel_show_password_check.isSelected()){
            hotel_password_text.setText(hotel_password_field.getText());
            hotel_password_text.setVisible(true);
            hotel_password_field.setVisible(false);
            hotel_password_text.setEditable(false);
            return;
        }
        hotel_password_text.setEditable(true);
        hotel_password_field.setText(hotel_password_text.getText());
        hotel_password_field.setVisible(true);
        hotel_password_text.setVisible(false);
    }
    public void editAdminPassword(MouseEvent event){
        hotel_password_field.setText(hotel_password_text.getText());
        hotel_password_field.setVisible(true);
        hotel_password_text.setVisible(false);
        hotel_show_password_check.setSelected(false);
        hotel_password_field.requestFocus();
    }


    public void registerHotelClick(ActionEvent event) throws IOException{
        TextField[] textFields = {
                hotel_name,
                hotel_address,
                admin_login,
                hotel_phone_number,
                hotel_email_field,
                hotel_rooms_count_field
        };
        for(TextField textField : textFields){
            if (textField.getText().isEmpty()){
                formBuilder.errorValidation("Всі поля повинні бути заповнені");
                return;
            }
        }
        if(!hotel_password_field.getText().equals(hotel_password_confirm_field.getText())){
            formBuilder.errorValidation("Паролі у полях не збігаються");
            return;
        }
        if(hotel_password_field.getText().length() < 8){
            formBuilder.errorValidation("Пароль повинен містити 8 або більше символів");
            return;
        }
        String roomsCountString = hotel_rooms_count_field.getText();
        int roomsCount = Integer.parseInt(roomsCountString);
        if (roomsCount > 25){
            formBuilder.errorValidation("Кількість кімнат не може перевищувати 25");
            return;
        }
        if(mongoDatabaseConnection.registerHotel(
                hotel_name.getText(),
                hotel_address.getText(),
                admin_login.getText(),
                hotel_password_field.getText(),
                hotel_email_field.getText(),
                hotel_phone_number.getText(),
                roomsCount)
        ){
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            formBuilder.openWindow(stage, "admin-forms/admin-form.fxml",
                    "Версія для адміністратора | Hotelis", 1100, 750);
        }
        else{
            formBuilder.errorValidation("Готель з такими даними вже існує");
        }
    }

    /* Інші методи ----------------------------------------------------------------------------------------- */

    public void goBackClick(ActionEvent event) throws IOException {
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        formBuilder.openWindow(stage, "login-form.fxml",
                "Авторизація | Hotelis", 700, 500);
    }

}
